/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.option;

import masoes.env.EnvironmentFactory;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


public class BootOptionTest {

    private BootOption bootOption;
    private EnvironmentFactory mockEnvironmentFactory;

    @Before
    public void setUp() {
        mockEnvironmentFactory = mock(EnvironmentFactory.class);
        bootOption = new BootOption(mockEnvironmentFactory);
    }

    @Test
    public void shouldGetCorrectConfiguration() {
        assertThat(bootOption.getOpt(), is("b"));
        assertThat(bootOption.getLongOpt(), is("boot"));
        assertThat(bootOption.getDescription(), is("Starts the application"));
        assertFalse(bootOption.hasArg());
        assertThat(bootOption.getOrder(), is(60));
    }

    @Test
    public void shouldInvokeEnvironmentCreation() {
        bootOption.exec(null);
        verify(mockEnvironmentFactory).createEnvironment();
    }

}