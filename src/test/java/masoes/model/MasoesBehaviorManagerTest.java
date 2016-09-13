/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.model;

import masoes.core.BehaviorType;
import masoes.core.EmotionType;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class MasoesBehaviorManagerTest {

    private MasoesBehaviorManager masoesBehaviorManager;

    @Before
    public void setUp() {
        masoesBehaviorManager = new MasoesBehaviorManager();
    }

    @Test
    public void shouldReturnCorrectBehaviorAssociated() {
        assertThat(masoesBehaviorManager.getBehaviorAssociated(EmotionType.NEGATIVE_HIGH), is(BehaviorType.REACTIVE));
        assertThat(masoesBehaviorManager.getBehaviorAssociated(EmotionType.NEGATIVE_LOW), is(BehaviorType.COGNITIVE));
        assertThat(masoesBehaviorManager.getBehaviorAssociated(EmotionType.POSITIVE), is(BehaviorType.IMITATIVE));
    }

}