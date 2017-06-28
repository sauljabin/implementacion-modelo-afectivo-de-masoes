/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.configurator;

import agent.AgentLogger;
import agent.AgentManagementAssistant;
import environment.dummy.DummyEmotionalAgentArgumentsBuilder;
import environment.wikipedia.chart.centralEmotion.CentralEmotionChartGui;
import environment.wikipedia.chart.emotionalDispersion.EmotionalDispersionChartGui;
import environment.wikipedia.chart.maximunDistance.MaximumDistanceChartGui;
import environment.wikipedia.configurator.agent.AgentGuiListener;
import environment.wikipedia.configurator.agent.table.AgentStateTableModel;
import environment.wikipedia.configurator.agent.table.AgentTableModel;
import environment.wikipedia.configurator.stimulus.StimulusGuiListener;
import environment.wikipedia.configurator.stimulus.StimulusModel;
import environment.wikipedia.configurator.stimulus.table.StimulusTableModel;
import gui.state.AffectiveModelChartGuiAgent;
import jade.core.AID;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import masoes.ontology.state.AgentState;
import translate.Translation;
import util.StringFormatter;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConfiguratorGuiAgent extends GuiAgent {

    private static final String CENTRAL_EMOTION = Translation.getInstance().get("gui.central_emotion");

    private AgentManagementAssistant assistant;
    private ConfiguratorGuiListener configuratorGuiListener;
    private ConfiguratorGui configuratorGui;
    private StimulusTableModel stimulusTableModel;
    private AgentLogger logger;
    private AgentTableModel agentTableModel;
    private ConfiguratorGuiAgentBehaviour agentBehaviour;
    private AgentStateTableModel agentStateTableModel;

    private MaximumDistanceChartGui maximumDistanceChart;
    private CentralEmotionChartGui centralEmotionChart;
    private EmotionalDispersionChartGui emotionalDispersion;

    private boolean suspended;
    private boolean started;

    public ConfiguratorGuiAgent() {
        logger = new AgentLogger(this);
        assistant = new AgentManagementAssistant(this);
        configuratorGui = new ConfiguratorGui();
        configuratorGuiListener = new ConfiguratorGuiListener(configuratorGui, this);

        centralEmotionChart = new CentralEmotionChartGui(() ->
                configuratorGui.getCentralEmotionCheckBox().setSelected(false)
        );

        maximumDistanceChart = new MaximumDistanceChartGui(() ->
                configuratorGui.getMaximumDistanceCheckBox().setSelected(false)
        );

        emotionalDispersion = new EmotionalDispersionChartGui(() ->
                configuratorGui.getEmotionalDispersionCheckBox().setSelected(false)
        );

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

        agentStateTableModel = new AgentStateTableModel(configuratorGui.getCurrentAgentStatesTable());
        initialGuiState();
    }

    private StimulusModel createStimulus(String name, double activation, double satisfaction) {
        return new StimulusModel(name, StringFormatter.toCamelCase(name), activation, satisfaction, true);
    }

    @Override
    protected void takeDown() {
        centralEmotionChart.disposeGui();
        maximumDistanceChart.disposeGui();
        emotionalDispersion.disposeGui();
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
                case PAUSE:
                    pause();
                    break;
                case REFRESH:
                    refresh();
                    break;
                case SHOW_AGENT_STATE:
                    showAgentState();
                    break;
                case HIDE_CENTRAL_EMOTION_CHART:
                    hideCentralEmotionChart();
                    break;
                case SHOW_CENTRAL_EMOTION_CHART:
                    showCentralEmotionChart();
                    break;
                case HIDE_MAXIMUM_DISTANCE_CHART:
                    hideMaximumDistanceChart();
                    break;
                case SHOW_MAXIMUM_DISTANCE_CHART:
                    showMaximumDistanceChart();
                    break;
                case HIDE_EMOTIONAL_DISPERSION_CHART:
                    hideEmotionalDispersionChart();
                    break;
                case SHOW_EMOTIONAL_DISPERSION_CHART:
                    showEmotionalDispersionChart();
                    break;
            }
        } catch (Exception e) {
            logger.exception(e);
            showError(e.getMessage());
        }
    }

    private void showEmotionalDispersionChart() {
        if (started) {
            emotionalDispersion.showGui();
        }
    }

    private void hideEmotionalDispersionChart() {
        emotionalDispersion.hideGui();
    }

    private void hideMaximumDistanceChart() {
        maximumDistanceChart.hideGui();
    }

    private void showMaximumDistanceChart() {
        if (started) {
            maximumDistanceChart.showGui();
        }
    }

    private void showCentralEmotionChart() {
        if (started) {
            centralEmotionChart.showGui();
        }
    }

    private void hideCentralEmotionChart() {
        centralEmotionChart.hideGui();
    }

    private void showAgentState() {
        if (agentStateTableModel.hasSelectedAgent()) {
            AgentState agentState = agentStateTableModel.getSelectedAgent();

            assistant.createAgent(
                    agentState.getAgent().getLocalName() + "GUI",
                    AffectiveModelChartGuiAgent.class,
                    Arrays.asList(agentState.getAgent().getLocalName())
            );
        }
    }

    private void refresh() {
        if (agentBehaviour != null) {
            removeBehaviour(agentBehaviour);
        }

        List<AID> agents = assistant.agents();

        agentTableModel.getAgents().forEach(agent -> {
            try {
                AID gui = getAID(agent.getName() + "GUI");
                if (agents.contains(gui)) {
                    assistant.killAgent(gui);
                }

                AID aid = getAID(agent.getName());
                if (agents.contains(aid)) {
                    assistant.killAgent(aid);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        hideAllCharts();

        started = false;

        initialGuiState();
    }

    private void hideAllCharts() {
        hideCentralEmotionChart();
        hideMaximumDistanceChart();
        hideEmotionalDispersionChart();
    }

    private void pause() {
        suspended = true;
        configuratorGui.getPauseButton().setEnabled(false);
        configuratorGui.getPlayButton().setEnabled(true);
        doWait();
    }

    private void play() {
        if (suspended) {
            suspended = false;
            configuratorGui.getPauseButton().setEnabled(true);
            configuratorGui.getPlayButton().setEnabled(false);
            doWake();
        } else {
            started = true;
            agentTableModel.getAgents().forEach(agent -> {
                String knowledge = agent.getStimuli()
                        .stream()
                        .map(StimulusModel::toClause)
                        .collect(Collectors.joining("\n"));

                List<String> arguments = new DummyEmotionalAgentArgumentsBuilder()
                        .activation(agent.getActivation())
                        .satisfaction(agent.getSatisfaction())
                        .knowledge(knowledge)
                        .build();

                assistant.createAgent(
                        agent.getName(),
                        agent.getAgentType().getAgentCLass(),
                        arguments
                );

                centralEmotionChart.addAgent(agent.getName());
            });

            agentBehaviour = new ConfiguratorGuiAgentBehaviour(
                    this,
                    (Integer) configuratorGui.getIterationsSpinner().getValue()
            );

            addBehaviour(agentBehaviour);

            startedGuiState();
            showChartsOnStart();
        }
    }

    private void showChartsOnStart() {
        if (configuratorGui.getCentralEmotionCheckBox().isSelected()) {
            centralEmotionChart.showGui();
        }

        if (configuratorGui.getMaximumDistanceCheckBox().isSelected()) {
            maximumDistanceChart.showGui();
        }

        if (configuratorGui.getEmotionalDispersionCheckBox().isSelected()) {
            emotionalDispersion.showGui();
        }
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
        if (agentTableModel.getAgents().isEmpty()) {
            configuratorGui.getPlayButton().setEnabled(false);
        }
    }

    private void addAgent() {
        new AgentGuiListener(
                agentTableModel.getAgents(),
                stimulusTableModel.getStimuli(),
                newAgent -> {
                    agentTableModel.addAgent(newAgent);
                    configuratorGui.getPlayButton().setEnabled(true);
                }
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

    public AgentStateTableModel getAgentStateTableModel() {
        return agentStateTableModel;
    }

    public void initialGuiState() {
        configuratorGui.getRefreshButton().setEnabled(false);
        configuratorGui.getPauseButton().setEnabled(false);
        configuratorGui.getShowAgentStateButton().setEnabled(false);

        if (agentTableModel.getAgents().isEmpty()) {
            configuratorGui.getPlayButton().setEnabled(false);
        } else {
            configuratorGui.getPlayButton().setEnabled(true);
        }

        configuratorGui.getIterationsSpinner().setEnabled(true);

        configuratorGui.getIterationLabel().setText("0");
        configuratorGui.getCollectiveCentralEmotionalStateLabel().setText("-");
        configuratorGui.getCollectiveCentralEmotionLabel().setText("-");
        configuratorGui.getEmotionalDispersionValueLabel().setText("-");
        configuratorGui.getMaxDistanceEmotionValueLabel().setText("-");

        configuratorGui.getAddAgentButton().setEnabled(true);
        configuratorGui.getDeleteAgentButton().setEnabled(true);
        configuratorGui.getEditAgentButton().setEnabled(true);

        configuratorGui.getAddStimulusButton().setEnabled(true);
        configuratorGui.getDeleteStimulusButton().setEnabled(true);
        configuratorGui.getEditStimulusButton().setEnabled(true);

        agentStateTableModel.clear();

        maximumDistanceChart.clear();
        emotionalDispersion.clear();
        centralEmotionChart.clear();
    }

    private void startedGuiState() {
        configuratorGui.getRefreshButton().setEnabled(true);
        configuratorGui.getPauseButton().setEnabled(true);
        configuratorGui.getShowAgentStateButton().setEnabled(true);

        configuratorGui.getPlayButton().setEnabled(false);
        configuratorGui.getIterationsSpinner().setEnabled(false);

        configuratorGui.getAddAgentButton().setEnabled(false);
        configuratorGui.getDeleteAgentButton().setEnabled(false);
        configuratorGui.getEditAgentButton().setEnabled(false);

        configuratorGui.getAddStimulusButton().setEnabled(false);
        configuratorGui.getDeleteStimulusButton().setEnabled(false);
        configuratorGui.getEditStimulusButton().setEnabled(false);
    }

    public CentralEmotionChartGui getCentralEmotionChart() {
        return centralEmotionChart;
    }

    public MaximumDistanceChartGui getMaximumDistanceChart() {
        return maximumDistanceChart;
    }

    public EmotionalDispersionChartGui getEmotionalDispersion() {
        return emotionalDispersion;
    }

}
