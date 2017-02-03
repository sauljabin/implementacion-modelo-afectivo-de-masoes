/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package util;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.unitils.util.ReflectionUtils.getFieldValue;
import static org.unitils.util.ReflectionUtils.getFieldWithName;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class StopWatchTest {

    private StopWatch stopWatch;
    private Field startTimeField;
    private Field stopTimeField;

    @Before
    public void setUp() {
        stopWatch = new StopWatch();
        startTimeField = getFieldWithName(StopWatch.class, "startTime", false);
        stopTimeField = getFieldWithName(StopWatch.class, "stopTime", false);
    }

    @Test
    public void shouldSetNotStartedWatch() {
        Object startTime = getFieldValue(stopWatch, startTimeField);
        Object stopTime = getFieldValue(stopWatch, stopTimeField);
        assertThat(startTime, is(-1L));
        assertThat(stopTime, is(-1L));
    }

    @Test
    public void shouldResetTime() {
        stopWatch.start();
        stopWatch.stop();
        stopWatch.reset();
        Object startTime = getFieldValue(stopWatch, startTimeField);
        Object stopTime = getFieldValue(stopWatch, stopTimeField);
        assertThat(startTime, is(-1L));
        assertThat(stopTime, is(-1L));
    }

    @Test
    public void shouldSetStartTime() {
        stopWatch.start();
        Long startTime = getFieldValue(stopWatch, startTimeField);
        assertThat(startTime, is(greaterThan(0L)));
    }

    @Test
    public void shouldSetStopTime() {
        stopWatch.stop();
        Long stopTime = getFieldValue(stopWatch, stopTimeField);
        assertThat(stopTime, is(greaterThan(0L)));
    }

    @Test
    public void shouldReturnNoTimeWhenStartIsLess0() {
        assertThat(stopWatch.getTime(), is(0L));
    }

    @Test
    public void shouldReturnSubtraction() throws Exception {
        setFieldValue(stopWatch, "startTime", 10L);
        setFieldValue(stopWatch, "stopTime", 15L);
        assertThat(stopWatch.getTime(), is(5L));
    }

    @Test
    public void shouldReturnCurrentTimeWhenNoStop() throws Exception {
        stopWatch.start();
        long sleep = 200L;
        Thread.sleep(sleep);
        assertThat(stopWatch.getTime(), is(greaterThanOrEqualTo(sleep)));
    }

}