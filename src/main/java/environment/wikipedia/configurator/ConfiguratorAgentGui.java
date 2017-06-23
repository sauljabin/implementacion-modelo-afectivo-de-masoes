/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.configurator;

import environment.wikipedia.configurator.agent.AgentType;
import masoes.component.behavioural.AffectiveModel;
import masoes.component.behavioural.Emotion;
import masoes.component.behavioural.EmotionalState;
import masoes.ontology.state.AgentState;
import masoes.ontology.state.collective.EmotionalDispersion;
import masoes.ontology.state.collective.MaximumDistances;
import net.miginfocom.swing.MigLayout;
import translate.Translation;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfiguratorAgentGui extends JFrame {

    private static final Font FONT_9 = new Font("Arial", Font.PLAIN, 9);
    private static final Font FONT_10 = new Font("Arial", Font.PLAIN, 10);
    private static final Font FONT_50 = new Font("Arial", Font.BOLD, 50);
    private JPanel leftPanel;
    private Translation translation;

    private JComboBox<Object> agentTypesToAddCombo;
    private AgentsStateTableModel agentsStateTableModel;
    private AgentsToAddTableModel agentsToAddTableModel;
    private JSpinner activationToAddSpinner;
    private JSpinner satisfactionToAddSpinner;
    private JSpinner iterationsSpinner;
    private JButton startButton;
    private JButton cleanButton;
    private JButton addAgentButton;
    private JButton removeAgentButton;
    private JButton windowAgentButton;
    private JTable agentsToAddTable;
    private JTable agentStateTable;
    private JLabel emotionToAddLabel;
    private JLabel emotionalDispersionValueLabel;
    private JLabel maxDistanceEmotionValueLabel;
    private JLabel collectiveCentralEmotionalStateLabel;
    private JLabel collectiveCentralEmotionLabel;
    private AffectiveModel affectiveModel;
    private JLabel actualIterationLabel;
    private JSpinner intervalBetweenEventsSpinner;
    private JCheckBox randomFrequencyCheckBox;
    private KnowledgeRulesTableModel knowledgeRulesTableModel;
    private JTable knowledgeRulesTable;
    private JButton selectAllButton;
    private JButton deselectAllButton;
    private JLabel frequencyValueLabel;
    private JButton selectAllEventsButton;
    private JButton deselectAllEventsButton;
    private JPanel rightPanel;
    private JPanel mainPanel;

    public ConfiguratorAgentGui() {
        translation = Translation.getInstance();
        affectiveModel = AffectiveModel.getInstance();
        setUp();
    }

    public static void main(String[] args) {
        ConfiguratorAgentGui configuratorAgentGui = new ConfiguratorAgentGui();
        configuratorAgentGui.showGui();
    }

    private void setUp() {
        setTitle(translation.get("gui.configurator") + " - Wikipedia");
        setSize(1400, 780);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());
        addComponents();
        configurationMode();
        setLocationRelativeTo(this);
    }

    private void addComponents() {
        mainPanel = new JPanel(new MigLayout("insets 0"));
        add(mainPanel, BorderLayout.CENTER);

        leftPanel = new JPanel(new MigLayout("insets 10 10 10 0"));
        mainPanel.add(leftPanel, "w 50%, h 100%");

        rightPanel = new JPanel(new MigLayout("insets 10"));
        mainPanel.add(rightPanel, "w 50%, h 100%");

        addResultsComponents(rightPanel);
        addConfigComponents(leftPanel);
    }

    private void addResultsComponents(JPanel panel) {
        addIterationPanel(panel);
        addCollectiveEmotionPanel(panel);
        addCurrentStateComponents(panel);
    }

    private void addIterationPanel(JPanel panel) {
        JPanel statusPanel = new JPanel(new MigLayout("insets 5"));
        statusPanel.setBorder(BorderFactory.createTitledBorder(translation.get("gui.iteration")));
        panel.add(statusPanel, "w 100%, wrap 20");

        actualIterationLabel = new JLabel("0");
        actualIterationLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        actualIterationLabel.setFont(FONT_50);
        statusPanel.add(actualIterationLabel, "w 100%");
    }

    private void addCollectiveEmotionPanel(JPanel panel) {
        JPanel collectiveEmotionPanel = new JPanel(new MigLayout("insets 5"));
        collectiveEmotionPanel.setBorder(BorderFactory.createTitledBorder(translation.get("gui.social_emotion")));
        panel.add(collectiveEmotionPanel, "w 100%, wrap 20");

        JLabel centralEmotionLabel = new JLabel(translation.get("gui.central_emotion"));
        collectiveEmotionPanel.add(centralEmotionLabel);

        collectiveCentralEmotionalStateLabel = new JLabel("-");
        collectiveCentralEmotionalStateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        collectiveEmotionPanel.add(collectiveCentralEmotionalStateLabel, "wrap");

        collectiveCentralEmotionLabel = new JLabel("-");
        collectiveCentralEmotionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        collectiveEmotionPanel.add(new JLabel());
        collectiveEmotionPanel.add(collectiveCentralEmotionLabel, "wrap");

        JLabel maxDistanceEmotionLabel = new JLabel(translation.get("gui.max_distance"));
        collectiveEmotionPanel.add(maxDistanceEmotionLabel);

        maxDistanceEmotionValueLabel = new JLabel("-");
        maxDistanceEmotionValueLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        collectiveEmotionPanel.add(maxDistanceEmotionValueLabel, "wrap");

        JLabel emotionalDispersionLabel = new JLabel(translation.get("gui.emotional_dispersion"));
        collectiveEmotionPanel.add(emotionalDispersionLabel);

        emotionalDispersionValueLabel = new JLabel("-");
        emotionalDispersionValueLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        collectiveEmotionPanel.add(emotionalDispersionValueLabel, "wrap");
    }

    private void addCurrentStateComponents(JPanel panel) {
        JPanel currentStatePanel = new JPanel(new MigLayout("insets 5"));
        currentStatePanel.setBorder(BorderFactory.createTitledBorder(translation.get("gui.current_emotional_states")));
        panel.add(currentStatePanel, "w 100%, h 100%");

        agentsStateTableModel = new AgentsStateTableModel();
        agentStateTable = new JTable(agentsStateTableModel);
        agentStateTable.setFillsViewportHeight(true);
        agentStateTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        agentsStateTableModel.setTable(agentStateTable);
        agentStateTable.getTableHeader().setFont(FONT_9);
        agentStateTable.setFont(FONT_9);

        JScrollPane scrollAgentStateTable = new JScrollPane(agentStateTable);
        currentStatePanel.add(scrollAgentStateTable, "h 100%, w 100%");

        JPanel buttonsPanel = new JPanel(new MigLayout("insets 0"));
        currentStatePanel.add(buttonsPanel, "h 100%, wrap");

        windowAgentButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/window.png")));
        buttonsPanel.add(windowAgentButton);
    }

    private void addConfigComponents(JPanel panel) {
        addInitialAgentConfigurationComponents(panel);
        addStimuliComponents(panel);
        addCaseStudyPanel(panel);
    }

    private void addInitialAgentConfigurationComponents(JPanel panel) {
        JPanel initialAgentConfigurationPanel = new JPanel(new MigLayout("insets 5"));
        initialAgentConfigurationPanel.setBorder(BorderFactory.createTitledBorder(translation.get("gui.initial_agent_configuration")));
        panel.add(initialAgentConfigurationPanel, "w 100%, h 100%, wrap");

        JPanel configAgentPanel = new JPanel(new MigLayout("insets 0"));
        initialAgentConfigurationPanel.add(configAgentPanel, "w 100%, span 2, wrap");

        JLabel agentLabel = new JLabel(translation.get("gui.agent"));
        configAgentPanel.add(agentLabel);

        agentTypesToAddCombo = new JComboBox<>(AgentType.values());
        configAgentPanel.add(agentTypesToAddCombo, "wrap");

        JLabel activationLabel = new JLabel(translation.get("gui.activation_x"));
        configAgentPanel.add(activationLabel);

        EmotionalState initialEmotionalState = new EmotionalState(.5, .5);
        Emotion initialEmotion = AffectiveModel.getInstance().searchEmotion(initialEmotionalState);

        activationToAddSpinner = new JSpinner();
        activationToAddSpinner.setModel(new SpinnerNumberModel(initialEmotionalState.getActivation(), -1., 1., .01));
        configAgentPanel.add(activationToAddSpinner, "grow");

        emotionToAddLabel = new JLabel();
        emotionToAddLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        emotionToAddLabel.setBackground(new Color(210, 210, 210));
        emotionToAddLabel.setOpaque(true);
        emotionToAddLabel.setHorizontalAlignment(SwingConstants.CENTER);
        setEmotionToAdd(initialEmotion);

        configAgentPanel.add(emotionToAddLabel, "growy, w 100%, span 0 2, wrap");

        JLabel satisfactionLabel = new JLabel(translation.get("gui.satisfaction_y"));
        configAgentPanel.add(satisfactionLabel);

        satisfactionToAddSpinner = new JSpinner();
        satisfactionToAddSpinner.setModel(new SpinnerNumberModel(initialEmotionalState.getSatisfaction(), -1., 1., .01));
        configAgentPanel.add(satisfactionToAddSpinner, "grow, wrap");

        agentsToAddTableModel = new AgentsToAddTableModel();
        agentsToAddTable = new JTable(agentsToAddTableModel);
        agentsToAddTable.setFillsViewportHeight(true);
        agentsToAddTable.getTableHeader().setFont(FONT_9);
        agentsToAddTable.setFont(FONT_9);
        TableColumnModel columnModel = agentsToAddTable.getColumnModel();
        columnModel.getColumn(0).setMaxWidth(100);

        JScrollPane scrollAgentsToAddTable = new JScrollPane(agentsToAddTable);
        initialAgentConfigurationPanel.add(scrollAgentsToAddTable, "h 100%, w 100%");

        JPanel buttonsPanel = new JPanel(new MigLayout("insets 0"));
        initialAgentConfigurationPanel.add(buttonsPanel, "h 100%, wrap");

        addAgentButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/plus.png")));
        buttonsPanel.add(addAgentButton, "wrap");

        removeAgentButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/minus.png")));
        buttonsPanel.add(removeAgentButton, "wrap");

        selectAllButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/select-all.png")));
        buttonsPanel.add(selectAllButton, "wrap");

        deselectAllButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/deselect-all.png")));
        buttonsPanel.add(deselectAllButton);
    }

    private void addStimuliComponents(JPanel panel) {
        JPanel stimuliPanel = new JPanel(new MigLayout("insets 5"));
        stimuliPanel.setBorder(BorderFactory.createTitledBorder(translation.get("gui.stimuli")));

        panel.add(stimuliPanel, "w 100%, h 100%, wrap");

        knowledgeRulesTableModel = new KnowledgeRulesTableModel();
        knowledgeRulesTable = new JTable(knowledgeRulesTableModel);
        knowledgeRulesTable.setFillsViewportHeight(true);
        TableColumnModel columnModel = knowledgeRulesTable.getColumnModel();
        columnModel.getColumn(0).setMaxWidth(100);
        knowledgeRulesTable.setRowSelectionAllowed(false);
        knowledgeRulesTable.setColumnSelectionAllowed(false);
        knowledgeRulesTable.setFocusable(false);
        knowledgeRulesTable.getTableHeader().setFont(FONT_9);
        knowledgeRulesTable.setFont(FONT_9);

        JScrollPane scrollKnowledgeRulesTable = new JScrollPane(knowledgeRulesTable);
        stimuliPanel.add(scrollKnowledgeRulesTable, "w 100%, h 100%");

        JPanel buttonsPanel = new JPanel(new MigLayout("insets 0"));
        stimuliPanel.add(buttonsPanel, "h 100%, wrap");

        selectAllEventsButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/select-all.png")));
        buttonsPanel.add(selectAllEventsButton, "wrap");

        deselectAllEventsButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/deselect-all.png")));
        buttonsPanel.add(deselectAllEventsButton);
    }

    private void addCaseStudyPanel(JPanel panel) {
        JPanel caseStudyPanel = new JPanel(new MigLayout("insets 5"));
        caseStudyPanel.setBorder(BorderFactory.createTitledBorder(translation.get("gui.case_study")));
        panel.add(caseStudyPanel, "w 100%");

        JPanel valuesPanel = new JPanel(new MigLayout("insets 0"));
        caseStudyPanel.add(valuesPanel, "w 100%");

        JLabel iterationsLabel = new JLabel(translation.get("gui.iterations"));
        valuesPanel.add(iterationsLabel);

        iterationsSpinner = new JSpinner();
        iterationsSpinner.setModel(new SpinnerNumberModel(1000, 0, 1000, 1));
        valuesPanel.add(iterationsSpinner, "w 70, wrap");

        JLabel intervalBetweenEventsLabel = new JLabel(translation.get("gui.interval_between_events"));
        valuesPanel.add(intervalBetweenEventsLabel);

        intervalBetweenEventsSpinner = new JSpinner();
        intervalBetweenEventsSpinner.setModel(new SpinnerNumberModel(5, 1, 1000, 1));
        valuesPanel.add(intervalBetweenEventsSpinner, "w 70");

        randomFrequencyCheckBox = new JCheckBox(translation.get("gui.random"));
        valuesPanel.add(randomFrequencyCheckBox, "wrap");

        JLabel frequencyLabel = new JLabel(translation.get("gui.event_frequency"));
        valuesPanel.add(frequencyLabel);

        frequencyValueLabel = new JLabel();
        valuesPanel.add(frequencyValueLabel, "w 70");
        updateFrequency();

        JPanel buttonsPanel = new JPanel(new MigLayout("insets 0"));
        caseStudyPanel.add(buttonsPanel, "h 100%");

        startButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/play.png")));
        buttonsPanel.add(startButton, "wrap");

        cleanButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/refresh.png")));
        buttonsPanel.add(cleanButton);
    }

    public void closeGui() {
        setVisible(false);
        dispose();
    }

    public void setAgentStates(List<AgentState> agentStates) {
        agentsStateTableModel.setAgentStates(agentStates);
    }

    public void showGui() {
        setVisible(true);
    }

    public void addActionListener(ActionListener actionListener) {
        activationToAddSpinner.addChangeListener(e -> actionListener.actionPerformed(new ActionEvent(
                e.getSource(),
                ConfiguratorAgentEvent.UPDATE_ACTIVATION_TO_ADD.getInt(),
                ConfiguratorAgentEvent.UPDATE_ACTIVATION_TO_ADD.toString()
        )));

        satisfactionToAddSpinner.addChangeListener(e -> actionListener.actionPerformed(new ActionEvent(
                e.getSource(),
                ConfiguratorAgentEvent.UPDATE_SATISFACTION_TO_ADD.getInt(),
                ConfiguratorAgentEvent.UPDATE_SATISFACTION_TO_ADD.toString()
        )));

        iterationsSpinner.addChangeListener(e -> actionListener.actionPerformed(new ActionEvent(
                e.getSource(),
                ConfiguratorAgentEvent.UPDATE_ITERATIONS.getInt(),
                ConfiguratorAgentEvent.UPDATE_ITERATIONS.toString()
        )));

        intervalBetweenEventsSpinner.addChangeListener(e -> actionListener.actionPerformed(new ActionEvent(
                e.getSource(),
                ConfiguratorAgentEvent.UPDATE_INTERVAL_BETWEEN_EVENTS.getInt(),
                ConfiguratorAgentEvent.UPDATE_INTERVAL_BETWEEN_EVENTS.toString()
        )));

        randomFrequencyCheckBox.setActionCommand(ConfiguratorAgentEvent.UPDATE_RANDOM_CHECKBOX.toString());
        randomFrequencyCheckBox.addActionListener(actionListener);

        startButton.setActionCommand(ConfiguratorAgentEvent.START.toString());
        startButton.addActionListener(actionListener);

        cleanButton.setActionCommand(ConfiguratorAgentEvent.CLEAN.toString());
        cleanButton.addActionListener(actionListener);

        addAgentButton.setActionCommand(ConfiguratorAgentEvent.ADD_AGENT.toString());
        addAgentButton.addActionListener(actionListener);

        removeAgentButton.setActionCommand(ConfiguratorAgentEvent.REMOVE_AGENTS.toString());
        removeAgentButton.addActionListener(actionListener);

        windowAgentButton.setActionCommand(ConfiguratorAgentEvent.SHOW_EMOTIONAL_STATE_GUI.toString());
        windowAgentButton.addActionListener(actionListener);

        selectAllButton.setActionCommand(ConfiguratorAgentEvent.SELECT_ALL_AGENTS_TO_ADD.toString());
        selectAllButton.addActionListener(actionListener);

        deselectAllButton.setActionCommand(ConfiguratorAgentEvent.DESELECT_ALL_AGENTS_TO_ADD.toString());
        deselectAllButton.addActionListener(actionListener);

        selectAllEventsButton.setActionCommand(ConfiguratorAgentEvent.SELECT_ALL_EVENTS.toString());
        selectAllEventsButton.addActionListener(actionListener);

        deselectAllEventsButton.setActionCommand(ConfiguratorAgentEvent.DESELECT_ALL_EVENTS.toString());
        deselectAllEventsButton.addActionListener(actionListener);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public double getActivationToAdd() {
        return (double) activationToAddSpinner.getValue();
    }

    public double getSatisfactionToAdd() {
        return (double) satisfactionToAddSpinner.getValue();
    }

    public AgentType getAgentTypeToAdd() {
        return (AgentType) agentTypesToAddCombo.getSelectedItem();
    }

    public void setEmotionToAdd(Emotion emotion) {
        emotionToAddLabel.setText(String.format("%s - %s", translation.get(emotion.getName().toLowerCase()), translation.get(emotion.getType().toString().toLowerCase())));
    }

    public void addAgentToAdd(AgentToAdd agentToAdd) {
        agentsToAddTableModel.addAgentToAdd(agentToAdd);
    }

    public void removeAgentToAdd(AgentToAdd agentToAdd) {
        agentsToAddTableModel.removeAgentToAdd(agentToAdd);
    }

    public List<AgentToAdd> getAgentsToAdd() {
        return agentsToAddTableModel.getAgentsToAdd();
    }

    public void setAgentsToAdd(List<AgentToAdd> agentToAdds) {
        agentsToAddTableModel.setAgentsToAdd(agentToAdds);
    }

    public List<AgentToAdd> getSelectedAgentToAdd() {
        List<AgentToAdd> agentToAdds = new ArrayList<>();
        Arrays.stream(agentsToAddTable.getSelectedRows())
                .forEach(index -> agentToAdds.add(agentsToAddTableModel.getAgentsToAdd().get(index)));
        return agentToAdds;
    }

    public AgentState getSelectedAgentState() {
        return agentsStateTableModel.getAgentStates().get(agentStateTable.getSelectedRow());
    }

    public void simulationMode() {
        cleanButton.setEnabled(true);
        windowAgentButton.setEnabled(true);
        addAgentButton.setEnabled(false);
        startButton.setEnabled(false);
        removeAgentButton.setEnabled(false);
        agentTypesToAddCombo.setEnabled(false);
        activationToAddSpinner.setEnabled(false);
        satisfactionToAddSpinner.setEnabled(false);
        iterationsSpinner.setEnabled(false);
        intervalBetweenEventsSpinner.setEnabled(false);
        randomFrequencyCheckBox.setEnabled(false);
        knowledgeRulesTable.setEnabled(false);
        deselectAllButton.setEnabled(false);
        selectAllButton.setEnabled(false);
        agentsToAddTable.setEnabled(false);
        selectAllEventsButton.setEnabled(false);
        deselectAllEventsButton.setEnabled(false);
    }

    public void configurationMode() {
        cleanButton.setEnabled(false);
        windowAgentButton.setEnabled(false);
        startButton.setEnabled(false);
        addAgentButton.setEnabled(true);
        removeAgentButton.setEnabled(true);
        agentTypesToAddCombo.setEnabled(true);
        activationToAddSpinner.setEnabled(true);
        satisfactionToAddSpinner.setEnabled(true);
        iterationsSpinner.setEnabled(true);
        intervalBetweenEventsSpinner.setEnabled(true);
        randomFrequencyCheckBox.setEnabled(true);
        randomFrequencyCheckBox.setSelected(false);
        knowledgeRulesTable.setEnabled(true);
        deselectAllButton.setEnabled(true);
        selectAllButton.setEnabled(true);
        agentsToAddTable.setEnabled(true);
        selectAllEventsButton.setEnabled(true);
        deselectAllEventsButton.setEnabled(true);

        collectiveCentralEmotionalStateLabel.setText("-");
        collectiveCentralEmotionLabel.setText("-");
        maxDistanceEmotionValueLabel.setText("-");
        emotionalDispersionValueLabel.setText("-");
        setActualIteration(0);
    }

    public void activateStartButton() {
        startButton.setEnabled(true);
    }

    public void deactivateStartButton() {
        startButton.setEnabled(false);
    }

    public void setEmotionalDispersion(EmotionalDispersion emotionalDispersion) {
        emotionalDispersionValueLabel.setText(String.format("(%.3f, %.3f)", emotionalDispersion.getActivation(), emotionalDispersion.getSatisfaction()));
    }

    public void setMaximumDistance(MaximumDistances maximumDistance) {
        maxDistanceEmotionValueLabel.setText(String.format("(%.3f, %.3f)", maximumDistance.getActivation(), maximumDistance.getSatisfaction()));
    }

    public void setCentralEmotion(EmotionalState centralEmotion) {
        collectiveCentralEmotionalStateLabel.setText(String.format("(%.3f, %.3f)", centralEmotion.getActivation(), centralEmotion.getSatisfaction()));
        Emotion emotion = affectiveModel.searchEmotion(centralEmotion);
        collectiveCentralEmotionLabel.setText(String.format("%s - %s", translation.get(emotion.getName().toLowerCase()), translation.get(emotion.getType().toString().toLowerCase())));
    }

    public int getIterations() {
        return (int) iterationsSpinner.getValue();
    }

    public int getIntervalBetweenEvents() {
        return (int) intervalBetweenEventsSpinner.getValue();
    }

    public void setIntervalBetweenEvents(int value) {
        intervalBetweenEventsSpinner.setValue(value);
    }

    public boolean isEventFrequencyRandom() {
        return randomFrequencyCheckBox.isSelected();
    }

    public void setActualIteration(int i) {
        actualIterationLabel.setText(String.format("%d", i));
    }

    public void setKnowledgeRules(List<KnowledgeRule> knowledgeRules) {
        knowledgeRulesTableModel.setKnowledgeRules(knowledgeRules);
    }

    public void deselectAllAgentsToAdd() {
        agentsToAddTableModel.deselectAllAgentsToAdd();
    }

    public void selectAllAgentsToAdd() {
        agentsToAddTableModel.selectAllAgentsToAdd();
    }

    public void updateFrequency() {
        int frequency = (int) iterationsSpinner.getValue() / (int) intervalBetweenEventsSpinner.getValue();
        frequencyValueLabel.setText(String.valueOf(frequency));
    }

    public void enabledIntervalBetweenEvents(boolean enabled) {
        intervalBetweenEventsSpinner.setEnabled(enabled);
    }

    public String getEventFrequency() {
        return frequencyValueLabel.getText();
    }

    public void selectAllEvents() {
        knowledgeRulesTableModel.selectAllEvents();
    }

    public void deselectAllEvents() {
        knowledgeRulesTableModel.deselectAllEvents();
    }

}
