/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package util;

import org.junit.Test;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

public class ColorsTest {

    @Test
    public void shouldGetRandomColorWhenNumberIsNotFound() {
        Color originalColor = Colors.getColor(1000);

        List<Color> occurrences = IntStream.range(0, 5)
                .mapToObj(i -> Colors.getRandomColor())
                .filter(emotion -> !emotion.equals(originalColor))
                .collect(Collectors.toList());

        assertThat(occurrences, is(not(empty())));
    }

}