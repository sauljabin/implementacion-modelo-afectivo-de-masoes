/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.behavioural;

import masoes.agent.EmotionalAgent;
import masoes.ontology.stimulus.Stimulus;
import util.ToStringBuilder;

public class BehaviouralComponent {

    private BehaviourManager behaviourManager;
    private EmotionalConfigurator emotionalConfigurator;
    private BehaviouralKnowledgeBase behaviouralKnowledgeBase;

    public BehaviouralComponent(EmotionalAgent emotionalAgent) {
        behaviouralKnowledgeBase = new BehaviouralKnowledgeBase(emotionalAgent);
        emotionalConfigurator = new EmotionalConfigurator(behaviouralKnowledgeBase);
        behaviourManager = new BehaviourManager(emotionalConfigurator, behaviouralKnowledgeBase);
    }

    public Emotion getCurrentEmotion() {
        return emotionalConfigurator.getEmotion();
    }

    public BehaviourType getCurrentBehaviourType() {
        return behaviourManager.getBehaviourType();
    }

    public EmotionalState getCurrentEmotionalState() {
        return emotionalConfigurator.getEmotionalState();
    }

    public void evaluateStimulus(Stimulus stimulus) {
        emotionalConfigurator.updateEmotion(stimulus);
        behaviourManager.updateBehaviour();
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("behaviourManager", behaviourManager)
                .append("emotionalConfigurator", emotionalConfigurator)
                .append("behaviouralKnowledgeBase", behaviouralKnowledgeBase)
                .toString();
    }

    public void setEmotionalState(EmotionalState emotionalState) {
        emotionalConfigurator.setEmotionalState(emotionalState);
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
