package com.conmu.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * @author mucongcong
 * @date 2024/10/16 23:45
 * @since
 **/
@ChannelHandler.Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", Charset.defaultCharset()));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuffer) throws Exception {
        System.out.println("Client received: " + byteBuffer.toString(Charset.defaultCharset()));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}