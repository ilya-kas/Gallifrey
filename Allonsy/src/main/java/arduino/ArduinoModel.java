package arduino;

import com.fazecast.jSerialComm.SerialPort;
import server.Server;

import java.io.InputStream;

public class ArduinoModel{
    private Arduino arduino;
    private ArduinoController master;

    public ArduinoModel(ArduinoController main, String port) {
        master = main;

        arduino = new Arduino(port,9600);
        boolean connected = arduino.openConnection(); //подключение к порту arduino
        if (connected)
            Server.log("Подключились");
        else
            Server.log("Сбой подключения");
    }

    public void sudoStop(){
        arduino.closeConnection();
    }

    /**
     * подать v вольт на пин type nom
     * @param type тип пина
     * @param nom номер пина, от 3 потому что первые 2 пина для консоли
     * @param v напряжение
     * @return успешность
     */
    public boolean setPin(PinType type, int nom, double v){
        if (v<0 || v>5)
            return false;
        if (type==PinType.A)       //проверка ввода
            if (nom<0 || nom>5)     //корректна для arduino uno
                return false;
        if (type==PinType.D){
            if (v!=0)
                v = 5;
            if (nom<3 || nom>19)     //корректна для arduino uno
                return false;
        }

        arduino.serialWrite("3");
        arduino.serialWrite(type.toString());
        arduino.serialWrite((char)('0'+nom));
        arduino.serialWrite((char)('0'+v));
        arduino.serialWrite("|");
        return true;
    }

    public boolean getFromPin(PinType type, int nom){
        if (type==PinType.A)       //проверка ввода
            if (nom<0 || nom>5)     //корректна для arduino uno
                return false;
        if (type==PinType.D)
            if (nom<3 || nom>19)     //корректна для arduino uno
                return false;

        arduino.serialWrite("2");
        arduino.serialWrite(type.toString());
        arduino.serialWrite((char)('0'+nom));
        arduino.serialWrite("|");

        //master.analyzeResponse("get " + arduino.serialRead());
        //serialRead();
        //System.out.println();
        return true;
    }

    public boolean bindPin(PinType type, int nom, PinMode mode){
        if (type==PinType.A)       //проверка ввода
            if (nom<0 || nom>5)     //корректна для arduino uno
                return false;
        if (type==PinType.D)
            if (nom<3 || nom>19)     //корректна для arduino uno
                return false;

        arduino.serialWrite("1");
        arduino.serialWrite(type.toString());
        arduino.serialWrite((char)('0'+nom));
        arduino.serialWrite(mode.toString());
        arduino.serialWrite("|");
        return true;
    }

    private void serialRead(){
        SerialPort comPort = SerialPort.getCommPorts()[1];
        comPort.openPort();
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        InputStream in = comPort.getInputStream();
        try {
            for (int j = 0; j < 1000; ++j)
                System.out.print((char)in.read());
            in.close();
        } catch (Exception e) { e.printStackTrace(); }
        comPort.closePort();
    }
}
