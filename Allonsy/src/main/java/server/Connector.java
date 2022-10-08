package server;

import arduino.ArduinoController;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class Connector extends Thread {
    private Socket socket;         //сокет соединения
    private DataInputStream in;    //потоки входных и выходных данных
    private DataOutputStream out;
    private ArduinoController controller;

    public Connector(Socket socket, ArduinoController arduinoController) {
        controller = arduinoController;
        this.socket = socket;
        try {
            InputStream socketIn = socket.getInputStream();
            OutputStream socketOut = socket.getOutputStream();

            in = new DataInputStream(socketIn);
            out = new DataOutputStream(socketOut);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * стартуем поток
     * происходит чтение ввода
     */
    @Override
    public void run(){
        try {
            while (!Thread.interrupted()) {
                Server.log("Ждём новое сообшение");
                byte[] message = in.readNBytes(4);
                Server.log("Приняли пакет: "+ Arrays.toString(message));
                if (controller.analyzeMessage(message))
                    out.write(0);
                else
                    out.write(1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * отправляет пакет с сообщением на подключённое устройство
     * @param response массив байт с ответом
     */
    public void respond(byte[] response){
        Server.log("Отправляем пакет"+" размера "+response.length +" на " + socket.getInetAddress());
        try {
            out.write(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
