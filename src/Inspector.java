// JAZA KHAN (UCID 30119100)
// CPSC 501 F2023 - Assignment 2

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.HashSet;

public class Inspector {
	private HashSet<Integer> inspectedObjects;

	public Inspector() {
		this.inspectedObjects = new HashSet<>();
	}

	public void inspect(Object obj, boolean recursive) throws IllegalArgumentException, IllegalAccessException {
		inspectObject(obj, recursive);
	}

	private void inspectObject(Object obj, boolean recursive) throws IllegalArgumentException, IllegalAccessException {
		if (obj == null) {
			System.out.println("Null object");
			return;
		}

		int objectHashCode = System.identityHashCode(obj);
		if (inspectedObjects.contains(objectHashCode)) {
			System.out.println("Already inspected (hashCode " + objectHashCode + "): " + obj.getClass().getName());
			return;
		}

		inspectedObjects.add(objectHashCode);

		Class<?> objClass = obj.getClass();

		// CLASSES // 
		System.out.println("\nCLASS NAME:" + objClass.getName());
		System.out.println("Immediate superclass: " + objClass.getSuperclass().getName());
		System.out.print("Implemented interfaces: ");
		Class<?>[] interfaces = objClass.getInterfaces();
		for (Class<?> intf : interfaces) {
			System.out.print(intf.getName() + " ");
		}
		System.out.println();
	
		Method[] methods = objClass.getDeclaredMethods();
		System.out.println("\n*************************************\nMETHODS\n*************************************");
		for (Method method : methods) {
			System.out.println("\nMethod name: " + method.getName());
			System.out.println("Method return type: " + method.getReturnType().getName());
			System.out.println("Method modifiers: " + Modifier.toString(method.getModifiers()));
			Class<?>[] parameterTypes = method.getParameterTypes();
			System.out.print("Method parameter types: ");
			for (Class<?> paramType : parameterTypes) {
				System.out.print(paramType.getName() + " ");
			}
			System.out.println();
			Class<?>[] exceptionTypes = method.getExceptionTypes();
			System.out.print("Exception Types: ");
			for (Class<?> exceptionType : exceptionTypes) {
				System.out.print(exceptionType.getName() + " ");
			}
			System.out.println();
		}

		// CONSTRUCTORS //
		Constructor<?>[] constructors = objClass.getDeclaredConstructors();
		System.out.println(
				"\n*************************************\nCONSTRUCTORS\n*************************************");
		for (Constructor<?> constructor : constructors) {
			// get constructor name
			System.out.println("\nConstructor name: " + constructor.getName());
			System.out.println(
					"Constructor modifiers: " + Modifier.toString(constructor.getModifiers()));
			Class<?>[] constructorParameterTypes = constructor.getParameterTypes();
			System.out.print("Constructor parameter types: ");
			for (Class<?> paramType : constructorParameterTypes) {
				System.out.print(paramType.getName() + " ");
			}
			System.out.println();
		}

  		// FIELDS
        Field[] fields = objClass.getDeclaredFields();
        System.out.println("\n*************************************\nFIELDS\n*************************************");
        for (Field field : fields) {
			try {
            	field.setAccessible(true);
			}
			catch (Exception e) {
				System.out.println("Error: Unable to set field to accesible");
				continue;
			}
            System.out.println("\nField name: " + field.getName());
            System.out.println("Field type: " + field.getType().getName());
            System.out.println("Field modifiers: " + Modifier.toString(field.getModifiers()));

            if (field.getType().isPrimitive()) {
                System.out.println("Field is primitive");
                Object value = field.get(obj);
                System.out.println("Value: " + value);
            } else if (field.getType().isArray()) {
                System.out.println("Field is an array");
                Object value = field.get(obj);
                if (value != null) {
                    int length = Array.getLength(value);
                    System.out.println("Array length: " + length);
                    System.out.println("Array contents: " + Arrays.toString((Object[]) value));
                } else {
                    System.out.println("Value is null");
                }
            } else {
                // handle non array reference fields
                Object objValue = field.get(obj);
                if (objValue != null && recursive) {
                    System.out.println("Value: ");
                    inspectObject(objValue, true);
                } else {
                    System.out.println("Value: " + objValue);
                }
            }
		} 
    }
}