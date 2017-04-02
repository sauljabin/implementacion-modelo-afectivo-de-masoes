/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package util;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

public class StringGeneratorTest {

    @Test
    public void shouldReturnRandomString() {
        int length = 100;
        String actualString = StringGenerator.getString(length);
        String actualStringAgain = StringGenerator.getString(length);
        assertThat(actualString.length(), is(length));
        assertThat(actualString, is(not(actualStringAgain)));
    }

}