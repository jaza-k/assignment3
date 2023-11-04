import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import java.io.StringReader;
import java.util.Map;
import java.util.IdentityHashMap;
import java.util.List;

public class Deserializer {
    private Map<Integer, Object> idToObjectMap = new IdentityHashMap<>();

    public Object deserialize(Document document) {
        Element root = document.getRootElement();
        deserializeObjects(root);
        return idToObjectMap.get(0);
    }

    private void deserializeObjects(Element element) {
        List<Element> objectElements = element.getChildren("object");
        for (Element objectElement : objectElements) {
            String className = objectElement.getAttributeValue("class");
            int objectId = Integer.parseInt(objectElement.getAttributeValue("id"));

            // create instance of the class using reflection
            Object object = createObjectInstance(className);

            idToObjectMap.put(objectId, object);

            deserializeFields(object, objectElement);

            // recursively deserialize nested objects
            deserializeObjects(objectElement);
        }
    }

    private Object createObjectInstance(String className) {
        try {
            Class objectClass = Class.forName(className);
            return objectClass;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void deserializeFields(Object object, Element objectElement) {
        System.out.println("Deserializing fields for object: " + object.getClass().getName());
    }
}
