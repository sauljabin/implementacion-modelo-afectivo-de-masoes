/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.env;

import masoes.app.settings.ApplicationSettings;
import masoes.env.dummy.DummyEnvironment;
import masoes.env.wikipedia.WikipediaEnvironment;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public class EnvironmentFactoryTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private EnvironmentFactory environmentFactory;
    private ApplicationSettings applicationSettings;

    @Before
    public void setUp() {
        applicationSettings = ApplicationSettings.getInstance();
        applicationSettings.load();
        environmentFactory = new EnvironmentFactory();
    }

    @Test
    public void shouldCreateDummyEnvironment() {
        assertThat(environmentFactory.createEnvironment(), instanceOf(DummyEnvironment.class));
    }

    @Test
    public void shouldCreateWikipediaEnvironment() {
        applicationSettings.set(ApplicationSettings.MASOES_ENV, "wikipedia");
        assertThat(environmentFactory.createEnvironment(), instanceOf(WikipediaEnvironment.class));
    }

    @Test
    public void shouldThrowInvalidParameterWhenNoExistValueException() {
        String stringArg = "anything";
        applicationSettings.set(ApplicationSettings.MASOES_ENV, stringArg);
        expectedException.expect(InvalidEnvironmentException.class);
        expectedException.expectMessage(String.format("Invalid environment name \"%s\"", stringArg));
        environmentFactory.createEnvironment();
    }

}