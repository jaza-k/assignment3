import org.jdom2.Document;
import org.jdom2.Element;

import java.util.Map;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.IdentityHashMap;
import java.util.List;

public class Deserializer {
    private Map<Integer, Object> idToObjectMap = new IdentityHashMap<>();

    public Object deserialize(Document document) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Element root = document.getRootElement();

        deserializeObjects(root);
        return idToObjectMap.get(0);
    }

    private void deserializeObjects(Element element) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        List<Element> objectElements = element.getChildren();
        for (Element objectElement : objectElements) {
            String className = objectElement.getAttributeValue("class");
            int objectId = Integer.parseInt(objectElement.getAttributeValue("id"));

            // create instance of the class using reflection
            Class objectClass =  Class.forName(objectElement.getAttributeValue("class"));
            Object instance = null;

             if(!objectClass.isArray()) {
                Constructor c = objectClass.getDeclaredConstructor(null);
                if (!Modifier.isPublic(c.getModifiers())) {
                    c.setAccessible(true);
                }
                instance = c.newInstance(null);
            }

            else {
                instance = Array.newInstance(objectClass.getComponentType(), Integer.parseInt(objectElement.getAttributeValue("length")));
            }

            idToObjectMap.put(objectId, instance);

            deserializeFields(instance, objectElement);

            // recursively deserialize nested objects
            deserializeObjects(objectElement);
        }
    }

    private void deserializeFields(Object instance, Element objectElement) {
        List<Element> fieldElements = objectElement.getChildren();
        for (Element fieldElement : fieldElements) {
            String fieldName = fieldElement.getAttributeValue("name");
            String fieldValue = fieldElement.getAttributeValue("value");
            String fieldReference = fieldElement.getAttributeValue("reference");
    
            try {
                Field field = instance.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                String fieldType = field.getType().getName();
    
                if (fieldType.equals("primitive")) { // Ensure fieldType is not null
                    field.set(instance, deserializePrimitive(fieldType, fieldValue));
                } else if (fieldType.equals("reference")) { // Ensure fieldType is not null
                    field.set(instance, idToObjectMap.get(Integer.parseInt(fieldReference)));
                } else if (fieldType.equals("array")) { // Ensure fieldType is not null
                    Object array = Array.newInstance(Class.forName(fieldElement.getAttributeValue("componentType")), Integer.parseInt(fieldElement.getAttributeValue("length")));
                    field.set(instance, array);
                    deserializeArray(array, fieldElement);
                }
            } catch (NoSuchFieldException | IllegalAccessException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    

    private void deserializeArray(Object array, Element fieldElement) throws IllegalAccessException {
        List<Element> arrayElements = fieldElement.getChildren();
        for (Element arrayElement : arrayElements) {
            String arrayIndex = arrayElement.getAttributeValue("index");
            //String arrayValue = arrayElement.getAttributeValue("value");
            String arrayReference = arrayElement.getAttributeValue("reference");

            Array.set(array, Integer.parseInt(arrayIndex), idToObjectMap.get(Integer.parseInt(arrayReference)));
        }
    }

    private Object deserializePrimitive(String fieldType, String fieldValue) {
        if (fieldType.equals("int")) {
            return Integer.parseInt(fieldValue);
        } else if (fieldType.equals("short")) {
            return Short.parseShort(fieldValue);
        } else if (fieldType.equals("long")) {
            return Long.parseLong(fieldValue);
        } else if (fieldType.equals("float")) {
            return Float.parseFloat(fieldValue);
        } else if (fieldType.equals("double")) {
            return Double.parseDouble(fieldValue);
        } else if (fieldType.equals("boolean")) {
            return Boolean.parseBoolean(fieldValue);
        } else if (fieldType.equals("char")) {
            return fieldValue.charAt(0);
        }
        return null;
    }
}