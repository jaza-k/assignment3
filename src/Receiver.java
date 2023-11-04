import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Receiver {
    private static Deserializer deserializer = null;

    public static void main(String[] args) throws JDOMException {
        int port = 54321; // Specify the port you want to use for communication
        deserializer = new Deserializer();

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

    private static void processDeserializedObject(Object deserializedObject) {
        System.out.println("Deserialized object: " + deserializedObject.getClass().getName());
    }
}
