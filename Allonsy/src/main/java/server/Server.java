package server;

import arduino.ArduinoController;

import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * сервер, управляющий средствами приёма и обработки подключений и запросов
 */
public class Server {
    private static List<Connector> users; //список с объектами, соединяющими с пользователями
    //список коннекторов к ардуино. список для того, чтобы можно было поддерживать множество микроконтроллеров
    private static List<ArduinoController> controllers;

    public static void main(String[] args) {
        users = new LinkedList<>();
        controllers = new ArrayList<>();
        controllers.add(new ArduinoController("/dev/ttyACM0"));

        Waiter waiter = new Waiter();
        waiter.start();
        log("Запустили приём подключений");
    }

    public static void add(Socket newbie){
        Connector connector = new Connector(newbie,controllers.get(0));
        users.add(connector);
        connector.start();
    }

    public static void log(String msg){
        System.out.println(msg);
    }
}
