import java.util.ArrayList;
import java.util.Scanner;

public class ObjectCreator {
    public static SimpleObject createSimpleObject(Scanner scanner) {
        SimpleObject simpleObject = new SimpleObject();

        System.out.println("Creating a simple object of 2 ints");
        System.out.println("Enter integer value: ");
        simpleObject.setIntValue(scanner.nextInt());

        System.out.println("Enter double value: ");
        simpleObject.setDoubleValue(scanner.nextDouble());

        System.out.println("Created Object A");
        return simpleObject;
    }

    public static ReferencesObject createReferencesObject(Scanner scanner) {
        SimpleObject simpleObject = new SimpleObject();

        System.out.println("Creating a simple Object A");
        System.out.println("Enter integer value: ");
        simpleObject.setIntValue(scanner.nextInt());

        System.out.println("Enter double value: ");
        simpleObject.setDoubleValue(scanner.nextDouble());

        ReferencesObject referencesObject = new ReferencesObject(simpleObject);

        System.out.println("Created Object B with reference to Object A");
        return referencesObject;
    }

    public static PrimitivesArray createPrimitivesArray(Scanner scanner) {
        PrimitivesArray primitiveArray = new PrimitivesArray();

        System.out.println("Creating an array of ints");
        System.out.println("Enter size of array: ");
        int size = scanner.nextInt();
        primitiveArray.intArray = new int[size];

        for (int i = 0; i < size; i++) {
            System.out.println("Enter value for index " + i + ": ");
            primitiveArray.intArray[i] = scanner.nextInt();
        }

        // print out array
        System.out.println("Your array values: ");
        for (int i = 0; i < size; i++) {
            System.out.println(primitiveArray.intArray[i]);
        }

        System.out.println("Created array of ints");
        return primitiveArray;
    }

    public static ReferencesArray createReferencesArray(Scanner scanner) {
        System.out.println("Creating an array of Objects");
        System.out.println("Enter size of array: ");
        int size = scanner.nextInt();

        SimpleObject[] refObjArray = new SimpleObject[size];

        for (int i = 0; i < size; i++) {
            System.out.printf("At index %d:\n", i);
            SimpleObject simpleObj = createSimpleObject(scanner);
            refObjArray[i] = simpleObj;
        }

        ReferencesArray referencesArray = new ReferencesArray(refObjArray);

        System.out.println("Created array of references");

        // print out array
        System.out.println("Your array values: ");
        for (int i = 0; i < size; i++) {
            System.out.println(refObjArray[i]);
        }

        return referencesArray;
    }

    public static CollectionObject createCollectionObject(Scanner scanner) {
        ArrayList<SimpleObject> objList = new ArrayList<SimpleObject>();
        Scanner collectionScan = new Scanner(System.in);
        String input;

        int collectionSize;

        System.out.println("How many objects do you want in the collection?");
        input = collectionScan.nextLine();

        collectionSize = Integer.parseInt(input);

        for (int i = 0; i < collectionSize; i++) {
            SimpleObject obj = createSimpleObject(collectionScan);
            objList.add(obj);
        }

        CollectionObject objectsCollectionObject = new CollectionObject(objList);

        return objectsCollectionObject;
    }
}