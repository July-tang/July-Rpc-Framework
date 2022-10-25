package com.july.rpc.transport.socket.server;

import com.july.rpc.entity.RpcRequest;
import com.july.rpc.entity.RpcResponse;
import com.july.rpc.handler.RequestHandler;
import com.july.rpc.registry.ServiceRegistry;
import com.july.rpc.transport.RpcServer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @author july
 */
@Slf4j
public class SocketServer implements RpcServer {
    private static final int CORE_POOL_SIZE = 5;
    private static final int MAXIMUM_POOL_SIZE = 50;
    private static final int KEEP_ALIVE_TIME = 60;
    private static final int BLOCKING_QUEUE_CAPACITY = 100;
    private final ExecutorService threadPool;

    private final RequestHandler requestHandler;

    public SocketServer(ServiceRegistry registry) {
        requestHandler = new RequestHandler(registry);
        BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, workingQueue, threadFactory);
    }

    @Override
    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            log.info("服务器正在启动...");
            Socket socket;
            while((socket = serverSocket.accept()) != null) {
                log.info("客户端连接！{}:{}" ,socket.getInetAddress(), socket.getPort());
                threadPool.execute(new SocketServer.RequestHandlerThread(socket, requestHandler));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            log.error("连接时有错误发生：", e);
        }
    }

    static class RequestHandlerThread implements Runnable {
        private final Socket socket;

        private final RequestHandler requestHandler;

        public RequestHandlerThread(Socket socket, RequestHandler requestHandler) {
            this.socket = socket;
            this.requestHandler = requestHandler;
        }

        @Override
        public void run() {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                 ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
                RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
                Object result = requestHandler.handle(rpcRequest);
                objectOutputStream.writeObject(RpcResponse.success(result));
                objectOutputStream.flush();
            } catch (Exception e) {
                log.error("调用或发送时有错误发生：", e);
            }
        }
    }
}
