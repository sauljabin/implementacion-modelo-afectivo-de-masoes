/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.util.collection;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class MapParserTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private Map<String, String> expectedMap;
    private String actualString;
    private MapParser mapParser;

    @Before
    public void setUp() {
        expectedMap = new HashMap<>();
        expectedMap.put("key1", "value1");
        actualString = expectedMap.toString();
        mapParser = new MapParser();
    }

    @Test
    public void shouldReturnCorrectMap() {
        assertReflectionEquals(expectedMap, mapParser.parseMap(actualString));
    }

    @Test
    public void shouldReturnCorrectMapWithTwoValues() {
        expectedMap.put("key2", "value2");
        actualString = expectedMap.toString();
        assertReflectionEquals(expectedMap, mapParser.parseMap(actualString));
    }

    @Test
    public void shouldThrowInvalidParameterWhenNoExistValueException() {
        String expectedArgs = "{setting1=,setting2=value2}";
        expectedException.expect(InvalidParameterException.class);
        expectedException.expectMessage("Incorrect string format: " + expectedArgs);
        mapParser.parseMap(expectedArgs);
    }

}