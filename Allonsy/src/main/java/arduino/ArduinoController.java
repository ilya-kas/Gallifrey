package arduino;

public class ArduinoController {
    ArduinoModel model;
    public ArduinoController(String port) {
        model = new ArduinoModel(this, port);
    }

    public boolean analyzeMessage(byte[] message){
        if (message.length==0)
            return false;
        byte command = message[0];
        PinType type;
        switch (command){
            case 1: //установить pinmode
                if (message.length<4)
                    return false;
                PinMode mode;
                if (message[1]==0)
                    type = PinType.A;
                else
                    type = PinType.D;
                switch (message[3]){
                    case 0:mode = PinMode.INPUT;break;
                    case 1:mode = PinMode.OUTPUT;break;
                    case 2:mode = PinMode.INPUT_PULLUP;break;
                    default:return false;
                }
                model.bindPin(type,message[2],mode);
                return true;
            case 2: //прочитать значение с пина
                if (message.length<3)
                    return false;
                if (message[1]==0)
                    type = PinType.A;
                else
                    type = PinType.D;
                model.getFromPin(type,message[2]);
                return true;
            case 3: //установить выходное значение на пине
                if (message.length<4)
                    return false;
                if (message[1]==0)
                    type = PinType.A;
                else
                    type = PinType.D;
                model.setPin(type,message[2],message[3]);
                return true;
            default:return false;
        }
    }

    public boolean analyzeResponse(String message){
        System.out.println(message);
        return true;
    }
}
