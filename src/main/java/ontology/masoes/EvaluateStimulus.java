/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package ontology.masoes;

import jade.content.AgentAction;
import util.ToStringBuilder;

public class EvaluateStimulus implements AgentAction {

    private Stimulus stimulus;

    public EvaluateStimulus() {
    }

    public EvaluateStimulus(Stimulus stimulus) {
        this.stimulus = stimulus;
    }

    public Stimulus getStimulus() {
        return stimulus;
    }

    public void setStimulus(Stimulus stimulus) {
        this.stimulus = stimulus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("stimulus", stimulus)
                .toString();
    }

}
