package com.example.demo.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
//网络客户端
public class NettyClient {
    public static void main(String[] args){
        //创建一个线程组(不像服务端需要有连接等待的线程池)
        EventLoopGroup group = new NioEventLoopGroup();
        try {

            //创建客户端的服务启动助手完成相应配置
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {//创建一个通道初始化对象
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new NettyClientHandler());//往pipeline中添加自定义的handler
                        }
                    });
            System.out.println("...Client is Ready...");
            //启动客户端去连接服务器端(通过启动助手)
            ChannelFuture cf = b.connect("127.0.0.1", 9999).sync();

            //关闭连接(异步非阻塞)
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }

    }
}