/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.state;

import agent.AgentException;
import agent.AgentLogger;
import environment.wikipedia.configurator.ConfiguratorAgentEvent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import masoes.ontology.MasoesOntology;
import masoes.ontology.state.AgentState;
import masoes.ontology.state.GetEmotionalState;
import ontology.OntologyAssistant;

public class EmotionalStateAgent extends GuiAgent {

    private static final int FPS = 10;
    private OntologyAssistant masoesOntologyAssistant;
    private EmotionalStateAgentGui emotionalStateAgentGui;
    private AgentLogger logger;
    private Behaviour emotionalStateAgentBehaviour;
    private EmotionalStateAgentListener emotionalStateAgentListener;

    public EmotionalStateAgent() {
        emotionalStateAgentGui = new EmotionalStateAgentGui();
        emotionalStateAgentListener = new EmotionalStateAgentListener(this, emotionalStateAgentGui);
        logger = new AgentLogger(this);
        masoesOntologyAssistant = new OntologyAssistant(this, MasoesOntology.getInstance());
    }

    @Override
    protected void setup() {

        if (!hasArgument()) {
            throw new AgentException(getLocalName() + " no has argument");
        }

        String agentName = (String) getArguments()[0];

        emotionalStateAgentBehaviour = new CyclicBehaviour() {
            @Override
            public void action() {
                AgentState agentState = (AgentState) masoesOntologyAssistant.sendRequestAction(getAID(agentName), new GetEmotionalState());
                emotionalStateAgentGui.setAgentState(agentState);
                try {
                    Thread.sleep(1000 / FPS);
                } catch (InterruptedException e) {
                }
            }
        };
        addBehaviour(emotionalStateAgentBehaviour);
        emotionalStateAgentGui.showGui();
    }

    private boolean hasArgument() {
        return getArguments() != null && getArguments().length == 1 && (getArguments()[0] instanceof String);
    }

    @Override
    protected void takeDown() {
        emotionalStateAgentGui.closeGui();
    }

    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {
        try {
            switch (ConfiguratorAgentEvent.fromInt(guiEvent.getType())) {
                case CLOSE_WINDOW:
                    doDelete();
                    break;
            }
        } catch (Exception e) {
            logger.exception(e);
        }
    }

}
