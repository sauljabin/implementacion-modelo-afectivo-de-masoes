/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.env.dummy;

import masoes.core.EmotionalState;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

public class DummyEmotionalConfiguratorTest {

    private DummyEmotionalConfigurator dummyEmotionalConfigurator;

    @Before
    public void setUp() {
        dummyEmotionalConfigurator = new DummyEmotionalConfigurator();
    }

    @Test
    public void shouldReturnRandomEmotionalState() {
        EmotionalState firstEmotionalState = dummyEmotionalConfigurator.calculateEmotionalState(null);
        EmotionalState secondEmotionalState = dummyEmotionalConfigurator.calculateEmotionalState(null);
        assertThat(firstEmotionalState.getActivation(), is(not(secondEmotionalState.getActivation())));
        assertThat(firstEmotionalState.getSatisfaction(), is(not(secondEmotionalState.getSatisfaction())));
    }

}