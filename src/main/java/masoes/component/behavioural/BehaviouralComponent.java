/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.component.behavioural;

import knowledge.Knowledge;
import knowledge.KnowledgeException;
import masoes.agent.EmotionalAgent;
import masoes.ontology.stimulus.Stimulus;
import util.ToStringBuilder;

public class BehaviouralComponent {

    private BehaviourManager behaviourManager;
    private EmotionalConfigurator emotionalConfigurator;
    private BehaviouralKnowledgeBase behaviouralKnowledgeBase;

    public BehaviouralComponent(EmotionalAgent emotionalAgent) {

        if (emotionalAgent.getLocalName() == null) {
            throw new KnowledgeException("No agent name, create component in setup agent method");
        }

        behaviouralKnowledgeBase = new BehaviouralKnowledgeBase(emotionalAgent.getLocalName());
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

    public void addKnowledge(Knowledge knowledge) {
        behaviouralKnowledgeBase.addKnowledge(knowledge);
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
