package com.july.rpc.transport.socket.server;

import com.july.rpc.entity.RpcRequest;
import com.july.rpc.entity.RpcResponse;
import com.july.rpc.handler.RequestHandler;
import com.july.rpc.provider.ServiceProvider;
import com.july.rpc.provider.ServiceProviderImpl;
import com.july.rpc.registry.NacosServiceRegistry;
import com.july.rpc.registry.ServiceRegistry;
import com.july.rpc.serializer.CommonSerializer;
import com.july.rpc.transport.AbstractRpcServer;
import com.july.rpc.transport.RpcServer;
import com.july.rpc.transport.socket.util.ObjectReader;
import com.july.rpc.transport.socket.util.ObjectWriter;
import com.july.rpc.util.ShutdownHook;
import com.july.rpc.util.ThreadPoolFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @author july
 */
@Slf4j
public class SocketServer extends AbstractRpcServer {

    private final ExecutorService threadPool;
    private final RequestHandler requestHandler = new RequestHandler();
    private CommonSerializer serializer;

    public SocketServer(String host, int port) {
        this(host, port, DEFAULT_SERIALIZER);
    }

    public SocketServer(String host, int port, int code) {
        this.host = host;
        this.port = port;
        threadPool = ThreadPoolFactory.createDefaultThreadPool("socket-rpc-server");
        registry = new NacosServiceRegistry();
        provider = new ServiceProviderImpl();
        serializer = CommonSerializer.getByCode(code);
    }

    @Override
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            log.info("服务器正在启动...");
            ShutdownHook.addClearAllHook();
            Socket socket;
            while((socket = serverSocket.accept()) != null) {
                log.info("客户端连接！{}:{}" ,socket.getInetAddress(), socket.getPort());
                threadPool.execute(new RequestHandlerThread(socket));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            log.error("连接时有错误发生：", e);
        }
    }


    class RequestHandlerThread implements Runnable {
        private final Socket socket;

        public RequestHandlerThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (InputStream inputStream = socket.getInputStream();
                 OutputStream outputStream = socket.getOutputStream()) {
                RpcRequest rpcRequest = (RpcRequest) ObjectReader.readObject(inputStream);
                Object result = requestHandler.handle(rpcRequest);
                RpcResponse<Object> response = RpcResponse.success(result);
                ObjectWriter.writeObject(outputStream, response, serializer);
            } catch (IOException e) {
                log.error("调用或发送时有错误发生：", e);
            }
        }
    }
}
