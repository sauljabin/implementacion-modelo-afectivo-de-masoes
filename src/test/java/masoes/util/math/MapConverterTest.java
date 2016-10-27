/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.util.math;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class MapConverterTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private Map<String, String> expectedMap;
    private String actualString;

    @Before
    public void setUp() throws Exception {
        expectedMap = new HashMap<>();
        expectedMap.put("key1", "value1");
        actualString = expectedMap.toString();
    }

    @Test
    public void shouldReturnCorrectMap() {
        assertReflectionEquals(expectedMap, MapConverter.convert(actualString));
    }

    @Test
    public void shouldThrowInvalidParameterWhenNoExistValueException() {
        String expectedArgs = "{setting1=,setting2=value2}";
        expectedException.expect(InvalidParameterException.class);
        expectedException.expectMessage("Incorrect string format: " + expectedArgs);
        MapConverter.convert(expectedArgs);
    }

}