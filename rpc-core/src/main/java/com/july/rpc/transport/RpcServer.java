package com.july.rpc.transport;

import com.july.rpc.entity.RpcRequest;
import com.july.rpc.entity.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @author july
 */
@Slf4j
public class RpcServer {

    private final ExecutorService threadPool;

    public RpcServer() {
        int corePollSize = 5;
        int maximumPoolSize = 50;
        long keepAliveTime = 60;
        BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(100);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        threadPool = new ThreadPoolExecutor(corePollSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workingQueue, threadFactory);
    }

    public void register(Object service, int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            log.info("服务器正在启动...");
            Socket socket;
            while((socket = serverSocket.accept()) != null) {
                log.info("客户端连接！Ip为：" + socket.getInetAddress());
                threadPool.execute(new WorkerThread(socket, service));
            }
        } catch (IOException e) {
            log.error("连接时有错误发生：", e);
        }
    }

    static class WorkerThread implements Runnable {
        private final Socket socket;

        private final Object service;

        public WorkerThread(Socket socket, Object service) {
            this.socket = socket;
            this.service = service;
        }

        @Override
        public void run() {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                 ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
                RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
                Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
                Object returnObject = method.invoke(service, rpcRequest.getParameters());
                objectOutputStream.writeObject(RpcResponse.success(returnObject));
                objectOutputStream.flush();
            } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                log.error("调用或发送时有错误发生：", e);
            }
        }
    }
}
