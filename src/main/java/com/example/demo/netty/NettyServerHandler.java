package com.example.demo.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.concurrent.TimeUnit;

//服务器中的业务处理类
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    //数据读取事件
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        //传来的消息包装成字节缓冲区
        ByteBuf byteBuf = (ByteBuf) msg;
        //Netty提供了字节缓冲区的toString方法，并且可以设置参数为编码格式：CharsetUtil.UTF_8
        System.out.println("客户端发来的消息：" +ctx.channel().remoteAddress() +" "+ byteBuf.toString(CharsetUtil.UTF_8));

        /*
            耗时间的任务 异步执行  提交channel对应的taskqueue中
            普通任务 单个线程
         */
        ctx.channel().eventLoop().execute(() -> {
            try {
                Thread.sleep(10*1000);
                ctx.writeAndFlush(Unpooled.copiedBuffer("你好1",CharsetUtil.UTF_8));
            } catch (InterruptedException e) {
                System.out.println("服务端发生异常");
            }
        });

        ctx.channel().eventLoop().schedule(()->{
                ctx.writeAndFlush(Unpooled.copiedBuffer("你好2",CharsetUtil.UTF_8));
        },10L, TimeUnit.SECONDS);
    }

    //数据读取完毕事件
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx){
        //数据读取完毕，将信息包装成一个Buffer传递给下一个Handler，Unpooled.copiedBuffer会返回一个Buffer
        //调用的是事件处理器的上下文对象的writeAndFlush方法
        //意思就是说将  你好  传递给了下一个handler
        ctx.writeAndFlush(Unpooled.copiedBuffer("你好!", CharsetUtil.UTF_8));
    }

    //异常发生的事件
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause){
        //异常发生时关闭上下文对象
        ctx.close();
    }

    public static void main(String[] args) {
        ByteBuf byteBuf = Unpooled.buffer();
        for (int i = 0; i < 10; i++) {
            byteBuf.writeByte(i);
        }
        System.out.println("byteBuf容量:"+byteBuf.capacity());

            System.out.println(byteBuf.readByte());

            ByteBuf b = Unpooled.copiedBuffer("nihao",CharsetUtil.UTF_8);

        System.out.println(b.readableBytes());
        System.out.println(b.capacity());
        System.out.println(byteBuf.capacity());

    }
}