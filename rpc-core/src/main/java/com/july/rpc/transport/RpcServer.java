package com.july.rpc.transport;

import com.july.rpc.entity.RpcRequest;
import com.july.rpc.entity.RpcResponse;
import com.july.rpc.enumeration.ResponseCode;
import com.july.rpc.registry.ServiceRegistry;
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

    private static final int CORE_POOL_SIZE = 5;
    private static final int MAXIMUM_POOL_SIZE = 50;
    private static final int KEEP_ALIVE_TIME = 60;
    private static final int BLOCKING_QUEUE_CAPACITY = 100;
    private final ExecutorService threadPool;

    private final ServiceRegistry registry;

    public RpcServer(ServiceRegistry registry) {
        this.registry = registry;
        BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, workingQueue, threadFactory);
    }

    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            log.info("服务器正在启动...");
            Socket socket;
            while((socket = serverSocket.accept()) != null) {
                log.info("客户端连接！{}:{}" ,socket.getInetAddress(), socket.getPort());
                threadPool.execute(new RequestHandlerThread(socket, registry));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            log.error("连接时有错误发生：", e);
        }
    }

    static class RequestHandlerThread implements Runnable {
        private final Socket socket;

        private final ServiceRegistry registry;

        public RequestHandlerThread(Socket socket, ServiceRegistry serviceRegistry) {
            this.socket = socket;
            this.registry = serviceRegistry;
        }

        @Override
        public void run() {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                 ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
                RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
                String interfaceName = rpcRequest.getInterfaceName();
                Object service = registry.getService(interfaceName);
                Object result = invokeTargetMethod(rpcRequest, service);
                objectOutputStream.writeObject(RpcResponse.success(result));
                objectOutputStream.flush();
            } catch (Exception e) {
                log.error("调用或发送时有错误发生：", e);
            }
        }

        private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) throws Exception{
            Method method;
            try {
                method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            } catch (NoSuchMethodException e) {
                return RpcResponse.fail(ResponseCode.METHOD_NOT_FOUND);
            }
            return method.invoke(service, rpcRequest.getParameters());
        }
    }
}
