import org.jdom2.Document;
import org.jdom2.Element;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.IdentityHashMap;
import java.util.Map;

public class Serializer {
    private int objectIdCounter = 0;
    private Map<Object, Integer> objectToIdMap = new IdentityHashMap<>();
    
    public Document serialize(Object obj) {
        Document document = new Document(new Element("serialized"));
        serializeObject(obj, document.getRootElement());

        return document;
    }

    private void serializeObject(Object obj, Element parentElement) {
        if (obj == null) {
            System.out.println("Object is null");
            return;
        }

        System.out.println("Object being serialized: " + obj.getClass().getName());

        // check ifobject has been serialized before
        if (objectToIdMap.containsKey(obj)) {
            // add reference element
            Element referenceElement = new Element("reference");
            referenceElement.setText(objectToIdMap.get(obj).toString());
            parentElement.addContent(referenceElement);
            return;
        }
        Class objClass = obj.getClass();

        // create a new object element
        Element objectElement = new Element("object");
        objectElement.setAttribute("class", obj.getClass().getName());
        objectElement.setAttribute("id", String.valueOf(objectIdCounter));
        objectToIdMap.put(obj, objectIdCounter);

        objectIdCounter++;
        
        if (objClass.isPrimitive()) {
            objectElement.addContent(serializePrimitive(obj));
        } 
        else if (objClass.isArray()) {
            objectElement.setAttribute("length", Integer.toString(Array.getLength(obj)));
            for (Element e : serializeArray(obj, objectToIdMap)) {
                objectElement.addContent(e);
            }
        } 
        else {
            for (Field field : objClass.getDeclaredFields()) {
                try {
                   field.setAccessible(true); 
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                // create field element
                Element fieldElement = new Element("field");
                fieldElement.setAttribute("name", field.getName());
                fieldElement.setAttribute("declaringclass", objClass.getName());

                try {
                    Object fieldValue = field.get(obj);
                    Class fieldType = field.getType();
                    Element fieldContents = null;
                    if (fieldValue != null) {
                        if (fieldType.isPrimitive()) {
                            fieldContents = serializePrimitive(fieldValue);
                        }
                        else {
                            fieldContents = serializeReference(fieldValue, fieldType, objectToIdMap);
                        }

                        Element valueElement = new Element("value");
                        valueElement.setText(fieldValue.toString());
                        fieldElement.addContent(valueElement);
                    } else {
                        fieldElement.addContent(new Element("null"));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                objectElement.addContent(fieldElement);
            }
            parentElement.addContent(objectElement);
        }
    }

    private Element serializeReference(Object fieldValue, Class<?> fieldType, Map<Object, Integer> objectToIdMap) {
        System.out.println("Serializing a reference");
        if (objectToIdMap.containsKey(fieldValue)) {
            // the referred object has been serialized before, add reference element
            Element referenceElement = new Element("reference");
            referenceElement.setText(objectToIdMap.get(fieldValue).toString());
            return referenceElement;
        } else {
            // the referred object hasn't been serialized yet, serialize and add new object element
            Element referredObjectElement = new Element("object");
            referredObjectElement.setAttribute("class", fieldType.getName());
            referredObjectElement.setAttribute("id", String.valueOf(objectIdCounter));
    
            serializeObject(fieldValue, referredObjectElement);
    
            return referredObjectElement;
        }
    }

    private Element[] serializeArray(Object obj, Map<Object, Integer> objectToIdMap) {
        System.out.println("Serializing an array");
        Class<?> componentType = obj.getClass().getComponentType();
        int length = Array.getLength(obj);

        Element arrayElement = new Element("object");
        arrayElement.setAttribute("class", obj.getClass().getName());
        arrayElement.setAttribute("id", String.valueOf(objectIdCounter++));
        arrayElement.setAttribute("length", String.valueOf(length));
        objectToIdMap.put(obj, objectIdCounter);

        for (int i = 0; i < length; i++) {
            Element valueElement = new Element("value");
            Object element = Array.get(obj, i);

            if (element != null) {
                if (componentType.isPrimitive()) {
                    valueElement.addContent(serializePrimitive(element));
                } else {
                    valueElement.addContent(serializeReference(element, componentType, objectToIdMap));
                }
            } else {
                valueElement.addContent(new Element("null"));
            }

            arrayElement.addContent(valueElement);
        }

        return new Element[]{arrayElement};
    }

    private Element serializePrimitive(Object obj) {
        System.out.println("Serializing a primitive");
        Element objValue = new Element("value");
        objValue.setText(obj.toString());
        return objValue;
    }
}