/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes;

import jade.core.Agent;
import masoes.behavioural.BehaviouralComponent;
import util.ToStringBuilder;

public abstract class EmotionalAgent extends Agent {

    private BehaviouralComponent behaviouralComponent;

    @Override
    protected final void setup() {
        setUp();
        behaviouralComponent = new BehaviouralComponent(this);
        addBehaviour(new BasicEmotionalAgentBehaviour(this));
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("aid", getAID())
                .append("behaviouralComponent", behaviouralComponent)
                .toString();
    }

    public BehaviouralComponent getBehaviouralComponent() {
        return behaviouralComponent;
    }

    public abstract void setUp();

    public abstract String getKnowledgePath();

    public abstract ImitativeBehaviour getImitativeBehaviour();

    public abstract ReactiveBehaviour getReactiveBehaviour();

    public abstract CognitiveBehaviour getCognitiveBehaviour();

}
