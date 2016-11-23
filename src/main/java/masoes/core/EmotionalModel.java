/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core;

public class EmotionalModel {

    private EmotionalConfigurator emotionalConfigurator;
    private BehaviourManager behaviourManager;

    public EmotionalModel(EmotionalConfigurator emotionalConfigurator, BehaviourManager behaviourManager) {
        this.emotionalConfigurator = emotionalConfigurator;
        this.behaviourManager = behaviourManager;
    }

    public EmotionalConfigurator getEmotionalConfigurator() {
        return emotionalConfigurator;
    }

    public BehaviourManager getBehaviourManager() {
        return behaviourManager;
    }

}
