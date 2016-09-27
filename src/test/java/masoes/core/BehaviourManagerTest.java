/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class BehaviourManagerTest {

    private BehaviourManager behaviourManager;

    @Before
    public void setUp() {
        behaviourManager = new BehaviourManager() {
        };
    }

    @Test
    public void shouldReturnCorrectBehaviourAssociated() {
        assertThat(behaviourManager.getBehaviourAssociated(EmotionType.NEGATIVE_HIGH), is(BehaviourType.REACTIVE));
        assertThat(behaviourManager.getBehaviourAssociated(EmotionType.NEGATIVE_LOW), is(BehaviourType.COGNITIVE));
        assertThat(behaviourManager.getBehaviourAssociated(EmotionType.POSITIVE), is(BehaviourType.IMITATIVE));
    }

}