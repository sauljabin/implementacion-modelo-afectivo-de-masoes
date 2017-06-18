/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package util;

public final class StringValidator {

    private StringValidator() {
    }

    public static boolean isInteger(String string) {
        return string.matches("-?[0-9]+");
    }

    public static boolean isReal(String string) {
        return string.matches("-?([0-9]+)?.[0-9]+");
    }

    public static boolean isNumber(String string) {
        return isInteger(string) || isReal(string);
    }

}
