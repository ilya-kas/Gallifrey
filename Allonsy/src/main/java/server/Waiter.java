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
            while (true) {
                Socket newbie =server.accept();
                //todo проверка на безопастность(защита от взлома)
                Server.add(newbie);
                System.out.println("Подключился новый пользователь: "+newbie.getInetAddress()); //todo logs
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
