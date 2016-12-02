/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core;

import jade.core.behaviours.Behaviour;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class BehaviourManagerTest {

    private BehaviourManager behaviourManager;
    private Behaviour mockBehaviour;

    @Before
    public void setUp() {
        mockBehaviour = mock(Behaviour.class);
        behaviourManager = createDummyBehaviourManager(mockBehaviour);
    }

    @Test
    public void shouldReturnCorrectBehaviourAssociated() {
        assertThat(behaviourManager.getBehaviourTypeAssociated(EmotionType.NEGATIVE_HIGH), is(BehaviourType.REACTIVE));
        assertThat(behaviourManager.getBehaviourTypeAssociated(EmotionType.NEGATIVE_LOW), is(BehaviourType.COGNITIVE));
        assertThat(behaviourManager.getBehaviourTypeAssociated(EmotionType.POSITIVE), is(BehaviourType.IMITATIVE));
    }

    @Test
    public void shouldUpdateBehaviour() {
        behaviourManager.updateBehaviour(mock(Emotion.class));
        assertThat(behaviourManager.getBehaviour(), is(mockBehaviour));
    }

    private BehaviourManager createDummyBehaviourManager(Behaviour Behaviour) {
        return new BehaviourManager() {
            @Override
            protected Behaviour calculateBehaviour(Emotion emotion) {
                return Behaviour;
            }
        };
    }

}