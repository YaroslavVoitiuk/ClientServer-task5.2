package org.example;

public class Main {
    public static void main( String[] args ) {
        Thread thread = new Server();
        Thread thread1 = new Client();
        thread.start();
        thread1.start();
        //здесь я выбрал non-blocking так как в задании указано,
        // что пользователь может вводить данные бесконечно и выходит пока сервер
        // ожидает новую строку он может обрабатывать те которые пришли раньше
    }
}
