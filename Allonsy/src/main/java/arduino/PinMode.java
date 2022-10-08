package arduino;

public enum PinMode {
    INPUT,OUTPUT,INPUT_PULLUP;

    @Override
    public String toString() {
        switch (this){
            case INPUT:return "1";
            case OUTPUT:return "2";
            case INPUT_PULLUP:return "3";
            default:return "0";
        }
    }
}
