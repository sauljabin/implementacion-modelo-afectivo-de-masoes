/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package util;

import java.util.List;
import java.util.Random;

public final class RandomGenerator {

    private static Random random = new Random();

    private RandomGenerator() {
    }

    public static double getDouble(double min, double max) {
        return (random.nextDouble() * (max - min)) + min;
    }

    public static int getInteger(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    public static <T> T getRandomItem(List<T> list) {
        return list.get(random.nextInt(list.size()));
    }

}
