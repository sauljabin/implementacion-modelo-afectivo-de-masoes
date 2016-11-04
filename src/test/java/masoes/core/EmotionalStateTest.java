/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core;

import com.vividsolutions.jts.geom.Point;
import masoes.util.math.RandomGenerator;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class EmotionalStateTest {

    private RandomGenerator random;
    private EmotionalState emotionalState;

    @Before
    public void setUp() {
        random = new RandomGenerator();
        emotionalState = new EmotionalState(random.getDouble(0, 1), random.getDouble(0, 1));
    }

    @Test
    public void shouldReturnCorrectGeometryPoint() {
        Point actualPoint = emotionalState.toPoint();
        assertThat(actualPoint.getX(), is(emotionalState.getActivation()));
        assertThat(actualPoint.getY(), is(emotionalState.getSatisfaction()));
    }

}