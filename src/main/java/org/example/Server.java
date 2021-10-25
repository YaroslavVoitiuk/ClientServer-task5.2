package org.example;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Server extends Thread {
    private static final int PORT = 13256;
    private static final String HOST = "127.0.0.1";
    private static final int BUFFER_SIZE = 2 << 10;

    @Override
    public void run() {
        boolean isWorking = true;
        ServerSocketChannel serverSocketChannel = null;
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(HOST, PORT));
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (isWorking) {
            try {
                assert serverSocketChannel != null;
                try (SocketChannel socketChannel = serverSocketChannel.accept()) {
                    final ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
                    while (socketChannel.isConnected()) {
                        int bytesCount = socketChannel.read(byteBuffer);
                        if (bytesCount == -1) break;
                        final String inputData = new String(byteBuffer.array(), 0, bytesCount,
                                StandardCharsets.UTF_8);
                        if (inputData.equals("end")) {
                            isWorking = false;
                            break;
                        }
                        String response = inputData.replaceAll("\\s", "");
                        socketChannel.write(ByteBuffer.wrap(response.getBytes(StandardCharsets.UTF_8)));
                        byteBuffer.clear();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
