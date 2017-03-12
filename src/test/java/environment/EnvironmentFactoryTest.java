/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment;

import environment.dummy.DummyEnvironment;
import environment.wikipedia.WikipediaEnvironment;
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
    public void setUp() {
        environmentFactory = new EnvironmentFactory();
    }

    @Test
    public void shouldCreateDummyEnvironment() {
        assertThat(environmentFactory.createEnvironment("dummy"), instanceOf(DummyEnvironment.class));
    }

    @Test
    public void shouldCreateWikipediaEnvironment() {
        assertThat(environmentFactory.createEnvironment("wikipedia"), instanceOf(WikipediaEnvironment.class));
    }

    @Test
    public void shouldThrowInvalidEnvironmentWhenEnvironmentNotExist() {
        String environment = "anything";
        expectedException.expect(InvalidEnvironmentException.class);
        expectedException.expectMessage(String.format("Invalid environment name \"%s\"", environment));
        environmentFactory.createEnvironment(environment);
    }

}