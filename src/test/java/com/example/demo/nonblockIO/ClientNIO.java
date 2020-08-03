package com.example.demo.nonblockIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class ClientNIO {

    private static SocketChannel socketChannel;
    private static Selector selector;
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 6667;
    private String username;
    public ClientNIO() throws IOException {
        selector = Selector.open();
        socketChannel = SocketChannel.open(new InetSocketAddress(HOST,PORT));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_WRITE);
        username = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println("客户端ok");
    }

    //向服务端发送数据
    public void sendMsg(String msg){
        try {
            socketChannel.write(ByteBuffer.wrap((username+" "+msg).getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ClientNIO clientNIO = new ClientNIO();
        clientNIO.sendMsg("你好呀!");
    }
}
