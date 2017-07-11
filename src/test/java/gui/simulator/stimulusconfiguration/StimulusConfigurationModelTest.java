/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.simulator.stimulusconfiguration;

import gui.simulator.stimulusdefinition.StimulusDefinitionModel;
import org.junit.Test;
import test.RandomUtil;
import util.StringFormatter;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class StimulusConfigurationModelTest {

    @Test
    public void shouldCreateCorrectPrologClause() {
        String name = RandomUtil.randomString();
        String value = RandomUtil.randomString();
        double activation = RandomUtil.randomDouble();
        double satisfaction = RandomUtil.randomDouble();
        StimulusDefinitionModel model = new StimulusDefinitionModel(name, value, activation, satisfaction, true);
        StimulusConfigurationModel stimulusConfigurationModel = new StimulusConfigurationModel(model);

        assertThat(stimulusConfigurationModel.toClause(), is("stimulus(AGENT, " + value + ", " + StringFormatter.toString(activation) + ", " + StringFormatter.toString(satisfaction) + ") :- self(AGENT)."));
    }

}