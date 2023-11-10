import org.jdom2.Document;
import org.jdom2.Element;

import java.util.Map;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;

public class Deserializer {
    private Map<String, Object> idToObjectMap = new HashMap<>();

    public Object deserialize(Document document) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Element root = document.getRootElement();
        deserializeObjects(root);

        return idToObjectMap.get("0");
    }

    private void deserializeObjects(Element element) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        List<Element> objectElements = element.getChildren("object");

        for (Element objectElement : objectElements) {
            Object obj = null;
            
            String className = objectElement.getAttributeValue("class");
            try {
                Class objectClass = Class.forName(className);
                if (objectClass.isArray()) {
                    obj = Array.newInstance(objectClass.getComponentType(), Integer.valueOf(objectElement.getAttributeValue("length")));
                }

                else {
                    Constructor con = objectClass.getDeclaredConstructor(null);
                    if (!Modifier.isPublic(con.getModifiers())) {
                        con.setAccessible(true);
                    }
                    obj = con.newInstance(null);
                    
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            idToObjectMap.put(objectElement.getAttributeValue("id"), obj);
     
            // check if reference
            if (objectElement.getChild("field").getChild("reference") != null) {
                Object refObj = null;
                String refClassname = objectElement.getChild("field").getChild("reference").getChild("object").getAttributeValue("class");
                Class refClass = Class.forName(refClassname);
                Constructor refCon = refClass.getDeclaredConstructor(null);
                if (!Modifier.isPublic(refCon.getModifiers())) {
                    refCon.setAccessible(true);
                }
                refObj = refCon.newInstance(null);
                idToObjectMap.put(objectElement.getChild("field").getChild("reference").getText(), refObj);
            }
        }

        for (Element objectElement : objectElements) {
            Object object = idToObjectMap.get(objectElement.getAttributeValue("id"));
            Class objectClass = object.getClass();
            List<Element> children = objectElement.getChildren();

            for (Element child : children) {
                  // check if reference
            if (child.getChild("reference") != null) {
                Object refObj = null;
                String refClassname = child.getChild("reference").getChild("object").getAttributeValue("class");
                Class refClass = Class.forName(refClassname);
                Constructor refCon = refClass.getDeclaredConstructor(null);
                if (!Modifier.isPublic(refCon.getModifiers())) {
                    refCon.setAccessible(true);
                }
                refObj = refCon.newInstance(null);
                idToObjectMap.put(child.getChild("reference").getText(), refObj);
            }

                String fieldName = child.getAttributeValue("name");
                try {
                    Field field = objectClass.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    
                    // check if field is an array
                    if (field.getType().isArray()) {
                        for (int i = 0; i < Array.getLength(field.get(object)); i++) {
                            Element arrayChild = child.getChildren().get(i);
                            Array.set(field.get(object), i, getElementVal(arrayChild, object, field.getType().getComponentType(), idToObjectMap));
                            // print out array
                            System.out.println("Array contents: " + Array.get(field.get(object), i));
                        }
                        return;
                    }

                    field.set(object, getElementVal(child, object, field.getType(), idToObjectMap));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Object deserializeField(Class fieldType, String fieldValue) {
        if (fieldType.equals(int.class)) {
            return Integer.valueOf(fieldValue);
        } else if (fieldType.equals(short.class)) {
            return Short.valueOf(fieldValue);
        } else if (fieldType.equals(long.class)) {
            return Long.valueOf(fieldValue);
        } else if (fieldType.equals(float.class)) {
            return Float.valueOf(fieldValue);
        } else if (fieldType.equals(double.class)) {
            return Double.valueOf(fieldValue);
        } else if (fieldType.equals(boolean.class)) {
            return Boolean.valueOf(fieldValue);
        } else if (fieldType.equals(char.class)) {
            return fieldValue.charAt(0);
        }
        return null;
    }

    private Object getElementVal(Element element, Object object, Class fieldType, Map<String, Object> idToObjectMap) {
        // check if reference
        if (element.getChild("reference") != null) {
            return idToObjectMap.get(element.getChild("reference").getText());
        }

        String value = element.getChild("value").getText();

        // get child of element
        Element child = element.getChild("value");

        if (child.getName().equals("value")) {
            return deserializeField(fieldType, value);
        }
        else {
            return idToObjectMap.get(value);
        }
    }
}
