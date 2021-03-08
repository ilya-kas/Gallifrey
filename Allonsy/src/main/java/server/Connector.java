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
            while (true) {
                System.out.println("Ждём новое сообшение"); //todo logs
                byte[] message = in.readNBytes(10); //todo столько байт, сколько в протоколе. для безопастности от взлома
                System.out.println("Приняли пакет: "+ Arrays.toString(message));
                controller.analyzeMessage(message);
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
        System.out.println("Отправляем пакет"+" размера "+response.length +" на " + socket.getInetAddress()); //todo logs
        try {
            out.write(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
