package com.remotecontroller.arduino;

public abstract class Arduino {
    PinMode[] dPins;
    PinMode[] aPins;

    public abstract boolean pinmode(PinType type, int nom, PinMode mode);
    public abstract boolean setPin(PinType type, int nom, int v);
    public abstract int readPin(PinType type, int nom);
}
