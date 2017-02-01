/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ToStringBuilder {

    private Object object;
    private Map<Object, Object> objects;

    public ToStringBuilder() {
        objects = new HashMap<>();
    }

    public ToStringBuilder object(Object object) {
        this.object = object;
        return this;
    }

    public ToStringBuilder field(String name, Object object) {
        objects.put(name, object);
        return this;
    }

    @Override
    public String toString() {
        String objectName = Optional.ofNullable(object).isPresent() ? object.getClass().getSimpleName() : "";
        return String.format("%s%s", objectName, getFieldsString());
    }

    private String getFieldsString() {
        if (objects.isEmpty()) {
            return "";
        }

        return mapToString(objects);
    }

    private String mapToString(Map<Object, Object> objects) {
        List<Object> stringObjectList = objects.entrySet().stream().map(entry ->
                String.format("%s=%s", objectToString(entry.getKey()), objectToString(entry.getValue()))
        ).collect(Collectors.toList());

        return listToString(stringObjectList);
    }

    private String listToString(List<Object> objects) {
        List<String> stringObjectList = objects.stream()
                .map(object -> objectToString(object))
                .collect(Collectors.toList());

        return new StringBuilder()
                .append("[")
                .append(String.join(", ", stringObjectList))
                .append("]")
                .toString();
    }

    private String objectArrayToString(Object[] objects) {
        return listToString(Arrays.asList(objects));
    }

    private String objectToString(Object object) {
        if (object instanceof List) {
            return listToString((List<Object>) object);
        } else if (object instanceof Map) {
            return mapToString((Map<Object, Object>) object);
        } else if (object instanceof Object[]) {
            return objectArrayToString((Object[]) object);
        } else if (object instanceof int[]) {
            return intArrayToString((int[]) object);
        } else if (object instanceof double[]) {
            return doubleArrayToString((double[]) object);
        } else if (object instanceof long[]) {
            return longArrayToString((long[]) object);
        } else if (object instanceof boolean[]) {
            return booleanArrayToString((boolean[]) object);
        } else if (object instanceof byte[]) {
            return byteArrayToString((byte[]) object);
        } else if (object instanceof short[]) {
            return shortArrayToString((short[]) object);
        } else if (object instanceof float[]) {
            return floatArrayToString((float[]) object);
        } else if (object instanceof char[]) {
            return charArrayToString((char[]) object);
        }
        return object.toString();
    }

    private String charArrayToString(char[] array) {
        List<Object> objectList = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            objectList.add(array[i]);
        }
        return listToString(objectList);
    }

    private String floatArrayToString(float[] array) {
        List<Object> objectList = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            objectList.add(array[i]);
        }
        return listToString(objectList);
    }

    private String booleanArrayToString(boolean[] array) {
        List<Object> objectList = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            objectList.add(array[i]);
        }
        return listToString(objectList);
    }

    private String byteArrayToString(byte[] array) {
        List<Object> objectList = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            objectList.add(array[i]);
        }
        return listToString(objectList);
    }

    private String shortArrayToString(short[] array) {
        List<Object> objectList = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            objectList.add(array[i]);
        }
        return listToString(objectList);
    }

    private String intArrayToString(int[] array) {
        List<Object> objectList = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            objectList.add(array[i]);
        }
        return listToString(objectList);
    }

    private String longArrayToString(long[] array) {
        List<Object> objectList = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            objectList.add(array[i]);
        }
        return listToString(objectList);
    }

    private String doubleArrayToString(double[] array) {
        List<Object> objectList = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            objectList.add(array[i]);
        }
        return listToString(objectList);
    }

}
