package com.remotecontroller.arduino;

public enum PinType {
    A,D;
    int toInt(){
        if (this == A)
            return 0;
        if (this == D)
            return 1;
        return -1;
    }
}
