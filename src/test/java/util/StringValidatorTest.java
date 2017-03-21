/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package util;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StringValidatorTest {

    private Random random;

    @Before
    public void setUp() {
        random = new Random();
    }

    @Test
    public void shouldReturnTrueWhenStringIsInteger() {
        assertTrue(StringValidator.isInteger(String.valueOf(random.nextInt(1000))));
        assertTrue(StringValidator.isInteger(String.valueOf(-random.nextInt(1000))));
        assertFalse(StringValidator.isInteger("text"));
        assertFalse(StringValidator.isInteger("1.0"));
    }

    @Test
    public void shouldReturnTrueWhenStringIsReal() {
        assertTrue(StringValidator.isReal(String.valueOf(random.nextDouble())));
        assertTrue(StringValidator.isReal(String.valueOf(-random.nextDouble())));
        assertFalse(StringValidator.isReal("text"));
        assertFalse(StringValidator.isReal("1"));
    }

}