/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.behavioural;

import com.vividsolutions.jts.geom.Point;
import org.junit.Test;
import util.RandomGenerator;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class EmotionalStateTest {

    @Test
    public void shouldCreatePoint() {
        RandomGenerator random = new RandomGenerator();
        testEmotionalState(new EmotionalState(random.getDouble(0, 1), random.getDouble(0, 1)));
    }

    @Test
    public void shouldConstructEmotionalStateWithRandomValues() {
        testEmotionalState(new EmotionalState());
    }

    private void testEmotionalState(EmotionalState emotionalState) {
        Point actualPoint = emotionalState.toPoint();
        assertThat(actualPoint.getX(), is(emotionalState.getActivation()));
        assertThat(actualPoint.getY(), is(emotionalState.getSatisfaction()));
    }

}