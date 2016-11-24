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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class BehaviourManagerTest {

    private BehaviourManager spyBehaviourManager;
    private Behaviour mockBehaviour;

    @Before
    public void setUp() {
        mockBehaviour = mock(Behaviour.class);
        spyBehaviourManager = spy(createDummyBehaviourManager(mockBehaviour));
    }

    @Test
    public void shouldReturnCorrectBehaviourAssociated() {
        assertThat(spyBehaviourManager.getBehaviourTypeAssociated(EmotionType.NEGATIVE_HIGH), is(BehaviourType.REACTIVE));
        assertThat(spyBehaviourManager.getBehaviourTypeAssociated(EmotionType.NEGATIVE_LOW), is(BehaviourType.COGNITIVE));
        assertThat(spyBehaviourManager.getBehaviourTypeAssociated(EmotionType.POSITIVE), is(BehaviourType.IMITATIVE));
    }

    @Test
    public void shouldUpdateBehaviour() {
        spyBehaviourManager.updateBehaviour(any());
        assertThat(spyBehaviourManager.getBehaviour(), is(mockBehaviour));
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