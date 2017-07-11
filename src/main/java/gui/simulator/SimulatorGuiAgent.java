/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.simulator;

import agent.AgentException;
import agent.AgentLogger;
import agent.AgentManagementAssistant;
import environment.dummy.DummyEmotionalAgentArgumentsBuilder;
import gui.agentstate.AgentStateGuiAgent;
import gui.chart.behaviourmodification.BehaviourModificationChartGui;
import gui.chart.centralemotion.CentralEmotionChartGui;
import gui.chart.emotionaldispersion.EmotionalDispersionChartGui;
import gui.chart.emotionalstate.EmotionalStateChartGui;
import gui.chart.emotionmodification.EmotionModificationChartGui;
import gui.chart.maximundistance.MaximumDistanceChartGui;
import gui.simulator.agentconfiguration.AgentConfigurationGuiListener;
import gui.simulator.agentconfiguration.AgentConfigurationModel;
import gui.simulator.agentconfiguration.AgentConfigurationTableModel;
import gui.simulator.agentstate.AgentStateTableModel;
import gui.simulator.agenttypedefinition.AgentTypeDefinitionCrudGuiListener;
import gui.simulator.agenttypedefinition.AgentTypeDefinitionModel;
import gui.simulator.stimulusconfiguration.StimulusConfigurationModel;
import gui.simulator.stimulusdefinition.StimulusDefinitionCrudGuiListener;
import gui.simulator.stimulusdefinition.StimulusDefinitionModel;
import jade.JadeSettings;
import jade.core.AID;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import masoes.ontology.state.AgentState;
import translate.Translation;
import util.StringFormatter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SimulatorGuiAgent extends GuiAgent {

    private ArrayList<AgentConfigurationModel> agentConfigurationModels;
    private ArrayList<StimulusDefinitionModel> stimulusDefinitionModels;
    private ArrayList<AgentTypeDefinitionModel> agentTypeDefinitionModels;

    private AgentManagementAssistant assistant;
    private SimulatorGuiListener simulatorGuiListener;
    private SimulatorGui simulatorGui;
    private AgentLogger logger;
    private AgentConfigurationTableModel agentConfigurationTableModel;
    private SimulatorGuiAgentBehaviour agentBehaviour;
    private AgentStateTableModel agentStateTableModel;

    private MaximumDistanceChartGui maximumDistanceChart;
    private BehaviourModificationChartGui behaviourModificationChart;
    private CentralEmotionChartGui centralEmotionChart;
    private EmotionalDispersionChartGui emotionalDispersionChart;
    private EmotionModificationChartGui emotionModificationChart;
    private EmotionalStateChartGui emotionalStateChart;

    private boolean paused;
    private boolean started;
    private SequentialBehaviour sequentialBehaviour;
    private ThreadedBehaviourFactory threadedBehaviourFactory;

    public SimulatorGuiAgent() {
        agentTypeDefinitionModels = new ArrayList<>();
        stimulusDefinitionModels = new ArrayList<>();
        agentConfigurationModels = new ArrayList<>();

        logger = new AgentLogger(this);
        assistant = new AgentManagementAssistant(this);
        simulatorGui = new SimulatorGui();
        simulatorGuiListener = new SimulatorGuiListener(simulatorGui, this);

        centralEmotionChart = new CentralEmotionChartGui(() ->
                simulatorGui.getCentralEmotionCheckBox().setSelected(false)
        );

        maximumDistanceChart = new MaximumDistanceChartGui(() ->
                simulatorGui.getMaximumDistanceCheckBox().setSelected(false)
        );

        emotionalDispersionChart = new EmotionalDispersionChartGui(() ->
                simulatorGui.getEmotionalDispersionCheckBox().setSelected(false)
        );

        behaviourModificationChart = new BehaviourModificationChartGui(() ->
                simulatorGui.getBehavioursCheckBox().setSelected(false)
        );

        emotionModificationChart = new EmotionModificationChartGui(() ->
                simulatorGui.getEmotionsCheckBox().setSelected(false)
        );

        emotionalStateChart = new EmotionalStateChartGui(() ->
                simulatorGui.getEmotionalStatesCheckBox().setSelected(false)
        );

        configView();
    }

    @Override
    protected void setup() {
        if (!isGUIEnabled()) {
            throw new AgentException(getLocalName() + ": gui option is disabled");
        }

        simulatorGui.setVisible(true);
    }

    private boolean isGUIEnabled() {
        return Boolean.parseBoolean(JadeSettings.getInstance().get(JadeSettings.GUI));
    }

    private void configView() {
        agentConfigurationTableModel = new AgentConfigurationTableModel(simulatorGui.getAgentsTable(), agentConfigurationModels);

        agentStateTableModel = new AgentStateTableModel(simulatorGui.getCurrentAgentStatesTable());
        initialGuiState();
    }

    private StimulusDefinitionModel createStimulus(String name, double activation, double satisfaction) {
        return new StimulusDefinitionModel(name, StringFormatter.toCamelCase(name), activation, satisfaction, true);
    }

    @Override
    protected void takeDown() {
        centralEmotionChart.disposeGui();
        maximumDistanceChart.disposeGui();
        emotionalDispersionChart.disposeGui();
        behaviourModificationChart.disposeGui();
        emotionModificationChart.disposeGui();
        emotionalStateChart.disposeGui();
        simulatorGui.dispose();
    }

    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {
        try {
            switch (SimulatorGuiEvent.fromInt(guiEvent.getType())) {
                case CLOSE_WINDOW:
                    closeWindow();
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
                case HIDE_BEHAVIOUR_MODIFICATION_CHART:
                    hideBehaviourModificationChart();
                    break;
                case SHOW_BEHAVIOUR_MODIFICATION_CHART:
                    showBehaviourModificationChart();
                    break;
                case HIDE_EMOTION_MODIFICATION_CHART:
                    hideEmotionModificationChart();
                    break;
                case SHOW_EMOTION_MODIFICATION_CHART:
                    showEmotionModificationChart();
                    break;
                case HIDE_EMOTIONAL_STATE_CHART:
                    hideEmotionalStateChart();
                    break;
                case SHOW_EMOTIONAL_STATE_CHART:
                    showEmotionalStateChart();
                    break;
                case SHOW_AGENT_TYPE_DEFINITION_GUI:
                    showAgentTypesDefinitionGUI();
                    break;
                case SHOW_STIMULUS_DEFINITION_GUI:
                    showStimuliDefinitionGUI();
                    break;
            }
        } catch (Exception e) {
            logger.exception(e);
            showError(e.getMessage());
        }
    }

    private void showStimuliDefinitionGUI() {
        new StimulusDefinitionCrudGuiListener(stimulusDefinitionModels);
    }

    private void showAgentTypesDefinitionGUI() {
        new AgentTypeDefinitionCrudGuiListener(agentTypeDefinitionModels);
    }

    private void showEmotionalStateChart() {
        if (started) {
            emotionalStateChart.showGui();
        }
    }

    private void hideEmotionalStateChart() {
        emotionalStateChart.hideGui();
    }

    private void showEmotionModificationChart() {
        if (started) {
            emotionModificationChart.showGui();
        }
    }

    private void hideEmotionModificationChart() {
        emotionModificationChart.hideGui();
    }

    private void showBehaviourModificationChart() {
        if (started) {
            behaviourModificationChart.showGui();
        }
    }

    private void hideBehaviourModificationChart() {
        behaviourModificationChart.hideGui();
    }

    private void showEmotionalDispersionChart() {
        if (started) {
            emotionalDispersionChart.showGui();
        }
    }

    private void hideEmotionalDispersionChart() {
        emotionalDispersionChart.hideGui();
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
        if (agentStateTableModel.hasSelected()) {
            AgentState agentState = agentStateTableModel.getSelectedElement();

            assistant.createAgent(
                    agentState.getAgent().getLocalName() + "GUI",
                    AgentStateGuiAgent.class,
                    Arrays.asList(agentState.getAgent().getLocalName())
            );
        }
    }

    private void refresh() {
        if (threadedBehaviourFactory != null) {
            threadedBehaviourFactory.interrupt();
        }

        List<AID> agents = assistant.agents();

        agentConfigurationModels.forEach(agent -> {
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
        paused = false;

        initialGuiState();
    }

    private void hideAllCharts() {
        hideCentralEmotionChart();
        hideMaximumDistanceChart();
        hideEmotionalDispersionChart();
        hideBehaviourModificationChart();
        hideEmotionModificationChart();
        hideEmotionalStateChart();
    }

    private void pause() {
        paused = true;
        simulatorGui.getPauseButton().setEnabled(false);
        simulatorGui.getPlayButton().setEnabled(true);
        agentBehaviour.pause();
    }

    private void play() {
        if (paused) {
            paused = false;
            simulatorGui.getPauseButton().setEnabled(true);
            simulatorGui.getPlayButton().setEnabled(false);
            agentBehaviour.play();
        } else {
            started = true;
            agentConfigurationModels.forEach(agent -> {
                String knowledge = agent.getStimulusConfigurations()
                        .stream()
                        .map(StimulusConfigurationModel::toClause)
                        .collect(Collectors.joining("\n"));

                List<String> arguments = new DummyEmotionalAgentArgumentsBuilder()
                        .activation(agent.getActivation())
                        .satisfaction(agent.getSatisfaction())
                        .knowledge(knowledge)
                        .build();

                assistant.createAgent(
                        agent.getName(),
                        agent.getAgentType().getAgentTypeClass(),
                        arguments
                );

                centralEmotionChart.addAgent(agent.getName());
                behaviourModificationChart.addAgent(agent.getName());
                emotionModificationChart.addAgent(agent.getName());
                emotionalStateChart.addAgent(agent.getName());
            });

            agentBehaviour = new SimulatorGuiAgentBehaviour(
                    this,
                    (Integer) simulatorGui.getIterationsSpinner().getValue()
            );

            threadedBehaviourFactory = new ThreadedBehaviourFactory();
            sequentialBehaviour = new SequentialBehaviour(this);
            sequentialBehaviour.addSubBehaviour(new SimulatorGuiAgentInitialBehaviour(this));
            sequentialBehaviour.addSubBehaviour(agentBehaviour);
            addBehaviour(threadedBehaviourFactory.wrap(sequentialBehaviour));

            startedGuiState();
            showChartsOnStart();
        }
    }

    private void showChartsOnStart() {
        if (simulatorGui.getCentralEmotionCheckBox().isSelected()) {
            centralEmotionChart.showGui();
        }

        if (simulatorGui.getMaximumDistanceCheckBox().isSelected()) {
            maximumDistanceChart.showGui();
        }

        if (simulatorGui.getEmotionalDispersionCheckBox().isSelected()) {
            emotionalDispersionChart.showGui();
        }

        if (simulatorGui.getBehavioursCheckBox().isSelected()) {
            behaviourModificationChart.showGui();
        }

        if (simulatorGui.getEmotionsCheckBox().isSelected()) {
            emotionModificationChart.showGui();
        }

        if (simulatorGui.getEmotionalStatesCheckBox().isSelected()) {
            emotionalStateChart.showGui();
        }
    }

    private void editAgent() {
        if (agentConfigurationTableModel.hasSelected()) {
            new AgentConfigurationGuiListener(
                    agentConfigurationTableModel.getSelectedElement(),
                    agentConfigurationModels,
                    agentTypeDefinitionModels,
                    stimulusDefinitionModels,
                    updatedAgent -> agentConfigurationTableModel.fireTableDataChanged()
            );
        }
    }

    private void deleteAgent() {
        agentConfigurationTableModel.deleteSelectedElements();
        if (agentConfigurationModels.isEmpty()) {
            simulatorGui.getPlayButton().setEnabled(false);
        }
    }

    private void addAgent() {
        if (agentTypeDefinitionModels.isEmpty()) {
            showInfo(Translation.getInstance().get("gui.message.agent_types_definition_not_found"));
        } else {
            new AgentConfigurationGuiListener(
                    agentConfigurationModels,
                    agentTypeDefinitionModels,
                    stimulusDefinitionModels,
                    newAgent -> {
                        agentConfigurationTableModel.add(newAgent);
                        simulatorGui.getPlayButton().setEnabled(true);
                    }
            );
        }
    }

    private void closeWindow() {
        doDelete();
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(simulatorGui, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showInfo(String message) {
        JOptionPane.showMessageDialog(simulatorGui, message, Translation.getInstance().get("gui.information"), JOptionPane.INFORMATION_MESSAGE);
    }

    public SimulatorGui getSimulatorGui() {
        return simulatorGui;
    }

    public AgentConfigurationTableModel getAgentConfigurationTableModel() {
        return agentConfigurationTableModel;
    }

    public AgentStateTableModel getAgentStateTableModel() {
        return agentStateTableModel;
    }

    public void initialGuiState() {
        simulatorGui.getRefreshButton().setEnabled(false);
        simulatorGui.getPauseButton().setEnabled(false);
        simulatorGui.getShowAgentStateButton().setEnabled(false);

        if (agentConfigurationModels.isEmpty()) {
            simulatorGui.getPlayButton().setEnabled(false);
        } else {
            simulatorGui.getPlayButton().setEnabled(true);
        }

        simulatorGui.getIterationsSpinner().setEnabled(true);

        simulatorGui.getIterationLabel().setText("0");
        simulatorGui.getCollectiveCentralEmotionalStateLabel().setText("-");
        simulatorGui.getCollectiveCentralEmotionLabel().setText("-");
        simulatorGui.getEmotionalDispersionValueLabel().setText("-");
        simulatorGui.getMaxDistanceEmotionValueLabel().setText("-");

        simulatorGui.getAddAgentButton().setEnabled(true);
        simulatorGui.getDeleteAgentButton().setEnabled(true);
        simulatorGui.getEditAgentButton().setEnabled(true);

        agentStateTableModel.clear();

        maximumDistanceChart.clear();
        emotionalDispersionChart.clear();
        centralEmotionChart.clear();
        behaviourModificationChart.clear();
        emotionModificationChart.clear();
        emotionalStateChart.clear();

        simulatorGui.getEditMenu().setEnabled(true);
        simulatorGui.getFileMenu().setEnabled(true);
    }

    private void startedGuiState() {
        simulatorGui.getRefreshButton().setEnabled(true);
        simulatorGui.getPauseButton().setEnabled(true);
        simulatorGui.getShowAgentStateButton().setEnabled(true);

        simulatorGui.getPlayButton().setEnabled(false);
        simulatorGui.getIterationsSpinner().setEnabled(false);

        simulatorGui.getAddAgentButton().setEnabled(false);
        simulatorGui.getDeleteAgentButton().setEnabled(false);
        simulatorGui.getEditAgentButton().setEnabled(false);

        simulatorGui.getEditMenu().setEnabled(false);
        simulatorGui.getFileMenu().setEnabled(false);
    }

    public CentralEmotionChartGui getCentralEmotionChart() {
        return centralEmotionChart;
    }

    public MaximumDistanceChartGui getMaximumDistanceChart() {
        return maximumDistanceChart;
    }

    public EmotionalDispersionChartGui getEmotionalDispersionChart() {
        return emotionalDispersionChart;
    }

    public BehaviourModificationChartGui getBehaviourModificationChart() {
        return behaviourModificationChart;
    }

    public EmotionModificationChartGui getEmotionModificationChart() {
        return emotionModificationChart;
    }

    public EmotionalStateChartGui getEmotionalStateChart() {
        return emotionalStateChart;
    }

    public ArrayList<StimulusDefinitionModel> getStimulusDefinitionModels() {
        return stimulusDefinitionModels;
    }

    public ArrayList<AgentTypeDefinitionModel> getAgentTypeDefinitionModels() {
        return agentTypeDefinitionModels;
    }

    public ArrayList<AgentConfigurationModel> getAgentConfigurationModels() {
        return agentConfigurationModels;
    }

}
