/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package util;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class StringFormatterTest {

    @Test
    public void shouldReturnCorrectCamelCaseValue() {
        String inputString = "Normal text";
        String expectedString = "normalText";

        assertThat(StringFormatter.toCamelCase(inputString), is(expectedString));
    }

    @Test
    public void shouldReturnCorrectCamelCaseValueWhitSpecialCharacters() {
        String inputString = "Normal text and carácteres and simbols - óo / íi / úu / ?";
        String expectedString = "normalTextAndCaracteresAndSimbolsOoIiUu";

        assertThat(StringFormatter.toCamelCase(inputString), is(expectedString));
    }

}