public class ReferencesObject {
    public SimpleObject fieldObject;

    // no argument constructor
    public ReferencesObject() {}

    public ReferencesObject(SimpleObject simpleObject) {
        this.fieldObject = simpleObject;
    }
}