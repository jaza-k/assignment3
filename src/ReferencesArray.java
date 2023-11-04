import java.io.Serializable;

public class ReferencesArray implements Serializable {
    public SimpleObject[] simpleObjectArray;

    // no argument constructor
    public ReferencesArray() {};

    public ReferencesArray(SimpleObject[] refObjArray) {
        this.simpleObjectArray = refObjArray;
    }

    public SimpleObject[] getSimpleObjectArray() {
        return simpleObjectArray;
    }
}