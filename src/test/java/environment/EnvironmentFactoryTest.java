/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment;

import application.ApplicationSettings;
import environment.dummy.DummyEnvironment;
import environment.wikipedia.WikipediaEnvironment;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class EnvironmentFactoryTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private EnvironmentFactory environmentFactory;
    private ApplicationSettings applicationSettingsMock;

    @Before
    public void setUp() throws Exception {
        applicationSettingsMock = mock(ApplicationSettings.class);
        environmentFactory = new EnvironmentFactory();
        setFieldValue(environmentFactory, "applicationSettings", applicationSettingsMock);
    }

    @Test
    public void shouldCreateDummyEnvironment() {
        doReturn("dummy").when(applicationSettingsMock).get(ApplicationSettings.MASOES_ENV);
        assertThat(environmentFactory.createEnvironment(), instanceOf(DummyEnvironment.class));
    }

    @Test
    public void shouldCreateWikipediaEnvironment() {
        doReturn("wikipedia").when(applicationSettingsMock).get(ApplicationSettings.MASOES_ENV);
        assertThat(environmentFactory.createEnvironment(), instanceOf(WikipediaEnvironment.class));
    }

    @Test
    public void shouldThrowInvalidEnvironmentWhenEnvironmentNotExist() {
        String stringArg = "anything";
        doReturn(stringArg).when(applicationSettingsMock).get(ApplicationSettings.MASOES_ENV);
        expectedException.expect(InvalidEnvironmentException.class);
        expectedException.expectMessage(String.format("Invalid environment name \"%s\"", stringArg));
        environmentFactory.createEnvironment();
    }

}