package com.conmu.server;

import com.conmu.handler.WebSocketFrameHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author mucongcong
 * @date 2024/11/01 18:10
 * @since
 **/
public class WebSocketServer {
    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup).
                    channel(NioServerSocketChannel.class).
                    childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new HttpServerCodec());
                            pipeline.addLast(new ChunkedWriteHandler());
                            pipeline.addLast(new HttpObjectAggregator(8192));
                            pipeline.addLast(new WebSocketServerProtocolHandler("/wts"));
                            // 添加自定义的 WebSocket 处理器，继承 WebSocketFrameHandler 类并重写其方法即可
                            pipeline.addLast(new WebSocketFrameHandler());
                        }
                    });
            ChannelFuture future = bootstrap.bind(8080).sync(); // 绑定端口号为 8080 并开始监听连接请求
            future.channel().closeFuture().sync();
            // 等待服务器通道关闭，即等待客户端关闭连接或发生异常导致关闭连接时退出程序。
            // 如果不想要一直等待关闭连接，可以调用 future.channel().closeFuture().sync() 来等待关闭连接事件发生。
            // 如果想要设置超时时间，可以在调用该方法时传入超时时间即可。例如：future.channel().closeFuture().sync(10000)
            // 表示等待 10 秒后超时退出程序。注意：在 Netty 中，默认情况下，服务器不会自动关闭连接，
            // 需要手动调用 ChannelHandlerContext 的 close() 方法来关闭连接。
            // 因此，在实现 WebSocket 处理器时，需要手动关闭连接。
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // 优雅地关闭 bossGroup 和 workerGroup 中的所有线程和资源。
            bossGroup.shutdownGracefully();
            // 优雅地关闭 bossGroup 和 workerGroup 中的所有线程和资源。
            workerGroup.shutdownGracefully();
        }

    }
}