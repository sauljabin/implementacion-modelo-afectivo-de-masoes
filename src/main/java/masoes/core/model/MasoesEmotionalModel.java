/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core.model;

import masoes.core.BehaviourManager;
import masoes.core.EmotionalConfigurator;
import masoes.core.EmotionalModel;

public class MasoesEmotionalModel implements EmotionalModel {

    private MasoesEmotionalConfigurator masoesEmotionalConfigurator;
    private MasoesBehaviourManager masoesBehaviourManager;

    public MasoesEmotionalModel() {
        masoesEmotionalConfigurator = new MasoesEmotionalConfigurator();
        masoesBehaviourManager = new MasoesBehaviourManager();
    }

    @Override
    public EmotionalConfigurator getEmotionalConfigurator() {
        return masoesEmotionalConfigurator;
    }

    @Override
    public BehaviourManager getBehaviourManager() {
        return masoesBehaviourManager;
    }
}
