/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.util.string;

import java.util.Random;
import java.util.stream.IntStream;

public class StringGenerator {

    private static final String CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private final Random random;

    public StringGenerator() {
        random = new Random();
    }

    public String getString(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        IntStream.range(0, length).forEach(i -> stringBuilder.append(getCharacter()));
        return stringBuilder.toString();
    }

    public char getCharacter() {
        return CHARACTERS.charAt(random.nextInt(CHARACTERS.length()));
    }

}
