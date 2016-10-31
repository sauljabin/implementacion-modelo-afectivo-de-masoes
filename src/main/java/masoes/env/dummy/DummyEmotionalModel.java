/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.env.dummy;

import masoes.core.BehaviourManager;
import masoes.core.EmotionalConfigurator;
import masoes.core.EmotionalModel;

public class DummyEmotionalModel extends EmotionalModel {

    @Override
    public EmotionalConfigurator getEmotionalConfigurator() {
        return new DummyEmotionalConfigurator();
    }

    @Override
    public BehaviourManager getBehaviourManager() {
        return new DummyBehaviourManager();
    }
}
