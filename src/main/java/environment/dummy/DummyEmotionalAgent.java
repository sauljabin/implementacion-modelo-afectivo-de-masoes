/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.dummy;

import knowledge.Knowledge;
import masoes.agent.EmotionalAgent;

import java.nio.file.Paths;

public class DummyEmotionalAgent extends EmotionalAgent {

    private static final String THEORY = "theories/behavioural/dummy/dummyEmotionalAgent.prolog";

    @Override
    public void setUp() {
        getBehaviouralComponent().addKnowledge(new Knowledge(Paths.get(THEORY)));
    }

}
