/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.behavioural;

// TODO: UNIT-TEST

import masoes.EmotionalAgent;
import masoes.EmotionalBehaviour;
import ontology.masoes.Stimulus;
import util.ToStringBuilder;

public class BehaviouralComponent {

    private BehaviourManager behaviourManager;
    private EmotionalConfigurator emotionalConfigurator;
    private BehaviouralKnowledgeBase behaviouralKnowledgeBase;
    private EmotionalAgent emotionalAgent;

    public BehaviouralComponent(EmotionalAgent emotionalAgent) {
        this.emotionalAgent = emotionalAgent;

        String agentName = "X";
        if (emotionalAgent.getLocalName() != null) {
            agentName = emotionalAgent.getLocalName().toLowerCase();
        }

        behaviouralKnowledgeBase = new BehaviouralKnowledgeBase(agentName, emotionalAgent.getKnowledgePath());
        emotionalConfigurator = new EmotionalConfigurator(behaviouralKnowledgeBase);

        behaviourManager = new BehaviourManager(behaviouralKnowledgeBase);
        behaviourManager.updateBehaviour(emotionalAgent, emotionalConfigurator.getEmotion());
    }

    public Emotion getCurrentEmotion() {
        return emotionalConfigurator.getEmotion();
    }

    public EmotionalBehaviour getCurrentEmotionalBehaviour() {
        return behaviourManager.getBehaviour();
    }

    public EmotionalState getEmotionalState() {
        return emotionalConfigurator.getEmotionalState();
    }

    public void evaluateStimulus(Stimulus stimulus) {
        emotionalConfigurator.updateEmotion(stimulus);
        behaviourManager.updateBehaviour(emotionalAgent, emotionalConfigurator.getEmotion());
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("behaviourManager", behaviourManager)
                .append("emotionalConfigurator", emotionalConfigurator)
                .append("behaviouralKnowledgeBase", behaviouralKnowledgeBase)
                .toString();
    }

    public BehaviourManager getBehaviourManager() {
        return behaviourManager;
    }

    public EmotionalConfigurator getEmotionalConfigurator() {
        return emotionalConfigurator;
    }

    public BehaviouralKnowledgeBase getBehaviouralKnowledgeBase() {
        return behaviouralKnowledgeBase;
    }

}
