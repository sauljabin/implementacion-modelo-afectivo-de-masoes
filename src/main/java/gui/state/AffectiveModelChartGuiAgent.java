/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.state;

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

import javax.swing.*;

public class AffectiveModelChartGuiAgent extends GuiAgent {

    private static final int FPS = 5;
    private OntologyAssistant masoesOntologyAssistant;
    private AffectiveModelChartGui affectiveModelChartGui;
    private AgentLogger logger;
    private Behaviour emotionalStateAgentBehaviour;
    private AffectiveModelChartGuiListener affectiveModelChartGuiListener;

    public AffectiveModelChartGuiAgent() {
        affectiveModelChartGui = new AffectiveModelChartGui();
        affectiveModelChartGuiListener = new AffectiveModelChartGuiListener(this, affectiveModelChartGui);
        logger = new AgentLogger(this);
        masoesOntologyAssistant = new OntologyAssistant(this, MasoesOntology.getInstance());
    }

    @Override
    protected void setup() {
        if (!hasArgument()) {
            String message = getLocalName() + " no has argument: emotional agent name is necessary";
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
            throw new AgentException(message);
        }

        String agentName = getEmotionalAgentName();

        emotionalStateAgentBehaviour = new CyclicBehaviour() {
            @Override
            public void action() {
                AgentState agentState = (AgentState) masoesOntologyAssistant.sendRequestAction(getAID(agentName), new GetEmotionalState());
                affectiveModelChartGui.setAgentState(agentState);
                try {
                    Thread.sleep(1000 / FPS);
                } catch (InterruptedException e) {
                }
            }
        };
        addBehaviour(emotionalStateAgentBehaviour);
        affectiveModelChartGui.showGui();
    }

    private String getEmotionalAgentName() {
        return (String) getArguments()[0];
    }

    private boolean hasArgument() {
        return getArguments() != null && getArguments().length == 1 && (getArguments()[0] instanceof String);
    }

    @Override
    protected void takeDown() {
        affectiveModelChartGui.closeGui();
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
