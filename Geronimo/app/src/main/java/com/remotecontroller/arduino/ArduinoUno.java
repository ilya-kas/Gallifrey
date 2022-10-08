package com.remotecontroller.arduino;

import com.remotecontroller.connection.Connector;

import java.util.Arrays;

public class ArduinoUno extends Arduino{
    public ArduinoUno() {
        dPins = new PinMode[19];
        Arrays.fill(dPins, PinMode.NONE);
        aPins = new PinMode[6];
        Arrays.fill(aPins, PinMode.NONE);
    }

    @Override
    public boolean pinmode(PinType type, int nom, PinMode mode) {
        if (type==PinType.A)
            if (aPins[nom]==mode)
                return true;
        if (type==PinType.D)
            if (dPins[nom]==mode)
                return true;
        Connector.INSTANCE.sendMessage(new byte[]{1,(byte)(type.toInt()), (byte)(nom), (byte) mode.toInt()});
        return true;
    }

    @Override
    public boolean setPin(PinType type, int nom, int v) {
        if (type==PinType.A)
            if (aPins[nom] != PinMode.OUTPUT)
                return false;
        if (type==PinType.D)
            if (dPins[nom] != PinMode.OUTPUT)
                return false;
        Connector.INSTANCE.sendMessage(new byte[]{3,(byte)(type.toInt()), (byte)(nom), (byte) v});
        return true;
    }

    @Override
    public int readPin(PinType type, int nom) {
        if (type==PinType.A)
            if (aPins[nom] != PinMode.INPUT && aPins[nom] != PinMode.INPUT_PULLUP)
                return -1;
        if (type==PinType.D)
            if (dPins[nom] != PinMode.INPUT && dPins[nom] != PinMode.INPUT_PULLUP)
                return -1;
        Connector.INSTANCE.sendMessage(new byte[]{2,(byte)(type.toInt()), (byte)(nom), (byte) 0});
        return 0;
    }
}
