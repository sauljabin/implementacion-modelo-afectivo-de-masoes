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
    public void setUp() throws Exception {
        toStringBuilder = new ToStringBuilder();
    }

    @Test
    public void shouldGetEmptyStringWhenAnyObjectIsAdded() throws Exception {
        assertThat(toStringBuilder.toString(), isEmptyString());
    }

    @Test
    public void shouldNotIncludeNullField() throws Exception {
        assertThat(toStringBuilder.append("field", null).toString(), isEmptyString());
    }

    @Test
    public void shouldIncludeClassNameInString() {
        Object object = new Object();
        assertThat(toStringBuilder.object(object).toString(), is(object.getClass().getSimpleName()));
    }

    @Test
    public void shouldIncludeField() throws Exception {
        String actual = toStringBuilder.append("expectedName", "expectedStringObject").toString();
        assertThat(actual, is("[expectedName=expectedStringObject]"));
    }

    @Test
    public void shouldIncludeTwoField() throws Exception {
        String actual = toStringBuilder
                .append("expectedName", "expectedStringObject")
                .append("expectedName2", "expectedStringObject2")
                .toString();
        assertThat(actual, is("[expectedName=expectedStringObject, expectedName2=expectedStringObject2]"));
    }

    @Test
    public void shouldIncludeFieldAndClassName() throws Exception {
        Object object = new Object();
        String actual = toStringBuilder
                .object(object)
                .append("expectedName", "expectedStringObject")
                .toString();
        assertThat(actual, is("Object[expectedName=expectedStringObject]"));
    }

    @Test
    public void shouldIncludeArrayField() throws Exception {
        String[] arrayObject = {"A", "B"};
        String actual = toStringBuilder
                .append("expectedName", arrayObject)
                .toString();
        assertThat(actual, is("[expectedName=[A, B]]"));
    }

    @Test
    public void shouldIncludeIntArrayField() throws Exception {
        int[] arrayObject = {1, 2};
        String actual = toStringBuilder
                .append("expectedName", arrayObject)
                .toString();
        assertThat(actual, is("[expectedName=[1, 2]]"));
    }

    @Test
    public void shouldIncludeDoubleArrayField() throws Exception {
        double[] arrayObject = {1.3, 2.};
        String actual = toStringBuilder
                .append("expectedName", arrayObject)
                .toString();
        assertThat(actual, is("[expectedName=[1.3, 2.0]]"));
    }

    @Test
    public void shouldIncludeLongArrayField() throws Exception {
        long[] arrayObject = {100, 200};
        String actual = toStringBuilder
                .append("expectedName", arrayObject)
                .toString();
        assertThat(actual, is("[expectedName=[100, 200]]"));
    }

    @Test
    public void shouldIncludeBooleanArrayField() throws Exception {
        boolean[] arrayObject = {true, false};
        String actual = toStringBuilder
                .append("expectedName", arrayObject)
                .toString();
        assertThat(actual, is("[expectedName=[true, false]]"));
    }

    @Test
    public void shouldIncludeByteArrayField() throws Exception {
        byte[] arrayObject = {1, 2};
        String actual = toStringBuilder
                .append("expectedName", arrayObject)
                .toString();
        assertThat(actual, is("[expectedName=[1, 2]]"));
    }

    @Test
    public void shouldIncludeShortArrayField() throws Exception {
        short[] arrayObject = {1, 2};
        String actual = toStringBuilder
                .append("expectedName", arrayObject)
                .toString();
        assertThat(actual, is("[expectedName=[1, 2]]"));
    }

    @Test
    public void shouldIncludeFloatArrayField() throws Exception {
        float[] arrayObject = {1.1F, 2.2F};
        String actual = toStringBuilder
                .append("expectedName", arrayObject)
                .toString();
        assertThat(actual, is("[expectedName=[1.1, 2.2]]"));
    }

    @Test
    public void shouldIncludeCharArrayField() throws Exception {
        char[] arrayObject = {'A', 'B'};
        String actual = toStringBuilder
                .append("expectedName", arrayObject)
                .toString();
        assertThat(actual, is("[expectedName=[A, B]]"));
    }

    @Test
    public void shouldIncludeListField() throws Exception {
        String actual = toStringBuilder
                .append("expectedName", Arrays.asList("A", "B"))
                .toString();
        assertThat(actual, is("[expectedName=[A, B]]"));
    }

    @Test
    public void shouldIncludeLeapListField() throws Exception {
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
    public void shouldIncludeDoubleListField() throws Exception {
        String actual = toStringBuilder
                .append("expectedName", Arrays.asList(1.1, 2.2))
                .toString();
        assertThat(actual, is("[expectedName=[1.1, 2.2]]"));
    }

    @Test
    public void shouldIncludeIntListField() throws Exception {
        List<Integer> object = Arrays.asList(1, 2);
        String actual = toStringBuilder
                .append("expectedName", object)
                .toString();
        assertThat(actual, is("[expectedName=[1, 2]]"));
    }

    @Test
    public void shouldIncludeMapField() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("1", "A");
        map.put("2", "B");
        String actual = toStringBuilder
                .append("expectedName", map)
                .toString();
        assertThat(actual, is("[expectedName=[1=A, 2=B]]"));
    }

}