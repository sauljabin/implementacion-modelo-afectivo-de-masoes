/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Paths;

public class SubmitterUserAgent extends EmotionalAgent implements ActionListener {

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
        return new Knowledge(Paths.get("theories/behavioural/wikipedia/submitterEmotionalAgent.prolog"));
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
    public void actionPerformed(ActionEvent e) {

    }

}
