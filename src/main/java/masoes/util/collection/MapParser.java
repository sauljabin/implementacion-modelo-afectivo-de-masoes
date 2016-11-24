/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.util.collection;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapParser {

    public Map<String, String> parseMap(String stringMap) {
        validateArgument(stringMap);
        String[] settings = splitArgument(stringMap);

        Map<String, String> map = new HashMap<>();

        for (String pair : settings) {
            setValue(map, pair);
        }

        return map;
    }

    private void validateArgument(String stringMap) {
        if (!stringMap.matches("^\\{(\\w+=[^,]+(, *)?)+\\}$")) {
            throw new InvalidParameterException("Incorrect string format: " + stringMap);
        }
    }

    private void setValue(Map<String, String> map, String pair) {
        String key = pair.substring(0, pair.indexOf("="));
        String value = pair.substring(pair.indexOf("=") + 1, pair.length());
        map.put(key.trim(), value.trim());
    }

    private String[] splitArgument(String optionValue) {
        char[] rawChars = optionValue.trim().substring(1, optionValue.length() - 1).trim().toCharArray();

        List<String> listArguments = new ArrayList<>();

        StringBuilder tempArg = new StringBuilder();
        boolean inBrace = false;

        for (int i = 0; i < rawChars.length; i++) {
            char value = rawChars[i];

            if (value == '{' || value == '(' || value == '[') {
                inBrace = true;
            } else if (value == '}' || value == ')' || value == ']') {
                inBrace = false;
            }

            if (value != ',' || inBrace) {
                tempArg.append(value);
            } else {
                listArguments.add(tempArg.toString().trim());
                tempArg.setLength(0);
            }
        }
        listArguments.add(tempArg.toString().trim());
        return listArguments.toArray(new String[listArguments.size()]);
    }

}
