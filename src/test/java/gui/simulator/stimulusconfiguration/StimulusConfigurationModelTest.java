/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.simulator.stimulusconfiguration;

import gui.simulator.stimulusdefinition.StimulusDefinitionModel;
import org.junit.Test;
import test.RandomTestUtils;
import util.StringFormatter;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class StimulusConfigurationModelTest {

    @Test
    public void shouldCreateCorrectPrologClause() {
        String name = RandomTestUtils.randomString();
        String value = RandomTestUtils.randomString();
        double activation = RandomTestUtils.randomDouble();
        double satisfaction = RandomTestUtils.randomDouble();
        StimulusDefinitionModel model = new StimulusDefinitionModel(name, value, activation, satisfaction, true);
        StimulusConfigurationModel stimulusConfigurationModel = new StimulusConfigurationModel(model);

        assertThat(stimulusConfigurationModel.toClause(), is("stimulus(AGENT, " + value + ", " + StringFormatter.toString(activation) + ", " + StringFormatter.toString(satisfaction) + ") :- self(AGENT)."));
    }

}