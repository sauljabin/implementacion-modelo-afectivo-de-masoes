/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.submitter;

import environment.dummy.DummyCognitiveBehaviour;
import environment.dummy.DummyImitativeBehaviour;
import environment.dummy.DummyReactiveBehaviour;
import jade.core.behaviours.CyclicBehaviour;
import knowledge.Knowledge;
import masoes.CognitiveBehaviour;
import masoes.EmotionalAgent;
import masoes.ImitativeBehaviour;
import masoes.ReactiveBehaviour;
import org.slf4j.event.Level;

import java.nio.file.Paths;

public class SubmitterUserAgent extends EmotionalAgent {

    private SubmitterUserAgentGui submitterUserAgentGui;

    @Override
    public void setUp() {
        submitterUserAgentGui = new SubmitterUserAgentGui();
        submitterUserAgentGui.setAgentName(getLocalName());
        submitterUserAgentGui.showGui();
        addBehaviour(new CyclicBehaviour() {
            private static final int FPS = 10;

            @Override
            public void action() {
                submitterUserAgentGui.setBehaviour(getBehaviouralComponent().getCurrentEmotionalBehaviour());
                submitterUserAgentGui.setEmotion(getBehaviouralComponent().getCurrentEmotion());
                submitterUserAgentGui.addEmotionalState(getBehaviouralComponent().getCurrentEmotionalState());
                block(1000 / FPS);
            }
        });
    }

    @Override
    protected void takeDown() {
        submitterUserAgentGui.closeGui();
    }

    @Override
    public Knowledge getKnowledge() {
        return new Knowledge(Paths.get("theories/dummy/dummyEmotionalAgent.prolog"));
    }

    @Override
    public ImitativeBehaviour getImitativeBehaviour() {
        return new DummyImitativeBehaviour();
    }

    @Override
    public ReactiveBehaviour getReactiveBehaviour() {
        return new DummyReactiveBehaviour();
    }

    @Override
    public CognitiveBehaviour getCognitiveBehaviour() {
        return new DummyCognitiveBehaviour();
    }

    @Override
    public void handleMessage(Level level, String message) {
        submitterUserAgentGui.logEvent(message);
    }

}
