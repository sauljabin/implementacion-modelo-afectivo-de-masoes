/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package util;

import java.util.Random;
import java.util.stream.IntStream;

public final class StringGenerator {

    private static final String ALPHA_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String ALPHA_NUMERIC_CHARACTERS = "0123456789" + ALPHA_CHARACTERS;
    private static Random random = new Random();

    private StringGenerator() {
    }

    public static String getString(int length) {
        return getString(length, false);
    }

    public static String getString(int length, boolean alphanumeric) {
        StringBuilder stringBuilder = new StringBuilder();
        IntStream.range(0, length).forEach(i -> stringBuilder.append(getCharacter(alphanumeric)));
        return stringBuilder.toString();
    }

    public static char getCharacter() {
        return getCharacter(false);
    }

    public static char getCharacter(boolean alphanumeric) {
        String base = alphanumeric ? ALPHA_NUMERIC_CHARACTERS : ALPHA_CHARACTERS;
        return base.charAt(random.nextInt(base.length()));
    }

}
