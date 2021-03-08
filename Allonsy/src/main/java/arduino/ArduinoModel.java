package arduino;

public class ArduinoModel extends Thread{
    private Arduino arduino;
    private ArduinoController master;

    public ArduinoModel(ArduinoController main) {
        master = main;

        arduino = new Arduino("COM4",9600);
        boolean connected = arduino.openConnection(); //подключение к порту arduino
        if (connected)
            System.out.println("Подключились"); //todo logs
        else
            System.out.println("Сбой подключения");
    }

    /**
     * подать v вольт на пин type nom
     * @param type тип пина
     * @param nom номер пина
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
            if (nom<0 || nom>19)     //корректна для arduino uno
                return false;
        }

        arduino.serialWrite("SP/"+type.toString()+"/"+nom+"/"+v);
        return true;
    }

    public boolean getFromPin(PinType type, int nom){
        if (type==PinType.A)       //проверка ввода
            if (nom<0 || nom>5)     //корректна для arduino uno
                return false;
        if (type==PinType.D)
            if (nom<0 || nom>19)     //корректна для arduino uno
                return false;

        arduino.serialWrite("GP/"+type.toString()+"/"+nom);
        return true;
    }


    //чтение вывода со стороны arduino
    @Override
    public void run() {
        super.run();
        while (!Thread.interrupted()){
            try {
                String sygnal = arduino.serialRead();     //чтение из порта
                sygnal = sygnal.substring(0,sygnal.length()-1);
                master.analyzeResponse(sygnal);
                //Thread.sleep(10);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
