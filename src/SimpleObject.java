import java.io.Serializable;

public class SimpleObject implements Serializable {
    private int intValue;
    private double doubleValue;
    
    // no argument constructor
    public SimpleObject() {
    }

    public int getIntValue() {
        return intValue;
    }

    public double getDoubleValue() {
        return doubleValue;
    }

    public void setIntValue(int nextInt) {
        this.intValue = nextInt;
    }

    public void setDoubleValue(double nextDouble) {
        this.doubleValue = nextDouble;
    }
}