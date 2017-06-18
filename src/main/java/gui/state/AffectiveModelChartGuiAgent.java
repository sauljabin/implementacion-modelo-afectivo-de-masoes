/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.state;

import agent.AgentException;
import agent.AgentLogger;
import environment.wikipedia.configurator.ConfiguratorAgentEvent;
import jade.JadeSettings;
import jade.core.AID;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;

import javax.swing.*;

public class AffectiveModelChartGuiAgent extends GuiAgent {

    private AffectiveModelChartGui affectiveModelChartGui;
    private AgentLogger logger;
    private AffectiveModelChartGuiListener affectiveModelChartGuiListener;

    public AffectiveModelChartGuiAgent() {
        affectiveModelChartGui = new AffectiveModelChartGui();
        affectiveModelChartGuiListener = new AffectiveModelChartGuiListener(this, affectiveModelChartGui);
        logger = new AgentLogger(this);
    }

    @Override
    protected void setup() {
        if (!isGUIEnabled()) {
            throw new AgentException(getLocalName() + ": gui option is disabled");
        }

        if (!hasArgument()) {
            String message = getLocalName() + " no has argument: emotional agent name is necessary";
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
            throw new AgentException(message);
        }

        addBehaviour(new AffectiveModelChartGuiAgentBehaviour(this));
        affectiveModelChartGui.showGui();
    }

    private boolean isGUIEnabled() {
        return Boolean.parseBoolean(JadeSettings.getInstance().get(JadeSettings.GUI));
    }

    public AffectiveModelChartGui getAffectiveModelChartGui() {
        return affectiveModelChartGui;
    }

    public String getEmotionalAgentName() {
        return (String) getArguments()[0];
    }

    public AID getEmotionalAgentAID() {
        return getAID(getEmotionalAgentName());
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
