package com.conmu.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * @author mucongcong
 * @date 2024/11/01 18:15
 * @since
 **/
public class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> implements WebSocketFrameListener {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
        // 处理接收到的 WebSocket 消息
        if (msg instanceof TextWebSocketFrame) {
            TextWebSocketFrame textFrame = (TextWebSocketFrame) msg;
            String text = textFrame.text();
            System.out.println("Received text message: " + text);
            // 处理接收到的文本消息
        } else if (msg instanceof BinaryWebSocketFrame) {
            BinaryWebSocketFrame binaryFrame = (BinaryWebSocketFrame) msg;
            byte[] bytes = binaryFrame.content().array();
            // 处理接收到的二进制消息
        } else if (msg instanceof PingWebSocketFrame) {
            // 处理收到的 Ping 消息
        } else if (msg instanceof PongWebSocketFrame) {
            // 处理收到的 Pong 消息
        } else if (msg instanceof CloseWebSocketFrame) {
            // 处理收到的 Close 消息，并关闭连接
        }
    }


    @Override
    public void onTextMessage(TextWebSocketFrame msg) {
        // 处理接收到的文本消息
//        channelRead0(ctx, msg);
    }

    @Override
    public void onBinaryMessage(BinaryWebSocketFrame msg) {
        // 处理接收到的二进制消息
//        channelRead0(ctx, msg);
    }

    @Override
    public void onPingMessage(PingWebSocketFrame msg) {
        // 处理收到的 Ping 消息
    }

    @Override
    public void onPongMessage(PongWebSocketFrame msg) {
        // 处理收到的 Pong 消息
    }

    @Override
    public void onCloseMessage(CloseWebSocketFrame msg) {
        // 处理收到的 Close 消息，并关闭连接
    }
}