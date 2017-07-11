/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package test;

import util.StringGenerator;

import java.util.Random;

public final class RandomUtil {

    private RandomUtil() {
    }

    public static String randomString() {
        return StringGenerator.getString(20);
    }

    public static int randomInt() {
        Random random = new Random();
        return random.nextInt();
    }

    public static double randomDouble() {
        Random random = new Random();
        return random.nextDouble();
    }

    public static boolean randomBoolean() {
        Random random = new Random();
        return random.nextBoolean();
    }

}
