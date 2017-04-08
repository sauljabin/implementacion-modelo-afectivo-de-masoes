/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package util;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.isOneOf;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.core.Is.is;

public class RandomGeneratorTest {

    @Test
    public void shouldReturnRandomNumberInInterval() {
        double xMin = 1.2;
        double xMax = 5.6;
        assertRandomDouble(xMin, xMax);
    }

    @Test
    public void shouldReturnNegativeNumber() {
        double xMin = -10.1;
        double xMax = -0.1;
        assertRandomDouble(xMin, xMax);
    }

    @Test
    public void shouldReturnRandomInteger() {
        assertRandomInteger(1, 10);
    }

    @Test
    public void shouldReturnRandomValueFromList() {
        List<String> stringList = Arrays.asList("item1", "item2", "item3");
        assertThat(RandomGenerator.getRandomItem(stringList), isOneOf(stringList.toArray()));
    }

    private void assertRandomInteger(int xMin, int xMax) {
        IntStream.range(0, 1000).forEach(i -> {
            int value = RandomGenerator.getInteger(xMin, xMax);
            assertThat(value, is(greaterThanOrEqualTo(xMin)));
            assertThat(value, is(lessThanOrEqualTo(xMax)));
        });
    }

    private void assertRandomDouble(double xMin, double xMax) {
        IntStream.range(0, 1000).forEach(i -> {
            double value = RandomGenerator.getDouble(xMin, xMax);
            assertThat(value, is(greaterThanOrEqualTo(xMin)));
            assertThat(value, is(lessThanOrEqualTo(xMax)));
        });
    }

}