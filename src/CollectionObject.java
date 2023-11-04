import java.io.Serializable;
import java.util.ArrayList;

public class CollectionObject implements Serializable {
    ArrayList<SimpleObject> list;

    // no arg constructor
    public CollectionObject() {}

    public CollectionObject(ArrayList<SimpleObject> objList) {
        this.list = objList;
    }
}