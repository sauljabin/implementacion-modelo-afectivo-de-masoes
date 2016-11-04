/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.option;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ApplicationOptionTest {

    private ApplicationOption mockApplicationOptionA;
    private ApplicationOption mockApplicationOptionB;

    @Before
    public void setUp() {
        mockApplicationOptionA = mock(ApplicationOption.class);
        mockApplicationOptionB = mock(ApplicationOption.class);
    }

    @Test
    public void shouldCorrectCompareObject() {
        when(mockApplicationOptionA.compareTo(any())).thenCallRealMethod();
        when(mockApplicationOptionB.compareTo(any())).thenCallRealMethod();
        when(mockApplicationOptionA.getOrder()).thenReturn(1);
        when(mockApplicationOptionB.getOrder()).thenReturn(2);
        assertThat(mockApplicationOptionA.compareTo(mockApplicationOptionA), is(0));
        assertThat(mockApplicationOptionB.compareTo(mockApplicationOptionB), is(0));
        assertThat(mockApplicationOptionA.compareTo(mockApplicationOptionB), is(lessThan(0)));
        assertThat(mockApplicationOptionB.compareTo(mockApplicationOptionA), is(greaterThan(0)));
    }

    @Test
    public void shouldGetCorrectString() {
        when(mockApplicationOptionA.getOpt()).thenReturn("a");
        when(mockApplicationOptionA.getLongOpt()).thenReturn("testA");
        when(mockApplicationOptionA.getOrder()).thenReturn(1);
        when(mockApplicationOptionA.toString()).thenCallRealMethod();
        assertThat(mockApplicationOptionA.toString(), is("{option=[-a,--testA], order=1}"));
    }

    @Test
    public void shouldGetCorrectKeyWhenOptIsNull() {
        when(mockApplicationOptionA.getKeyOpt()).thenCallRealMethod();
        when(mockApplicationOptionA.getLongOpt()).thenReturn("testA");
        assertThat(mockApplicationOptionA.getKeyOpt(), is(mockApplicationOptionA.getLongOpt()));
    }

    @Test
    public void shouldGetCorrectKeyLongOptIsNull() {
        when(mockApplicationOptionA.getKeyOpt()).thenCallRealMethod();
        when(mockApplicationOptionA.getOpt()).thenReturn("a");
        assertThat(mockApplicationOptionA.getKeyOpt(), is(mockApplicationOptionA.getOpt()));
    }

}