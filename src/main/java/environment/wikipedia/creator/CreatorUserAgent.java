/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.creator;

import environment.dummy.DummyCognitiveBehaviour;
import environment.dummy.DummyImitativeBehaviour;
import environment.dummy.DummyReactiveBehaviour;
import jade.core.behaviours.CyclicBehaviour;
import knowledge.Knowledge;
import masoes.CognitiveBehaviour;
import masoes.EmotionalAgent;
import masoes.ImitativeBehaviour;
import masoes.ReactiveBehaviour;

import java.nio.file.Paths;

public class CreatorUserAgent extends EmotionalAgent {

    private CreatorUserAgentGui creatorUserAgentGui;

    @Override
    public void setUp() {
        creatorUserAgentGui = new CreatorUserAgentGui();
        creatorUserAgentGui.setAgentName(getLocalName());
        creatorUserAgentGui.showGui();
        addBehaviour(new CyclicBehaviour() {
            private static final int FPS = 10;

            @Override
            public void action() {
                creatorUserAgentGui.setBehaviour(getBehaviouralComponent().getCurrentEmotionalBehaviour());
                creatorUserAgentGui.setEmotion(getBehaviouralComponent().getCurrentEmotion());
                creatorUserAgentGui.addEmotionalState(getBehaviouralComponent().getCurrentEmotionalState());
                try {
                    Thread.sleep(1000 / FPS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
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
    public synchronized void log(String message) {
        super.log(message);
        creatorUserAgentGui.logEvent(message);
    }

}
