/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package util;

import java.util.Random;
import java.util.stream.IntStream;

public class StringGenerator {

    private static final String ALPHA_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String ALPHA_NUMERIC_CHARACTERS = "0123456789" + ALPHA_CHARACTERS;
    private Random random;

    public StringGenerator() {
        random = new Random();
    }

    public String getString(int length) {
        return getString(length, false);
    }

    public String getString(int length, boolean alphanumeric) {
        StringBuilder stringBuilder = new StringBuilder();
        IntStream.range(0, length).forEach(i -> stringBuilder.append(getCharacter(alphanumeric)));
        return stringBuilder.toString();
    }

    public char getCharacter() {
        return getCharacter(false);
    }

    public char getCharacter(boolean alphanumeric) {
        String base = alphanumeric ? ALPHA_NUMERIC_CHARACTERS : ALPHA_CHARACTERS;
        return base.charAt(random.nextInt(base.length()));
    }

}
