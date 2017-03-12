/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package util;

import java.util.Random;

public class RandomGenerator {

    private Random random;

    public RandomGenerator() {
        random = new Random();
    }

    public double getDouble(double min, double max) {
        return (random.nextDouble() * (max - min)) + min;
    }

}
