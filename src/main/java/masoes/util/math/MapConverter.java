/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.util.math;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

public class MapConverter {

    private MapConverter() {
    }

    public static Map<String, String> convert(String stringMap) {
        validateArgument(stringMap);
        String[] settings = splitArgument(stringMap);

        Map<String, String> map = new HashMap<>();

        for (String pair : settings) {
            setValue(map, pair);
        }
        return map;
    }

    private static void validateArgument(String stringMap) {
        if (!stringMap.matches("^\\{(\\w+=\\w+(, *)?)+\\}$")) {
            throw new InvalidParameterException("Incorrect string format: " + stringMap);
        }
    }

    private static void setValue(Map<String, String> map, String pair) {
        String[] pairArray = pair.split("=");
        map.put(pairArray[0].trim(), pairArray[1].trim());
    }

    private static String[] splitArgument(String optionValue) {
        return optionValue.trim().replaceAll("\\{|\\}", "").trim().split(",");
    }

}
