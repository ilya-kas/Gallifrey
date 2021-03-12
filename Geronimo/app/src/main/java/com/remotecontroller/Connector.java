package com.remotecontroller;

import android.app.Activity;
import android.widget.EditText;

import java.io.*;
import java.net.Socket;

public class Connector extends Thread {
    private Socket socket;         //сокет соединения
    private DataInputStream in;    //потоки входных и выходных данных
    private DataOutputStream out;
    private final EditText et_ip;
    private final EditText et_port;

    private String ip;
    private int port;

    public Connector(Activity finder) {
        et_ip = finder.findViewById(R.id.et_ip);
        et_port = finder.findViewById(R.id.et_port);
        reconnect();
    }

    private void reconnect(){
        Thread temp = new Thread(() -> {
            try {
                if (et_ip.getText().toString().equals(""))
                    return;
                ip = et_ip.getText().toString();
                port = Integer.parseInt(et_port.getText().toString());
                socket = new Socket(ip,port);

                InputStream socketIn = socket.getInputStream();
                OutputStream socketOut = socket.getOutputStream();

                in = new DataInputStream(socketIn);
                out = new DataOutputStream(socketOut);
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        temp.start();
    }

    /**
     * стартуем поток
     * происходит чтение ответа
     */
    @Override
    public void run(){
        try {
            while (!Thread.interrupted()) {
                System.out.println("Ждём новое сообшение"); //todo logs
                byte[] message = new byte[10]; //todo столько байт, сколько в протоколе. для безопастности от взлома
                for (int i=0;i<message.length;i++)
                    message[i] = in.readByte();
                System.out.println("Приняли пакет");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * отправляет пакет с сообщением на сервер
     * @param message массив байт посылки
     */
    public void sendMessage(byte[] message){
        Thread temp = new Thread(() -> {
            if (socket == null)
                reconnect();
            if (socket == null) {
                System.out.println("Нет соединения");
                return;
            }
            System.out.println("Отправляем пакет" + " размера " + message.length + " на " + socket.getInetAddress()); //todo logs
            try {
                out.write(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        temp.start();
    }
}
