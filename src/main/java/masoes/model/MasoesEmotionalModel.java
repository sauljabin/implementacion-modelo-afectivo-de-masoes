/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.model;

import masoes.core.BehaviorManager;
import masoes.core.EmotionalConfigurator;
import masoes.core.EmotionalModel;

public class MasoesEmotionalModel implements EmotionalModel {

    private MasoesEmotionalConfigurator masoesEmotionalConfigurator;
    private MasoesBehaviorManager masoesBehaviorManager;

    public MasoesEmotionalModel() {
        masoesEmotionalConfigurator = new MasoesEmotionalConfigurator();
        masoesBehaviorManager = new MasoesBehaviorManager();
    }

    @Override
    public EmotionalConfigurator getEmotionalConfigurator() {
        return masoesEmotionalConfigurator;
    }

    @Override
    public BehaviorManager getBehaviorManager() {
        return masoesBehaviorManager;
    }
}
