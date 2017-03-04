package test;

import java.lang.reflect.Field;

public final class ReflectionTestUtils {

    private ReflectionTestUtils() {
    }

    public static void setFieldValue(Object affectedObject, String fieldName, Object newValue) throws Exception {
        Class<?> aClass = affectedObject.getClass();
        Field affectedField;

        try {
            affectedField = aClass.getDeclaredField(fieldName);
        } catch (Exception e) {
            affectedField = aClass.getSuperclass().getDeclaredField(fieldName);
        }

        boolean shouldResetAccessibility = false;

        if (!affectedField.isAccessible()) {
            affectedField.setAccessible(true);
            shouldResetAccessibility = true;
        }

        affectedField.set(affectedObject, newValue);

        if (shouldResetAccessibility) {
            affectedField.setAccessible(false);
        }
    }

}
