/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class EmotionTypeTest {

    @Test
    public void shouldGetCorrectBehaviorType() {
        assertThat(EmotionType.POSITIVE.getBehaviorType(), is(BehaviorType.IMITATIVE));
        assertThat(EmotionType.NEGATIVE_LOW.getBehaviorType(), is(BehaviorType.COGNITIVE));
        assertThat(EmotionType.NEGATIVE_HIGH.getBehaviorType(), is(BehaviorType.REACTIVE));
    }

}
