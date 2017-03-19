/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application.option;

import application.ArgumentType;
import jade.JadeBoot;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class BootOptionTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private BootOption bootOption;
    private JadeBoot jadeBootMock;

    @Before
    public void setUp() throws Exception {
        jadeBootMock = mock(JadeBoot.class);
        bootOption = new BootOption();
        setFieldValue(bootOption, "jadeBoot", jadeBootMock);
    }

    @Test
    public void shouldGetCorrectConfiguration() {
        assertThat(bootOption.getOpt(), is("b"));
        assertThat(bootOption.getLongOpt(), is("boot"));
        assertThat(bootOption.getDescription(), is(notNullValue()));
        assertThat(bootOption.getArgType(), is(ArgumentType.NO_ARGS));
        assertThat(bootOption.getOrder(), is(70));
        assertTrue(bootOption.isFinalOption());
    }

    @Test
    public void shouldInvokeJadeBoot() {
        bootOption.exec();
        verify(jadeBootMock).boot();
    }

}