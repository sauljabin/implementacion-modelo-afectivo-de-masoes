package test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

public final class ReflectionTestUtils {

    private ReflectionTestUtils() {
    }

    public static void setFieldValue(Object affectedObject, String fieldName, Object newValue) throws Exception {
        Class<?> aClass = affectedObject.getClass();
        Field affectedField;

        Optional<Field> first = Arrays.stream(aClass.getDeclaredFields())
                .filter(field -> field.getName().equals(fieldName))
                .findFirst();

        if (first.isPresent()) {
            affectedField = aClass.getDeclaredField(fieldName);
        } else {
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
