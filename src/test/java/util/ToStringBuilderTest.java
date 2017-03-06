/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package util;

import jade.util.leap.ArrayList;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.text.IsEmptyString.isEmptyString;
import static org.junit.Assert.assertThat;

public class ToStringBuilderTest {

    private ToStringBuilder toStringBuilder;

    @Before
    public void setUp() {
        toStringBuilder = new ToStringBuilder();
    }

    @Test
    public void shouldGetEmptyStringWhenAnyObjectIsAdded() {
        assertThat(toStringBuilder.toString(), isEmptyString());
    }

    @Test
    public void shouldNotIncludeNullField() {
        assertThat(toStringBuilder.append("field", null).toString(), isEmptyString());
    }

    @Test
    public void shouldIncludeClassNameInString() {
        Object object = new Object();
        assertThat(toStringBuilder.object(object).toString(), is(object.getClass().getSimpleName()));
    }

    @Test
    public void shouldIncludeField() {
        String actual = toStringBuilder.append("expectedName", "expectedStringObject").toString();
        assertThat(actual, is("[expectedName=expectedStringObject]"));
    }

    @Test
    public void shouldIncludeTwoField() {
        String actual = toStringBuilder
                .append("expectedName", "expectedStringObject")
                .append("expectedName2", "expectedStringObject2")
                .toString();
        assertThat(actual, is("[expectedName=expectedStringObject, expectedName2=expectedStringObject2]"));
    }

    @Test
    public void shouldIncludeFieldAndClassName() {
        Object object = new Object();
        String actual = toStringBuilder
                .object(object)
                .append("expectedName", "expectedStringObject")
                .toString();
        assertThat(actual, is("Object[expectedName=expectedStringObject]"));
    }

    @Test
    public void shouldIncludeArrayField() {
        String[] arrayObject = {"A", "B"};
        String actual = toStringBuilder
                .append("expectedName", arrayObject)
                .toString();
        assertThat(actual, is("[expectedName=[A, B]]"));
    }

    @Test
    public void shouldIncludeIntArrayField() {
        int[] arrayObject = {1, 2};
        String actual = toStringBuilder
                .append("expectedName", arrayObject)
                .toString();
        assertThat(actual, is("[expectedName=[1, 2]]"));
    }

    @Test
    public void shouldIncludeDoubleArrayField() {
        double[] arrayObject = {1.3, 2.};
        String actual = toStringBuilder
                .append("expectedName", arrayObject)
                .toString();
        assertThat(actual, is("[expectedName=[1.3, 2.0]]"));
    }

    @Test
    public void shouldIncludeLongArrayField() {
        long[] arrayObject = {100, 200};
        String actual = toStringBuilder
                .append("expectedName", arrayObject)
                .toString();
        assertThat(actual, is("[expectedName=[100, 200]]"));
    }

    @Test
    public void shouldIncludeBooleanArrayField() {
        boolean[] arrayObject = {true, false};
        String actual = toStringBuilder
                .append("expectedName", arrayObject)
                .toString();
        assertThat(actual, is("[expectedName=[true, false]]"));
    }

    @Test
    public void shouldIncludeByteArrayField() {
        byte[] arrayObject = {1, 2};
        String actual = toStringBuilder
                .append("expectedName", arrayObject)
                .toString();
        assertThat(actual, is("[expectedName=[1, 2]]"));
    }

    @Test
    public void shouldIncludeShortArrayField() {
        short[] arrayObject = {1, 2};
        String actual = toStringBuilder
                .append("expectedName", arrayObject)
                .toString();
        assertThat(actual, is("[expectedName=[1, 2]]"));
    }

    @Test
    public void shouldIncludeFloatArrayField() {
        float[] arrayObject = {1.1F, 2.2F};
        String actual = toStringBuilder
                .append("expectedName", arrayObject)
                .toString();
        assertThat(actual, is("[expectedName=[1.1, 2.2]]"));
    }

    @Test
    public void shouldIncludeCharArrayField() {
        char[] arrayObject = {'A', 'B'};
        String actual = toStringBuilder
                .append("expectedName", arrayObject)
                .toString();
        assertThat(actual, is("[expectedName=[A, B]]"));
    }

    @Test
    public void shouldIncludeListField() {
        String actual = toStringBuilder
                .append("expectedName", Arrays.asList("A", "B"))
                .toString();
        assertThat(actual, is("[expectedName=[A, B]]"));
    }

    @Test
    public void shouldIncludeLeapListField() {
        Map<String, String> map = new HashMap<>();
        map.put("1", "A");
        map.put("2", "B");

        jade.util.leap.List list = new ArrayList();
        list.add(map);
        list.add("B");

        String actual = toStringBuilder
                .append("expectedName", list)
                .toString();
        assertThat(actual, is("[expectedName=[[1=A, 2=B], B]]"));
    }

    @Test
    public void shouldIncludeDoubleListField() {
        String actual = toStringBuilder
                .append("expectedName", Arrays.asList(1.1, 2.2))
                .toString();
        assertThat(actual, is("[expectedName=[1.1, 2.2]]"));
    }

    @Test
    public void shouldIncludeIntListField() {
        List<Integer> object = Arrays.asList(1, 2);
        String actual = toStringBuilder
                .append("expectedName", object)
                .toString();
        assertThat(actual, is("[expectedName=[1, 2]]"));
    }

    @Test
    public void shouldIncludeMapField() {
        Map<String, String> map = new HashMap<>();
        map.put("1", "A");
        map.put("2", "B");
        String actual = toStringBuilder
                .append("expectedName", map)
                .toString();
        assertThat(actual, is("[expectedName=[1=A, 2=B]]"));
    }

}