/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core;

import masoes.util.math.RandomGenerator;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;

public class EmotionalConfiguratorTest {

    private EmotionalConfigurator emotionalConfigurator;
    private EmotionalSpace emotionalSpace;
    private RandomGenerator random;
    private EmotionalState emotionalState;

    @Before
    public void setUp() {
        random = new RandomGenerator();
        emotionalState = new EmotionalState(random.getDouble(-1, 1), random.getDouble(-1, 1));
        emotionalConfigurator = createDummyEmotionalConfigurator(emotionalState);
    }

    @Test
    public void shouldReturnTheSameActivationAndSatisfaction() {
        emotionalConfigurator.updateEmotionalState(null);
        assertThat(emotionalConfigurator.getEmotionalState(), is(emotionalState));
    }

    @Test
    public void shouldReturnCorrectEmotion() {
        emotionalSpace = new EmotionalSpace();
        emotionalConfigurator.updateEmotionalState(null);
        Emotion expectedEmotion = emotionalSpace.searchEmotion(emotionalConfigurator.getEmotionalState());
        assertThat(emotionalConfigurator.getEmotion(), is(instanceOf(expectedEmotion.getClass())));
    }

    private EmotionalConfigurator createDummyEmotionalConfigurator(EmotionalState emotionalState) {
        return new EmotionalConfigurator() {
            @Override
            protected EmotionalState calculateEmotionalState(Stimulus stimulus) {
                return emotionalState;
            }
        };
    }

}