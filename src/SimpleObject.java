// Define a simple class with only primitives for instance variables

import java.io.Serializable;

public class SimpleObject implements Serializable {
    private int intValue;
    private double doubleValue;
    private String stringValue;
    
    // no argument constructor
    public SimpleObject() {
    }

    public int getIntValue() {
        return intValue;
    }

    public double getDoubleValue() {
        return doubleValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setIntValue(int nextInt) {
        this.intValue = nextInt;
    }

    public void setDoubleValue(double nextDouble) {
        this.doubleValue = nextDouble;
    }

    public void setStringValue(String nextLine) {
        this.stringValue = nextLine;
    }
}