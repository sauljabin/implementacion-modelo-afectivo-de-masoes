/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.behavioural;

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
        behaviouralKnowledgeBase = new BehaviouralKnowledgeBase(emotionalAgent);
        emotionalConfigurator = new EmotionalConfigurator(emotionalAgent, behaviouralKnowledgeBase);
        behaviourManager = new BehaviourManager(emotionalAgent, emotionalConfigurator, behaviouralKnowledgeBase);
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
