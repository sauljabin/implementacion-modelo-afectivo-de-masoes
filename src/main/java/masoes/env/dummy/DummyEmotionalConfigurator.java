/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.env.dummy;

import masoes.core.EmotionalConfigurator;
import masoes.core.EmotionalState;
import masoes.core.Stimulus;
import masoes.util.math.RandomGenerator;

public class DummyEmotionalConfigurator extends EmotionalConfigurator {

    @Override
    protected EmotionalState calculateEmotionalState(Stimulus stimulus) {
        RandomGenerator randomGenerator = new RandomGenerator();
        return new EmotionalState(randomGenerator.getDouble(-1, 1), randomGenerator.getDouble(-1, 1));
    }

}
