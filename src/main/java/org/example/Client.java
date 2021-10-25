package org.example;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client extends Thread {
    private static final int PORT = 13256;
    private static final String HOST = "127.0.0.1";
    private static final int BUFFER_SIZE = 2 << 10;

    @Override
    public void run() {
        InetSocketAddress socketAddress = new InetSocketAddress(HOST, PORT);
        SocketChannel socketChannel = null;
        try {
            socketChannel = SocketChannel.open();
            socketChannel.connect(socketAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (Scanner scanner = new Scanner(System.in)) {

            final ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
            String data;

            while (true) {
                assert socketChannel != null;
                System.out.println("Введите текст для форматирования...");
                data = scanner.nextLine();
                if (data.equals("end")) {
                    socketChannel.write(ByteBuffer.wrap(data.getBytes(StandardCharsets.UTF_8)));
                    break;
                }
                socketChannel.write(ByteBuffer.wrap(data.getBytes(StandardCharsets.UTF_8)));
                int bytesCount = socketChannel.read(byteBuffer);
                System.out.println("SERVER RESPONSE:" + new String(byteBuffer.array(), 0, bytesCount,
                        StandardCharsets.UTF_8).trim());
                byteBuffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
