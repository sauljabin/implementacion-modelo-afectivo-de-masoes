package test;

import java.lang.reflect.Field;

public class ReflectionTestUtils {

    public static void setField(Object affectedObject, String fieldName, Object newValue) throws NoSuchFieldException, IllegalAccessException {
        Class<?> aClass = affectedObject.getClass();
        Field affectedField = aClass.getDeclaredField(fieldName);
        boolean shouldResetAccessibility = false;
        if(!affectedField.isAccessible()){
            affectedField.setAccessible(true);
            shouldResetAccessibility = true;
        }
        affectedField.set(affectedObject, newValue);
        if(shouldResetAccessibility) {
            affectedField.setAccessible(false);
        }
    }
}
