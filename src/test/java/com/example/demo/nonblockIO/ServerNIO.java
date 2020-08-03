package com.example.demo.nonblockIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class ServerNIO {

    private static ServerSocketChannel serverSocketChannel;
    private static Selector selector;
    private static final int PORT = 6667;

    public ServerNIO() throws IOException {
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(PORT));//端口绑定
        selector = Selector.open();//选择器
        serverSocketChannel.configureBlocking(false);//非阻塞
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);//注册到selectors
    }

    public void listen() {
        try {
            while (true) {
                int count = selector.select(2000);
                if (count > 0) { //有时间处理
                    //遍历selectorKeys
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    //selector.selectedKeys().forEach(k-> System.out.println(k.hashCode()+"  "));
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        //监听到accept
                        if (key.isAcceptable()) {
                            SocketChannel socketChannel = serverSocketChannel.accept();
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector, SelectionKey.OP_READ);
                            System.out.println(socketChannel.getRemoteAddress() + "上线了！");
                        }
                        if (key.isReadable()) {//通道可读状态
                            //处理读
                            this.readMsg(key);
                        }
                        //移除key
                        iterator.remove();
                    }
                } else {
                    System.out.println("服务端等待中...");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void readMsg(SelectionKey key) throws IOException {
        SocketChannel socketChannel = null;
        try {
            socketChannel = (SocketChannel) key.channel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int count = socketChannel.read(byteBuffer);
            if (count > 0) {
                String msg = new String(byteBuffer.array());
                System.out.println("服务端收到客户端消息：" + msg);
            }
        } catch (Exception e) {
            System.out.println("异常");
        }finally {
            socketChannel.close();
        }

    }


    public static void main(String[] args) {
        try {
            ServerNIO serverNIO = new ServerNIO();
            serverNIO.listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
