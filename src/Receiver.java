import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;

public class Receiver {
    private static Deserializer deserializer = null;

    public static void main(String[] args) throws JDOMException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        int port = 12345; 
        deserializer = new Deserializer();

        // Some code is taken from tutorial Week 7 - Spooky Session 2 (MyServer.java)
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            Socket socket = serverSocket.accept();
            System.out.println("Client connected.");

            // Create input stream for communication with clientsocket
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			// Rebuild the XML using saxBuilder
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(in);

            // Load the JDOM into a nicer format and now implement your saxBuilder
            XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
            String xmlString = xmlOutputter.outputString(document);
            System.out.println(xmlString);

            // Deserialize the object
            Object deserializedObject = deserializer.deserialize(document);

            // Display the deserialized object 
            processDeserializedObject(deserializedObject);

            System.out.println("Received and processed the object");
            
			socket.close();
            serverSocket.close();
            in.close();
            System.out.println("Closed connection.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processDeserializedObject(Object deserializedObject) throws IllegalArgumentException, IllegalAccessException {
        // invoke Inspector
        Inspector inspector = new Inspector();
        inspector.inspect(deserializedObject, false);

        System.out.println("\nDeserialized object: " + deserializedObject.getClass().getName());
        System.out.println("Object hashcode: " + System.identityHashCode(deserializedObject));
        System.out.println("Object fields: " + deserializedObject.getClass().getDeclaredFields().length);
        System.out.println("Object methods: " + deserializedObject.getClass().getDeclaredMethods().length);
    }
}
