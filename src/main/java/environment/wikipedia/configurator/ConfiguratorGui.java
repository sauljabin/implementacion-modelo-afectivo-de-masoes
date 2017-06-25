/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.configurator;

import net.miginfocom.swing.MigLayout;
import translate.Translation;

import javax.swing.*;
import java.awt.*;

public class ConfiguratorGui extends JFrame {

    private static final Font FONT_50 = new Font("Arial", Font.BOLD, 50);

    private static final String PRINCIPAL_PANELS_SIZE = "w 50%, h 100%";
    private static final String PANELS_SIZE = "w 100%, h 100%, wrap";
    private static final String PANELS_SIZE_WITHOUT_H = "w 100%, wrap";
    private static final String PANEL_INSETS = "insets 5";

    private Translation translation = Translation.getInstance();
    private JButton addStimulusButton;
    private JButton deleteStimulusButton;
    private JButton editStimulusButton;
    private JButton addAgentButton;
    private JButton deleteAgentButton;
    private JButton editAgentButton;
    private JButton showAgentStateButton;
    private JTable stimuliTable;
    private JTable agentsTable;
    private JLabel iterationLabel;
    private JLabel collectiveCentralEmotionalStateLabel;
    private JLabel collectiveCentralEmotionLabel;
    private JLabel maxDistanceEmotionValueLabel;
    private JLabel emotionalDispersionValueLabel;
    private JButton playButton;
    private JButton pauseButton;
    private JButton refreshButton;
    private JSpinner iterationsSpinner;
    private JCheckBox centralEmotionCheckBox;
    private JCheckBox maximumDistanceCheckBox;
    private JCheckBox emotionalDispersionCheckBox;
    private JCheckBox emotionalStatesCheckBox;
    private JCheckBox behavioursCheckBox;
    private JCheckBox emotionsCheckBox;
    private JTable currentAgentStatesTable;

    public ConfiguratorGui() {
        setTitle(translation.get("gui.configurator") + " - Wikipedia");
        setSize(1400, 780);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new MigLayout());
        add(mainPanel, BorderLayout.CENTER);

        JPanel leftPanel = new JPanel(new MigLayout("insets 10 10 10 0"));
        mainPanel.add(leftPanel, PRINCIPAL_PANELS_SIZE);

        leftPanel.add(createStimulusPanel(), PANELS_SIZE);
        leftPanel.add(createAgentsPanel(), PANELS_SIZE);

        JPanel rightPanel = new JPanel(new MigLayout("insets 10"));
        mainPanel.add(rightPanel, PRINCIPAL_PANELS_SIZE);

        rightPanel.add(createControlPanel(), PANELS_SIZE_WITHOUT_H);
        rightPanel.add(createSelectChartsPanel(), PANELS_SIZE_WITHOUT_H);
        rightPanel.add(createIterationPanel(), PANELS_SIZE_WITHOUT_H);
        rightPanel.add(createCollectiveEmotionPanel(), PANELS_SIZE_WITHOUT_H);
        rightPanel.add(createCurrentAgentStatePanel(), PANELS_SIZE);

        setLocationRelativeTo(this);
    }

    public static void main(String[] args) {
        ConfiguratorGui configuratorGui = new ConfiguratorGui();
        configuratorGui.setVisible(true);
    }

    private JPanel createSelectChartsPanel() {
        JPanel chartsButtonsPanel = new JPanel(new MigLayout(PANEL_INSETS));
        chartsButtonsPanel.setBorder(BorderFactory.createTitledBorder(translation.get("gui.real_time_charts")));

        emotionalStatesCheckBox = new JCheckBox(translation.get("gui.emotional_states"), true);
        chartsButtonsPanel.add(emotionalStatesCheckBox);

        emotionsCheckBox = new JCheckBox(translation.get("gui.emotions"), true);
        chartsButtonsPanel.add(emotionsCheckBox);

        behavioursCheckBox = new JCheckBox(translation.get("gui.behaviours"), true);
        chartsButtonsPanel.add(behavioursCheckBox, "wrap");

        centralEmotionCheckBox = new JCheckBox(translation.get("gui.central_emotion"), true);
        chartsButtonsPanel.add(centralEmotionCheckBox);

        maximumDistanceCheckBox = new JCheckBox(translation.get("gui.maximum_distance"), true);
        chartsButtonsPanel.add(maximumDistanceCheckBox);

        emotionalDispersionCheckBox = new JCheckBox(translation.get("gui.emotional_dispersion"), true);
        chartsButtonsPanel.add(emotionalDispersionCheckBox);

        return chartsButtonsPanel;
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new MigLayout(PANEL_INSETS));
        controlPanel.setBorder(BorderFactory.createTitledBorder(translation.get("gui.control")));

        JLabel iterationsLabel = new JLabel(translation.get("gui.iterations"));
        controlPanel.add(iterationsLabel);

        iterationsSpinner = new JSpinner();
        iterationsSpinner.setModel(new SpinnerNumberModel(100, 0, 1000, 1));
        controlPanel.add(iterationsSpinner, "h 30, w 70");

        playButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/play.png")));
        controlPanel.add(playButton);

        pauseButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/pause.png")));
        controlPanel.add(pauseButton);

        refreshButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/refresh.png")));
        controlPanel.add(refreshButton);

        return controlPanel;
    }

    private JPanel createIterationPanel() {
        JPanel iterationPanel = new JPanel(new MigLayout(PANEL_INSETS));
        iterationPanel.setBorder(BorderFactory.createTitledBorder(translation.get("gui.iteration")));

        iterationLabel = new JLabel("0");
        iterationLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        iterationLabel.setFont(FONT_50);
        iterationPanel.add(iterationLabel, "w 100%");

        return iterationPanel;
    }

    private JPanel createCollectiveEmotionPanel() {
        JPanel collectiveEmotionPanel = new JPanel(new MigLayout("insets 5"));
        collectiveEmotionPanel.setBorder(BorderFactory.createTitledBorder(translation.get("gui.social_emotion")));

        JLabel centralEmotionLabel = new JLabel(translation.get("gui.central_emotion"));
        collectiveEmotionPanel.add(centralEmotionLabel);

        collectiveCentralEmotionalStateLabel = new JLabel("-");
        collectiveCentralEmotionalStateLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        collectiveEmotionPanel.add(collectiveCentralEmotionalStateLabel, "wrap");

        collectiveCentralEmotionLabel = new JLabel("-");
        collectiveEmotionPanel.add(new JLabel());
        collectiveCentralEmotionLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        collectiveEmotionPanel.add(collectiveCentralEmotionLabel, "wrap");

        JLabel maxDistanceEmotionLabel = new JLabel(translation.get("gui.maximum_distance"));
        collectiveEmotionPanel.add(maxDistanceEmotionLabel);

        maxDistanceEmotionValueLabel = new JLabel("-");
        maxDistanceEmotionValueLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        collectiveEmotionPanel.add(maxDistanceEmotionValueLabel, "wrap");

        JLabel emotionalDispersionLabel = new JLabel(translation.get("gui.emotional_dispersion"));
        collectiveEmotionPanel.add(emotionalDispersionLabel);

        emotionalDispersionValueLabel = new JLabel("-");
        emotionalDispersionValueLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        collectiveEmotionPanel.add(emotionalDispersionValueLabel, "wrap");

        return collectiveEmotionPanel;
    }

    private JPanel createAgentsPanel() {
        JPanel agentPanel = new JPanel(new MigLayout(PANEL_INSETS));
        agentPanel.setBorder(BorderFactory.createTitledBorder(translation.get("gui.initial_agent_configuration")));

        agentsTable = new JTable();

        JScrollPane scrollTable = new JScrollPane(agentsTable);
        agentPanel.add(scrollTable, "w 100%, h 100%");

        JPanel buttonsPanel = new JPanel(new MigLayout("insets 0"));
        agentPanel.add(buttonsPanel, "h 100%, wrap");

        addAgentButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/plus.png")));
        buttonsPanel.add(addAgentButton, "wrap");

        deleteAgentButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/minus.png")));
        buttonsPanel.add(deleteAgentButton, "wrap");

        editAgentButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/edit.png")));
        buttonsPanel.add(editAgentButton);

        return agentPanel;
    }

    private JPanel createStimulusPanel() {
        JPanel stimuliPanel = new JPanel(new MigLayout(PANEL_INSETS));
        stimuliPanel.setBorder(BorderFactory.createTitledBorder(translation.get("gui.stimuli_configuration")));

        stimuliTable = new JTable();

        JScrollPane scrollTable = new JScrollPane(stimuliTable);
        stimuliPanel.add(scrollTable, "w 100%, h 100%");

        JPanel buttonsPanel = new JPanel(new MigLayout("insets 0"));
        stimuliPanel.add(buttonsPanel, "h 100%, wrap");

        addStimulusButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/plus.png")));
        buttonsPanel.add(addStimulusButton, "wrap");

        deleteStimulusButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/minus.png")));
        buttonsPanel.add(deleteStimulusButton, "wrap");

        editStimulusButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/edit.png")));
        buttonsPanel.add(editStimulusButton);

        return stimuliPanel;
    }

    private JPanel createCurrentAgentStatePanel() {
        JPanel stimuliPanel = new JPanel(new MigLayout(PANEL_INSETS));
        stimuliPanel.setBorder(BorderFactory.createTitledBorder(translation.get("gui.current_emotional_states")));

        currentAgentStatesTable = new JTable();

        JScrollPane scrollTable = new JScrollPane(currentAgentStatesTable);
        stimuliPanel.add(scrollTable, "w 100%, h 100%");

        JPanel buttonsPanel = new JPanel(new MigLayout("insets 0"));
        stimuliPanel.add(buttonsPanel, "h 100%, wrap");

        showAgentStateButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/window.png")));
        buttonsPanel.add(showAgentStateButton, "wrap");

        return stimuliPanel;
    }

    public JButton getAddStimulusButton() {
        return addStimulusButton;
    }

    public JButton getDeleteStimulusButton() {
        return deleteStimulusButton;
    }

    public JButton getEditStimulusButton() {
        return editStimulusButton;
    }

    public JButton getAddAgentButton() {
        return addAgentButton;
    }

    public JButton getDeleteAgentButton() {
        return deleteAgentButton;
    }

    public JButton getEditAgentButton() {
        return editAgentButton;
    }

    public JButton getShowAgentStateButton() {
        return showAgentStateButton;
    }

    public JTable getStimuliTable() {
        return stimuliTable;
    }

    public JTable getAgentsTable() {
        return agentsTable;
    }

    public JLabel getIterationLabel() {
        return iterationLabel;
    }

    public JLabel getCollectiveCentralEmotionalStateLabel() {
        return collectiveCentralEmotionalStateLabel;
    }

    public JLabel getCollectiveCentralEmotionLabel() {
        return collectiveCentralEmotionLabel;
    }

    public JLabel getMaxDistanceEmotionValueLabel() {
        return maxDistanceEmotionValueLabel;
    }

    public JLabel getEmotionalDispersionValueLabel() {
        return emotionalDispersionValueLabel;
    }

    public JButton getPlayButton() {
        return playButton;
    }

    public JButton getPauseButton() {
        return pauseButton;
    }

    public JButton getRefreshButton() {
        return refreshButton;
    }

    public JSpinner getIterationsSpinner() {
        return iterationsSpinner;
    }

    public JCheckBox getCentralEmotionCheckBox() {
        return centralEmotionCheckBox;
    }

    public JCheckBox getMaximumDistanceCheckBox() {
        return maximumDistanceCheckBox;
    }

    public JCheckBox getEmotionalDispersionCheckBox() {
        return emotionalDispersionCheckBox;
    }

    public JCheckBox getEmotionalStatesCheckBox() {
        return emotionalStatesCheckBox;
    }

    public JCheckBox getBehavioursCheckBox() {
        return behavioursCheckBox;
    }

    public JCheckBox getEmotionsCheckBox() {
        return emotionsCheckBox;
    }

    public JTable getCurrentAgentStatesTable() {
        return currentAgentStatesTable;
    }

}
