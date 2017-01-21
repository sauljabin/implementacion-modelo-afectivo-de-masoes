/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.env;

import application.settings.ApplicationSettings;
import masoes.env.dummy.DummyEnvironment;
import masoes.env.exception.InvalidEnvironmentException;
import masoes.env.wikipedia.WikipediaEnvironment;
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
    private ApplicationSettings mockApplicationSettings;

    @Before
    public void setUp() throws Exception {
        mockApplicationSettings = mock(ApplicationSettings.class);
        environmentFactory = new EnvironmentFactory();
        setFieldValue(environmentFactory, "applicationSettings", mockApplicationSettings);
    }

    @Test
    public void shouldCreateDummyEnvironment() {
        doReturn("dummy").when(mockApplicationSettings).get(ApplicationSettings.MASOES_ENV);
        assertThat(environmentFactory.createEnvironment(), instanceOf(DummyEnvironment.class));
    }

    @Test
    public void shouldCreateWikipediaEnvironment() {
        doReturn("wikipedia").when(mockApplicationSettings).get(ApplicationSettings.MASOES_ENV);
        assertThat(environmentFactory.createEnvironment(), instanceOf(WikipediaEnvironment.class));
    }

    @Test
    public void shouldThrowInvalidParameterWhenNoExistValueException() {
        String stringArg = "anything";
        doReturn(stringArg).when(mockApplicationSettings).get(ApplicationSettings.MASOES_ENV);
        expectedException.expect(InvalidEnvironmentException.class);
        expectedException.expectMessage(String.format("Invalid environment name \"%s\"", stringArg));
        environmentFactory.createEnvironment();
    }

}