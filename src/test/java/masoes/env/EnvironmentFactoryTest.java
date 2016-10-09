/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.env;

import masoes.app.setting.Setting;
import masoes.env.generic.GenericEnvironment;
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

    @Before
    public void setUp() throws Exception {
        environmentFactory = new EnvironmentFactory();
    }

    @Test
    public void shouldCreateGenericEnvironment() {
        assertThat(environmentFactory.createEnvironment(), instanceOf(GenericEnvironment.class));
    }

    @Test
    public void shouldCreateWikipediaEnvironment() {
        Setting.MASOES_ENV.setValue("wikipedia");
        assertThat(environmentFactory.createEnvironment(), instanceOf(WikipediaEnvironment.class));
    }

    @Test
    public void shouldThrowInvalidParameterWhenNoExistValueException() {
        String stringArg = "anything";
        Setting.MASOES_ENV.setValue(stringArg);
        expectedException.expect(InvalidEnvironmentException.class);
        expectedException.expectMessage("Invalid environment name: " + stringArg);
        environmentFactory.createEnvironment();
    }

}