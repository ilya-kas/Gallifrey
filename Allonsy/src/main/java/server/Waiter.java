package server;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * экземпляр waiter ожидает подключения нового устройства
 */
public class Waiter extends Thread { //подключает пользователей
    int port = 5432;
    ServerSocket server;

    public Waiter() {
        try {
            server = new ServerSocket(port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                Socket newbie = server.accept();
                Server.add(newbie);
                Server.log("Подключился новый пользователь: "+newbie.getInetAddress());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
