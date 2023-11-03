import java.util.ArrayList;
import java.util.Scanner;
import java.io.Serializable;

public class ObjectCreator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
            System.out.println("Choose an object type to create (enter number):");
            System.out.println("-----------------------------------------------------");
            System.out.println("1. Simple Object - contains only primitives for instance variables");
            System.out.println("2. References Object - contains references to other objects");
            System.out.println("3. Primitive Array Object - contains an array of primitives");
            System.out.println("4. References Array Object - contains an array of object references");
            System.out.println("5. Java Collection Object - uses one of Java’s collection classes to refer to other objects\n");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    SimpleObject simpleObject = createSimpleObject(scanner);
                    
                    break;
                case 2:
                    ReferencesObject referencesObject = createReferencesObject(scanner);

                    break;
                case 3:
                    PrimitivesArray primitiveArray = createPrimitivesArray(scanner);
                    
                    break;
                case 4:
                    ReferencesArray referencesArray = createReferencesArray(scanner);
                    
                    break;
                case 5:
                    CollectionObject collectionObject = createCollectionObject(scanner);
                    
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

        scanner.close();

       // serializeObjects(host, port);
    }

    // COLLECTION OBJECT 
    private static CollectionObject createCollectionObject(Scanner scanner) {
        ArrayList<SimpleObject> objList = new ArrayList<SimpleObject>();
        Scanner collectionScan = new Scanner(System.in);
        String input;
  
        int collectionSize;
  
        System.out.println("How many objects do you want in the collection?");
        input = collectionScan.nextLine();
  
        collectionSize = Integer.parseInt(input);
  
        for(int i = 0; i < collectionSize; i++) {
            SimpleObject obj = createSimpleObject(collectionScan);
            objList.add(obj);
        }
  
        CollectionObject objectsCollectionObject = new CollectionObject(objList);
  
        return objectsCollectionObject;
    }
     
    // REFERENCES ARRAY
    private static ReferencesArray createReferencesArray(Scanner scanner) {
        ReferencesArray referencesArray = new ReferencesArray();

        System.out.println("Creating an array of Objects");
        System.out.println("Enter size of array: ");
        int size = scanner.nextInt();
        referencesArray.simpleObjectArray = new SimpleObject[size];
        System.out.println("Created array of references");

        return referencesArray;
    }

    // PRIMITIVE ARRAY
    private static PrimitivesArray createPrimitivesArray(Scanner scanner) {
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
        System.out.println("Array values: ");
        for (int i = 0; i < size; i++) {
            System.out.println(primitiveArray.intArray[i]);
        }

        System.out.println("Created array of ints");
        return primitiveArray;
    }

    // REFERENCES OBJECT
    private static ReferencesObject createReferencesObject(Scanner scanner) {
        SimpleObject simpleObject = new SimpleObject();

        System.out.println("Creating a simple Object A");
        System.out.println("Enter integer value: ");
        simpleObject.setIntValue(scanner.nextInt());

        System.out.println("Enter double value: ");
        simpleObject.setDoubleValue(scanner.nextDouble());

        scanner.nextLine(); 
        System.out.println("Enter string value: ");
        simpleObject.setStringValue(scanner.nextLine());

        ReferencesObject referencesObject = new ReferencesObject(simpleObject);
        System.out.println("Created Object B with reference to Object A");

        return referencesObject;
    }

    // SIMPLE OBJECT
    private static SimpleObject createSimpleObject(Scanner scanner) {
        SimpleObject simpleObject = new SimpleObject();

        System.out.println("Creating a simple Object A");
        System.out.println("Enter integer value: ");
        simpleObject.setIntValue(scanner.nextInt());

        System.out.println("Enter double value: ");
        simpleObject.setDoubleValue(scanner.nextDouble());

        scanner.nextLine(); 
        System.out.println("Enter string value: ");
        simpleObject.setStringValue(scanner.nextLine());
        
        System.out.println("Created Object A");

        return simpleObject;
    }
}
