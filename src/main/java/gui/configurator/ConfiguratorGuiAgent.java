/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.configurator;

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
import gui.configurator.agentconfiguration.AgentConfigurationGuiListener;
import gui.configurator.agentconfiguration.AgentConfigurationModel;
import gui.configurator.agentconfiguration.AgentConfigurationTableModel;
import gui.configurator.agentstate.AgentStateTableModel;
import gui.configurator.agenttypedefinition.AgentTypeDefinitionCrudGuiListener;
import gui.configurator.agenttypedefinition.AgentTypeDefinitionModel;
import gui.configurator.stimulusconfiguration.StimulusConfigurationModel;
import gui.configurator.stimulusdefinition.StimulusDefinitionCrudGuiListener;
import gui.configurator.stimulusdefinition.StimulusDefinitionModel;
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

public class ConfiguratorGuiAgent extends GuiAgent {

    private ArrayList<AgentConfigurationModel> agentConfigurationModels;
    private ArrayList<StimulusDefinitionModel> stimulusDefinitionModels;
    private ArrayList<AgentTypeDefinitionModel> agentTypeDefinitionModels;

    private AgentManagementAssistant assistant;
    private ConfiguratorGuiListener configuratorGuiListener;
    private ConfiguratorGui configuratorGui;
    private AgentLogger logger;
    private AgentConfigurationTableModel agentConfigurationTableModel;
    private ConfiguratorGuiAgentBehaviour agentBehaviour;
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

    public ConfiguratorGuiAgent() {
        agentTypeDefinitionModels = new ArrayList<>();
        stimulusDefinitionModels = new ArrayList<>();
        agentConfigurationModels = new ArrayList<>();

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

        emotionalDispersionChart = new EmotionalDispersionChartGui(() ->
                configuratorGui.getEmotionalDispersionCheckBox().setSelected(false)
        );

        behaviourModificationChart = new BehaviourModificationChartGui(() ->
                configuratorGui.getBehavioursCheckBox().setSelected(false)
        );

        emotionModificationChart = new EmotionModificationChartGui(() ->
                configuratorGui.getEmotionsCheckBox().setSelected(false)
        );

        emotionalStateChart = new EmotionalStateChartGui(() ->
                configuratorGui.getEmotionalStatesCheckBox().setSelected(false)
        );

        configView();
    }

    @Override
    protected void setup() {
        if (!isGUIEnabled()) {
            throw new AgentException(getLocalName() + ": gui option is disabled");
        }

        configuratorGui.setVisible(true);
    }

    private boolean isGUIEnabled() {
        return Boolean.parseBoolean(JadeSettings.getInstance().get(JadeSettings.GUI));
    }

    private void configView() {
        agentConfigurationTableModel = new AgentConfigurationTableModel(configuratorGui.getAgentsTable(), agentConfigurationModels);

        agentStateTableModel = new AgentStateTableModel(configuratorGui.getCurrentAgentStatesTable());
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
        configuratorGui.dispose();
    }

    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {
        try {
            switch (ConfiguratorGuiEvent.fromInt(guiEvent.getType())) {
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
        configuratorGui.getPauseButton().setEnabled(false);
        configuratorGui.getPlayButton().setEnabled(true);
        agentBehaviour.pause();
    }

    private void play() {
        if (paused) {
            paused = false;
            configuratorGui.getPauseButton().setEnabled(true);
            configuratorGui.getPlayButton().setEnabled(false);
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

            agentBehaviour = new ConfiguratorGuiAgentBehaviour(
                    this,
                    (Integer) configuratorGui.getIterationsSpinner().getValue()
            );

            threadedBehaviourFactory = new ThreadedBehaviourFactory();
            sequentialBehaviour = new SequentialBehaviour(this);
            sequentialBehaviour.addSubBehaviour(new ConfiguratorGuiAgentInitialBehaviour(this));
            sequentialBehaviour.addSubBehaviour(agentBehaviour);
            addBehaviour(threadedBehaviourFactory.wrap(sequentialBehaviour));

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
            emotionalDispersionChart.showGui();
        }

        if (configuratorGui.getBehavioursCheckBox().isSelected()) {
            behaviourModificationChart.showGui();
        }

        if (configuratorGui.getEmotionsCheckBox().isSelected()) {
            emotionModificationChart.showGui();
        }

        if (configuratorGui.getEmotionalStatesCheckBox().isSelected()) {
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
            configuratorGui.getPlayButton().setEnabled(false);
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
                        configuratorGui.getPlayButton().setEnabled(true);
                    }
            );
        }
    }

    private void closeWindow() {
        doDelete();
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(configuratorGui, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showInfo(String message) {
        JOptionPane.showMessageDialog(configuratorGui, message, Translation.getInstance().get("gui.information"), JOptionPane.INFORMATION_MESSAGE);
    }

    public ConfiguratorGui getConfiguratorGui() {
        return configuratorGui;
    }

    public AgentConfigurationTableModel getAgentConfigurationTableModel() {
        return agentConfigurationTableModel;
    }

    public AgentStateTableModel getAgentStateTableModel() {
        return agentStateTableModel;
    }

    public void initialGuiState() {
        configuratorGui.getRefreshButton().setEnabled(false);
        configuratorGui.getPauseButton().setEnabled(false);
        configuratorGui.getShowAgentStateButton().setEnabled(false);

        if (agentConfigurationModels.isEmpty()) {
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

        agentStateTableModel.clear();

        maximumDistanceChart.clear();
        emotionalDispersionChart.clear();
        centralEmotionChart.clear();
        behaviourModificationChart.clear();
        emotionModificationChart.clear();
        emotionalStateChart.clear();

        configuratorGui.getEditMenu().setEnabled(true);
        configuratorGui.getFileMenu().setEnabled(true);
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

        configuratorGui.getEditMenu().setEnabled(false);
        configuratorGui.getFileMenu().setEnabled(false);
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
