import java.util.ArrayList;
import java.util.Scanner;
import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Sender {
    private static Serializer serializer = null;

    public static void main(String[] args) throws IOException {
        String hostname = "localhost";
        int port = 12345;

        serializer = new Serializer();
        Object objectToSerialize = null;

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
                objectToSerialize = simpleObject;
                break;
            case 2:
                ReferencesObject referencesObject = createReferencesObject(scanner);
                objectToSerialize = referencesObject;
                break;
            case 3:
                PrimitivesArray primitiveArray = createPrimitivesArray(scanner);
                objectToSerialize = primitiveArray;
                break;
            case 4:
                ReferencesArray referencesArray = createReferencesArray(scanner);
                objectToSerialize = referencesArray;
                break;
            case 5:
                CollectionObject collectionObject = createCollectionObject(scanner);
                objectToSerialize = collectionObject;
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }

        scanner.close();

        serializeObject(hostname, port, objectToSerialize);
    }

    // SERIALIZE
    private static void serializeObject(String hostname, int port, Object obj) throws IOException {
        System.out.println("-----------------------------------------------------");
        System.out.println("Serializing object...");

        Document document = serializer.serialize(obj);

        System.out.println("Now going to send XML document...");
        sendDoc(hostname, port, document);
    }

    // SENDING DOC
    // Some code is taken from tutorial Week 7 - Spooky Session 2 (MyClient.java)
    private static void sendDoc(String hostname, int port, Document document) {
        try {
            System.out.println("Connecting to " + hostname + " on port: " + port);

            Socket socket = new Socket(hostname, port);
            System.out.println("Connected to " + socket.getRemoteSocketAddress());

            // reate output stream for communication using the BufferedOutputStream
            OutputStream outputStream = socket.getOutputStream();
            BufferedOutputStream bufferedStream = new BufferedOutputStream(outputStream);

            XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
            String xmlString = xmlOutputter.outputString(document);

            // stream to output to the server
            ByteArrayOutputStream byteoutStream = new ByteArrayOutputStream();

            // set the XMLOutputter object to send output through our stream
            xmlOutputter.output(document, byteoutStream);

            // all bytes should be fed into a byteList so it can be written
            byte[] byteList = byteoutStream.toByteArray();

            // Send JDOM to server
            bufferedStream.write(byteList);

            bufferedStream.flush();
            bufferedStream.close();
            byteoutStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        for (int i = 0; i < collectionSize; i++) {
            SimpleObject obj = createSimpleObject(collectionScan);
            objList.add(obj);
        }

        CollectionObject objectsCollectionObject = new CollectionObject(objList);

        return objectsCollectionObject;
    }

    // REFERENCES ARRAY
    private static ReferencesArray createReferencesArray(Scanner scanner) {
        // ReferencesArray referencesArray = new ReferencesArray();

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
        System.out.println("Your array values: ");
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

        ReferencesObject referencesObject = new ReferencesObject(simpleObject);

        System.out.println("Created Object B with reference to Object A");
        return referencesObject;
    }

    // SIMPLE OBJECT
    private static SimpleObject createSimpleObject(Scanner scanner) {
        SimpleObject simpleObject = new SimpleObject();

        System.out.println("Creating a simple object of 2 ints");
        System.out.println("Enter integer value: ");
        simpleObject.setIntValue(scanner.nextInt());

        System.out.println("Enter double value: ");
        simpleObject.setDoubleValue(scanner.nextDouble());

        System.out.println("Created Object A");
        return simpleObject;
    }
}