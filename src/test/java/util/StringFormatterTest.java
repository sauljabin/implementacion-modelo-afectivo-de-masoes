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

    @Test
    public void normalizeNAndU() {
        assertThat(StringFormatter.toCamelCase("üñ"), is("un"));
    }

    @Test
    public void shouldShowZeroDecimalSize() {
        assertThat(StringFormatter.toString(0.0), is("0"));
    }

    @Test
    public void shouldShowOneDecimalSize() {
        assertThat(StringFormatter.toString(0.2), is("0.2"));
    }

    @Test
    public void shouldShowOneDecimalSizeWithoutNumber() {
        assertThat(StringFormatter.toString(.2), is("0.2"));
    }

    @Test
    public void shouldShowTwoDecimalSize() {
        assertThat(StringFormatter.toString(0.23), is("0.23"));
    }

    @Test
    public void shouldShowThreeDecimalSize() {
        assertThat(StringFormatter.toString(0.2359), is("0.236"));
    }

    @Test
    public void shouldShowStringPointFormat() {
        assertThat(StringFormatter.toStringPoint(.5, .6), is("(0.5, 0.6)"));
    }

}