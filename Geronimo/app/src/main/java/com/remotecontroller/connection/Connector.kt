package com.remotecontroller.connection

import com.remotecontroller.ui.MainActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.net.Socket
import kotlin.concurrent.thread

public object Connector : Thread() {
    private var socket : Socket? = null //сокет соединения
    private var input : DataInputStream? = null //потоки входных и выходных данных
    private var out: DataOutputStream? = null

    private var ip = "192.168.0.104"
    private var port = 5432

    private var connected = false;

    fun setIp(ip: String) {
        this.ip = ip
    }

    fun setPort(port: Int) {
        this.port = port
    }

    fun getIp(): String {
        return ip
    }

    fun getPort(): Int {
        return port
    }

    fun reconnect() {
        thread {
            try {
                socket?.close()
                socket = Socket(ip, port)
                val socketIn = socket!!.getInputStream()
                val socketOut = socket!!.getOutputStream()
                input = DataInputStream(socketIn)
                out = DataOutputStream(socketOut)
                connected = true
                MainActivity.setConnected(true)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * стартуем поток
     * происходит чтение ответа
     */
    override fun run() {
        try {
            while (!interrupted()) {
                MainActivity.log("Ждём новое сообшение")
                val message = ByteArray(10)
                for (i in message.indices) message[i] = input!!.readByte()
                MainActivity.log("Приняли пакет")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * отправляет пакет с сообщением на сервер
     * @param message массив байт посылки
     */
    fun sendMessage(message: ByteArray) {
        val temp = Thread {
            if (!connected) reconnect()
            if (socket == null) {
                MainActivity.log("Нет соединения")
                return@Thread
            }
            MainActivity.log("Отправляем пакет" + " размера " + message.size + " на " + socket!!.inetAddress)
            try {
                out!!.write(message)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        temp.start()
    }
}