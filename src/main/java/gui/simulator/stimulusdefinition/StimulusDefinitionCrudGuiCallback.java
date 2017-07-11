/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.simulator.stimulusdefinition;

import java.util.List;

public interface StimulusDefinitionCrudGuiCallback {

    void afterDelete(List<StimulusDefinitionModel> models);

    void afterSave(StimulusDefinitionModel model);

}
