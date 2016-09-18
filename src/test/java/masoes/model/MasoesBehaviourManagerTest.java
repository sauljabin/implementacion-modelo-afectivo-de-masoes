/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.model;

import masoes.core.BehaviourType;
import masoes.core.EmotionType;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class MasoesBehaviourManagerTest {

    private MasoesBehaviourManager masoesBehaviourManager;

    @Before
    public void setUp() {
        masoesBehaviourManager = new MasoesBehaviourManager();
    }

    @Test
    public void shouldReturnCorrectBehaviourAssociated() {
        assertThat(masoesBehaviourManager.getBehaviourAssociated(EmotionType.NEGATIVE_HIGH), is(BehaviourType.REACTIVE));
        assertThat(masoesBehaviourManager.getBehaviourAssociated(EmotionType.NEGATIVE_LOW), is(BehaviourType.COGNITIVE));
        assertThat(masoesBehaviourManager.getBehaviourAssociated(EmotionType.POSITIVE), is(BehaviourType.IMITATIVE));
    }

}