/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.state;

import agent.AgentLogger;
import environment.wikipedia.configurator.ConfiguratorAgentEvent;
import jade.AgentException;
import jade.core.behaviours.CyclicBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import masoes.ontology.MasoesOntology;
import masoes.ontology.state.AgentState;
import masoes.ontology.state.GetEmotionalState;
import ontology.OntologyAssistant;

public class EmotionalStateAgent extends GuiAgent {

    private OntologyAssistant masoesOntologyAssistant;
    private EmotionalStateAgentGui emotionalStateAgentGui;
    private AgentLogger logger;

    public EmotionalStateAgent() {
        emotionalStateAgentGui = new EmotionalStateAgentGui();
        logger = new AgentLogger(this);
        masoesOntologyAssistant = new OntologyAssistant(this, MasoesOntology.getInstance());
    }

    @Override
    protected void setup() {

        if (!hasArgument()) {
            throw new AgentException(getLocalName() + " no has argument");
        }

        String agentName = (String) getArguments()[0];

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                AgentState agentState = (AgentState) masoesOntologyAssistant.sendRequestAction(getAID(agentName), new GetEmotionalState());
                emotionalStateAgentGui.setAgentState(agentState);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
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
