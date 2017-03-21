/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.dummy;

import knowledge.Knowledge;
import masoes.agent.EmotionalAgent;
import masoes.component.behavioural.EmotionalState;
import util.StringValidator;

import java.nio.file.Paths;

public class DummyEmotionalAgent extends EmotionalAgent {

    private static final String THEORY = "theories/behavioural/dummy/dummyEmotionalAgent.prolog";

    @Override
    public void setUp() {
        if (hasArgs() && argsAreReal()) {
            double activation = Double.parseDouble((String) getArguments()[0]);
            double satisfaction = Double.parseDouble((String) getArguments()[1]);

            EmotionalState emotionalState = new EmotionalState(activation, satisfaction);
            getBehaviouralComponent().setEmotionalState(emotionalState);
        }
        getBehaviouralComponent().addKnowledge(new Knowledge(Paths.get(THEORY)));
    }

    private boolean argsAreReal() {
        return (getArguments()[0] instanceof String) && (getArguments()[1] instanceof String) && StringValidator.isReal((String) getArguments()[0]) && StringValidator.isReal((String) getArguments()[1]);
    }

    private boolean hasArgs() {
        return getArguments() != null && getArguments().length == 2;
    }

}
