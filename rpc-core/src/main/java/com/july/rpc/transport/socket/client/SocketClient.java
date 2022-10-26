package com.july.rpc.transport.socket.client;

import com.july.rpc.entity.RpcRequest;
import com.july.rpc.entity.RpcResponse;
import com.july.rpc.enumeration.ResponseCode;
import com.july.rpc.enumeration.RpcError;
import com.july.rpc.exception.RpcException;
import com.july.rpc.loadbalancer.LoadBalancer;
import com.july.rpc.loadbalancer.RoundRobinLoadBalancer;
import com.july.rpc.registry.NacosServiceDiscovery;
import com.july.rpc.registry.ServiceDiscovery;
import com.july.rpc.serializer.CommonSerializer;
import com.july.rpc.transport.RpcClient;
import com.july.rpc.transport.socket.util.ObjectReader;
import com.july.rpc.transport.socket.util.ObjectWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author july
 */
@Slf4j
public class SocketClient implements RpcClient {

    private final ServiceDiscovery discovery;

    private CommonSerializer serializer;

    public SocketClient() {
        this(DEFAULT_SERIALIZER, new RoundRobinLoadBalancer());
    }

    public SocketClient(int code) {
        this(code, new RoundRobinLoadBalancer());
    }

    public SocketClient(LoadBalancer loadBalancer) {
        this(DEFAULT_SERIALIZER, loadBalancer);
    }

    public SocketClient(int code, LoadBalancer loadBalancer) {
        this.discovery = new NacosServiceDiscovery(loadBalancer);
        serializer = CommonSerializer.getByCode(code);
    }

    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        InetSocketAddress inetSocketAddress = discovery.lookupService(rpcRequest.getInterfaceName());
        try (Socket socket = new Socket()) {
            socket.connect(inetSocketAddress);
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            ObjectWriter.writeObject(outputStream, rpcRequest, serializer);
            Object obj = ObjectReader.readObject(inputStream);
            RpcResponse rpcResponse = (RpcResponse) obj;
            if (rpcResponse == null) {
                log.error("服务调用失败，service：{}", rpcRequest.getInterfaceName());
                throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, " service:" + rpcRequest.getInterfaceName());
            }
            if (rpcResponse.getStatusCode() == null || rpcResponse.getStatusCode() != ResponseCode.SUCCESS.getCode()) {
                log.error("调用服务失败, service: {}, response:{}", rpcRequest.getInterfaceName(), rpcResponse);
                throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, " service:" + rpcRequest.getInterfaceName());
            }
            return rpcResponse.getData();
        } catch (IOException e) {
            log.error("调用时有错误发生：", e);
            throw new RpcException("服务调用失败: ", e);
        }
    }
}
