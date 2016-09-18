/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.util;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.core.Is.is;

public class RandomGeneratorTest {

    private static final int ITERATIONS = 100;
    private RandomGenerator randomGenerator;

    @Before
    public void setUp() throws Exception {
        randomGenerator = new RandomGenerator();
    }

    @Test
    public void shouldReturnRandomNumberInInterval() {
        double xMin = 1.2;
        double xMax = 5.6;
        for (int i = 0; i < ITERATIONS; i++) {
            assertRandom(xMin, xMax);
        }
    }

    @Test
    public void shouldReturnNegativeNumber() {
        double xMin = -100;
        double xMax = -0.1;
        for (int i = 0; i < ITERATIONS; i++) {
            assertRandom(xMin, xMax);
        }
    }

    private void assertRandom(double xMin, double xMax) {
        double value = randomGenerator.random(xMin, xMax);
        assertThat(value, is(greaterThanOrEqualTo(xMin)));
        assertThat(value, is(lessThanOrEqualTo(xMax)));
    }

}