/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package behaviour;

import org.junit.Before;
import org.junit.Test;

import java.util.stream.IntStream;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class CounterBehaviourTest {

    private CounterBehaviour counterBehaviourSpy;

    @Before
    public void setUp() {
        counterBehaviourSpy = spy(createBehaviour(5));
        counterBehaviourSpy.onStart();
    }

    public CounterBehaviour createBehaviour(int maxCount) {
        return new CounterBehaviour(maxCount) {
            @Override
            public void count(int i) {

            }
        };
    }

    @Test
    public void shouldInvokeCount() {
        counterBehaviourSpy.action();
        verify(counterBehaviourSpy).count(1);
    }

    @Test
    public void shouldInitCountOnStart() {
        counterBehaviourSpy.action();
        counterBehaviourSpy.onStart();
        assertThat(counterBehaviourSpy.getCount(), is(0));
    }

    @Test
    public void shouldReturnDoneWhenCountIsGreaterThanMaxCount() {
        IntStream.range(0, 5).forEach(value -> counterBehaviourSpy.action());
        assertTrue(counterBehaviourSpy.done());
        verify(counterBehaviourSpy).count(5);
    }

    @Test
    public void shouldReturnNotDoneWhenCountIsLessThanMaxCount() {
        IntStream.range(0, 4).forEach(value -> counterBehaviourSpy.action());
        assertFalse(counterBehaviourSpy.done());
    }

}