/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

import java.util.Iterator;

public class Alphabet implements Iterator<Character> {

    private static final int CHAR_TO_START = 64;
    private static final int CHAR_TO_END = 90;
    private int currentChar = CHAR_TO_START;

    public static void main(String[] args) {
        Alphabet alphabet = new Alphabet();
        while (alphabet.hasNext()) {
            System.out.println(alphabet.next());
        }
    }

    @Override
    public boolean hasNext() {
        return currentChar != CHAR_TO_END;
    }

    @Override
    public Character next() {
        if (hasNext()) {
            return (char) ++currentChar;
        } else {
            throw new RuntimeException("No more elements");
        }
    }

}
