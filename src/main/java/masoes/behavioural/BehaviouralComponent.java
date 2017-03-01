/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.behavioural;

import masoes.EmotionalAgent;
import masoes.EmotionalBehaviour;
import ontology.masoes.Stimulus;
import util.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class BehaviouralComponent {

    private BehaviourManager behaviourManager;
    private EmotionalConfigurator emotionalConfigurator;
    private BehaviouralKnowledgeBase behaviouralKnowledgeBase;
    private EmotionalAgent emotionalAgent;
    private Stimulus lastStimulus;
    private List<EmotionalState> emotionalStatesRecords;

    public BehaviouralComponent(EmotionalAgent emotionalAgent) {
        if (emotionalAgent.getLocalName() == null) {
            throw new BehaviouralComponentException("No agent name, create in setup agent method");
        }
        this.emotionalAgent = emotionalAgent;
        emotionalStatesRecords = new ArrayList<>();
        behaviouralKnowledgeBase = new BehaviouralKnowledgeBase(emotionalAgent.getLocalName(), emotionalAgent.getKnowledgePath());
        emotionalConfigurator = new EmotionalConfigurator(behaviouralKnowledgeBase);
        behaviourManager = new BehaviourManager(behaviouralKnowledgeBase);
        behaviourManager.updateBehaviour(emotionalAgent, emotionalConfigurator.getEmotion());
        registerEmotionalState();
    }

    private void registerEmotionalState() {
        EmotionalState emotionalState = emotionalConfigurator.getEmotionalState();
        emotionalStatesRecords.add(new EmotionalState(emotionalState.getActivation(), emotionalState.getSatisfaction()));
    }

    public Emotion getCurrentEmotion() {
        return emotionalConfigurator.getEmotion();
    }

    public EmotionalBehaviour getCurrentEmotionalBehaviour() {
        return behaviourManager.getBehaviour();
    }

    public EmotionalState getCurrentEmotionalState() {
        return emotionalConfigurator.getEmotionalState();
    }

    public void evaluateStimulus(Stimulus stimulus) {
        emotionalConfigurator.updateEmotion(stimulus);
        behaviourManager.updateBehaviour(emotionalAgent, emotionalConfigurator.getEmotion());
        lastStimulus = stimulus;
        registerEmotionalState();
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

    public Stimulus getLastStimulus() {
        return lastStimulus;
    }

    public List<EmotionalState> getEmotionalStatesRecords() {
        return emotionalStatesRecords;
    }

}
