import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Sender {
    private static Serializer serializer = new Serializer();

    public static void main(String[] args) throws IOException {
        String hostname = "localhost";
        int port = 12345;

        // create object to serialize first
        Object objectToSerialize = getObjectToSerialize();

        // serialize the object
        serializeObject(hostname, port, objectToSerialize);
    }

    private static Object getObjectToSerialize() {
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

        Object objectToSerialize = null;

        switch (choice) {
            case 1:
                objectToSerialize = ObjectCreator.createSimpleObject(scanner);
                break;
            case 2:
                objectToSerialize = ObjectCreator.createReferencesObject(scanner);
                break;
            case 3:
                objectToSerialize = ObjectCreator.createPrimitivesArray(scanner);
                break;
            case 4:
                objectToSerialize = ObjectCreator.createReferencesArray(scanner);
                break;
            case 5:
                objectToSerialize = ObjectCreator.createCollectionObject(scanner);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }

        scanner.close();
        return objectToSerialize;
    }

    // SERIALIZE
    private static void serializeObject(String hostname, int port, Object obj) throws IOException {
        System.out.println("-----------------------------------------------------");
        System.out.println("Serializing object...");

        Document document = serializer.serialize(obj);

        System.out.println("Creating document to send...");
        File fileToSend = XMLtoFile(document);

        System.out.println("Now going to send XML document...");
        sendDoc(hostname, port, document);
    }

    // XML TO FILE
    private static File XMLtoFile(Document document) throws IOException {
        XMLOutputter xmlOutput = new XMLOutputter();
        xmlOutput.setFormat(Format.getPrettyFormat());

        File sendFile = new File("fileToSend.xml");
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(sendFile));

        xmlOutput.output(document, bufferedWriter);
        bufferedWriter.close();

        return sendFile;
    }

    // SENDING DOC
    // Some code is taken from tutorial Week 7 - Spooky Session 2 (MyClient.java)
    private static void sendDoc(String hostname, int port, Document document) {
        try {
            System.out.println("Connecting to " + hostname + " on port: " + port);

            Socket socket = new Socket(hostname, port);
            System.out.println("Connected to " + socket.getRemoteSocketAddress());

            // create output stream for communication using the BufferedOutputStream
            OutputStream outputStream = socket.getOutputStream();
            BufferedOutputStream bufferedStream = new BufferedOutputStream(outputStream);

            XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
            //String xmlString = xmlOutputter.outputString(document);

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
}