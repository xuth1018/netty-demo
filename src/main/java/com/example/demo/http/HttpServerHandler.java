package com.example.demo.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    //读取客户端数据
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {
        if( httpObject instanceof HttpRequest){
            System.out.println("msg类型:"+httpObject.getClass());
            System.out.println("客户端地址："+channelHandlerContext.channel().remoteAddress());
            System.out.println("channel HsahCode:"+channelHandlerContext.channel().hashCode());
            System.out.println("pipeline HashCode:"+channelHandlerContext.pipeline().hashCode());
            ByteBuf content = Unpooled.copiedBuffer("你好呀", CharsetUtil.UTF_8);
            //构建http响应
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK
                    ,content);
            response.headers().add(HttpHeaderNames.CONTENT_TYPE,"text/plain;charset=UTF-8");
            response.headers().add(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());
            channelHandlerContext.channel().writeAndFlush(response);
        }
    }
}
