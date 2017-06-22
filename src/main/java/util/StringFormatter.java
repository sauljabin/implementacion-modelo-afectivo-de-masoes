/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package util;

public class StringFormatter {

    public static String toCamelCase(String string) {
        StringBuilder stringBuilder = new StringBuilder();

        String[] splitValues = string
                .toLowerCase()
                .replace('á', 'a')
                .replace('é', 'e')
                .replace('í', 'i')
                .replace('ó', 'o')
                .replace('ú', 'u')
                .replaceAll("[^a-z ]+", "")
                .split(" ");

        stringBuilder.append(splitValues[0]);

        for (int i = 1; i < splitValues.length; i++) {
            String value = splitValues[i];
            if (!value.isEmpty()) {
                stringBuilder.append(value.substring(0, 1).toUpperCase());
                stringBuilder.append(value.substring(1).toLowerCase());
            }
        }

        return stringBuilder.toString();
    }

}
