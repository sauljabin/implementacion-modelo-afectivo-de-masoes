/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package util;

import org.junit.Test;

import java.awt.*;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

public class ColorsTest {

    @Test
    public void shouldGetRandomColorWhenNumberIsNotFound() {
        Color color1 = Colors.getColor(1000);
        Color color2 = Colors.getColor(1000);
        assertThat(color1, is(not(color2)));
    }

}