/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.submitter;

import jade.core.behaviours.CyclicBehaviour;
import knowledge.Knowledge;
import masoes.agent.EmotionalAgent;
import masoes.behavioural.BehaviourType;

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
                submitterUserAgentGui.setBehaviour(BehaviourType.IMITATIVE);
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
    public void actionPerformed(ActionEvent e) {

    }

}
