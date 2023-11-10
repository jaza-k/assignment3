import java.io.Serializable;

public class ReferencesObject implements Serializable {
    public SimpleObject fieldObject;

    // no argument constructor
    public ReferencesObject() {}

    public ReferencesObject(SimpleObject simpleObject) {
        this.fieldObject = simpleObject;
    }

    public void setFieldObject(SimpleObject simpleObject) {
        this.fieldObject = simpleObject;
    }

    public SimpleObject getFieldObject() {
        return this.fieldObject;
    }
}