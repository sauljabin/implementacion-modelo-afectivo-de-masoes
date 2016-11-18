/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.util.math;

import java.util.Random;

public class RandomGenerator {

    public double getDouble(double min, double max) {
        return (new Random().nextDouble() * (max - min)) + min;
    }

}
