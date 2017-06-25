/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.configurator;

import agent.AgentLogger;
import environment.wikipedia.configurator.agent.AgentGuiListener;
import environment.wikipedia.configurator.agent.table.AgentTableModel;
import environment.wikipedia.configurator.stimulus.StimulusModel;
import environment.wikipedia.configurator.stimulus.StimulusGuiListener;
import environment.wikipedia.configurator.stimulus.table.StimulusTableModel;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import util.StringFormatter;

import javax.swing.*;

public class ConfiguratorGuiAgent extends GuiAgent {

    private ConfiguratorGuiListener configuratorGuiListener;
    private ConfiguratorGui configuratorGui;
    private StimulusTableModel stimulusTableModel;
    private AgentLogger logger;
    private AgentTableModel agentTableModel;

    public ConfiguratorGuiAgent() {
        logger = new AgentLogger(this);
        configuratorGui = new ConfiguratorGui();
        configuratorGuiListener = new ConfiguratorGuiListener(configuratorGui, this);
        configView();
        configuratorGui.setVisible(true);
    }

    private void configView() {
        stimulusTableModel = new StimulusTableModel(configuratorGui.getStimuliTable());
        stimulusTableModel.addStimulus(createStimulus("Incremento de reputación alta", .3, .3));
        stimulusTableModel.addStimulus(createStimulus("Incremento de reputación media", 0, .1));
        stimulusTableModel.addStimulus(createStimulus("Incremento de reputación baja", -.05, .05));
        stimulusTableModel.addStimulus(createStimulus("Decremento de reputación alta", -.3, -.3));
        stimulusTableModel.addStimulus(createStimulus("Decremento de reputación media", 0, -.1));
        stimulusTableModel.addStimulus(createStimulus("Decremento de reputación baja", -.05, -.05));

        agentTableModel = new AgentTableModel(configuratorGui.getAgentsTable());
    }

    private StimulusModel createStimulus(String name, double activation, double satisfaction) {
        return new StimulusModel(name, StringFormatter.toCamelCase(name), activation, satisfaction, true);
    }

    @Override
    protected void takeDown() {
        configuratorGui.dispose();
    }

    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {
        try {
            switch (ConfiguratorGuiEvent.fromInt(guiEvent.getType())) {
                case CLOSE_WINDOW:
                    closeWindow();
                    break;
                case ADD_STIMULUS:
                    addStimulus();
                    break;
                case DELETE_STIMULUS:
                    deleteStimulus();
                    break;
                case EDIT_STIMULUS:
                    editStimulus();
                    break;
                case ADD_AGENT:
                    addAgent();
                    break;
                case DELETE_AGENT:
                    deleteAgent();
                    break;
                case EDIT_AGENT:
                    editAgent();
                    break;
                case PLAY:
                    play();
                    break;
            }
        } catch (Exception e) {
            logger.exception(e);
            showError(e.getMessage());
        }
    }

    private void play() {
        addBehaviour(new ConfiguratorGuiAgentBehaviour(
                this,
                (Integer) configuratorGui.getIterationsSpinner().getValue()
        ));
    }

    private void editAgent() {
        if (agentTableModel.hasSelectedAgent()) {
            new AgentGuiListener(
                    agentTableModel.getSelectedAgent(),
                    agentTableModel.getAgents(),
                    stimulusTableModel.getStimuli(),
                    updatedAgent -> agentTableModel.fireTableDataChanged()
            );
        }
    }

    private void deleteAgent() {
        agentTableModel.deleteSelectedAgent();
    }

    private void addAgent() {
        new AgentGuiListener(
                agentTableModel.getAgents(),
                stimulusTableModel.getStimuli(),
                newAgent -> agentTableModel.addAgent(newAgent)
        );
    }

    private void editStimulus() {
        if (stimulusTableModel.hasSelectedStimulus()) {
            new StimulusGuiListener(
                    stimulusTableModel.getSelectedStimulus(),
                    updatedStimulus -> stimulusTableModel.fireTableDataChanged()
            );
        }
    }

    private void deleteStimulus() {
        stimulusTableModel.deleteSelectedStimuli();
    }

    private void addStimulus() {
        new StimulusGuiListener(
                newStimulus -> stimulusTableModel.addStimulus(newStimulus)
        );
    }

    private void closeWindow() {
        doDelete();
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(configuratorGui, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public ConfiguratorGui getConfiguratorGui() {
        return configuratorGui;
    }

    public StimulusTableModel getStimulusTableModel() {
        return stimulusTableModel;
    }

    public AgentTableModel getAgentTableModel() {
        return agentTableModel;
    }

}
