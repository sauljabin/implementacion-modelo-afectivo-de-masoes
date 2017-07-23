/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.simulator;

import net.miginfocom.swing.MigLayout;
import translate.Translation;

import javax.swing.*;
import java.awt.*;

public class SimulatorGui extends JFrame {

    private static final Font FONT_50 = new Font("Arial", Font.BOLD, 50);
    private static final Font FONT_9 = new Font("Arial", Font.BOLD, 9);

    private static final String PRINCIPAL_PANELS_SIZE = "w 100%, h 100%";
    private static final String PANELS_SIZE = "w 100%, h 100%, wrap";
    private static final String PANELS_SIZE_WITHOUT_H = "w 400, wrap";
    private static final String PANEL_INSETS = "insets 5";
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu editMenu;
    private JMenuItem editAgentTypesDefinitionMenu;

    private Translation translation = Translation.getInstance();
    private JButton addAgentButton;
    private JButton deleteAgentButton;
    private JButton editAgentButton;
    private JButton showAgentStateButton;
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
    private JMenuItem editStimuliDefinitionMenu;
    private JMenuItem importConfigurationMenu;
    private JMenuItem exportConfigurationMenu;
    private JButton addMultipleAgentsButton;

    public SimulatorGui() {
        setTitle(translation.get("gui.simulator"));
        setSize(1200, 780);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());

        configMenu();

        JPanel mainPanel = new JPanel(new MigLayout());
        add(mainPanel, BorderLayout.CENTER);

        JPanel leftPanel = new JPanel(new MigLayout("insets 10 10 10 0"));
        mainPanel.add(leftPanel, PRINCIPAL_PANELS_SIZE);

        leftPanel.add(createAgentsPanel(), PANELS_SIZE);
        leftPanel.add(createCurrentAgentStatePanel(), PANELS_SIZE);

        JPanel rightPanel = new JPanel(new MigLayout("insets 10"));
        mainPanel.add(rightPanel, "h 100%");

        rightPanel.add(createControlPanel(), PANELS_SIZE_WITHOUT_H);
        rightPanel.add(createSelectChartsPanel(), PANELS_SIZE_WITHOUT_H);
        rightPanel.add(createIterationPanel(), PANELS_SIZE_WITHOUT_H);
        rightPanel.add(createCollectiveEmotionPanel(), PANELS_SIZE_WITHOUT_H);

        setLocationRelativeTo(this);
    }

    public static void main(String[] args) {
        SimulatorGui simulatorGui = new SimulatorGui();
        simulatorGui.setVisible(true);
    }

    private void configMenu() {
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        fileMenu = new JMenu(translation.get("gui.file"));
        menuBar.add(fileMenu);

        importConfigurationMenu = new JMenuItem(translation.get("gui.import_configuration"));
        fileMenu.add(importConfigurationMenu);

        exportConfigurationMenu = new JMenuItem(translation.get("gui.export_configuration"));
        fileMenu.add(exportConfigurationMenu);

        editMenu = new JMenu(translation.get("gui.edit"));
        menuBar.add(editMenu);

        editAgentTypesDefinitionMenu = new JMenuItem(translation.get("gui.emotional_agent_types_definition"));
        editMenu.add(editAgentTypesDefinitionMenu);

        editStimuliDefinitionMenu = new JMenuItem(translation.get("gui.stimuli_definition"));
        editMenu.add(editStimuliDefinitionMenu);
    }

    private JPanel createSelectChartsPanel() {
        JPanel chartsButtonsPanel = new JPanel(new MigLayout(PANEL_INSETS));
        chartsButtonsPanel.setBorder(BorderFactory.createTitledBorder(translation.get("gui.real_time_charts")));

        emotionalStatesCheckBox = new JCheckBox(translation.get("gui.emotional_states"), false);
        chartsButtonsPanel.add(emotionalStatesCheckBox);

        emotionsCheckBox = new JCheckBox(translation.get("gui.emotions"), false);
        chartsButtonsPanel.add(emotionsCheckBox, "wrap");

        behavioursCheckBox = new JCheckBox(translation.get("gui.behaviours"), false);
        chartsButtonsPanel.add(behavioursCheckBox);

        centralEmotionCheckBox = new JCheckBox(translation.get("gui.central_emotion"), false);
        chartsButtonsPanel.add(centralEmotionCheckBox, "wrap");

        maximumDistanceCheckBox = new JCheckBox(translation.get("gui.maximum_distance"), false);
        chartsButtonsPanel.add(maximumDistanceCheckBox);

        emotionalDispersionCheckBox = new JCheckBox(translation.get("gui.emotional_dispersion"), false);
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
        collectiveCentralEmotionalStateLabel.setHorizontalAlignment(SwingConstants.CENTER);
        collectiveEmotionPanel.add(collectiveCentralEmotionalStateLabel, "w 100%, wrap");

        collectiveCentralEmotionLabel = new JLabel("-");
        collectiveCentralEmotionLabel.setFont(FONT_9);
        collectiveCentralEmotionLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        collectiveCentralEmotionLabel.setBackground(new Color(210, 210, 210));
        collectiveCentralEmotionLabel.setOpaque(true);
        collectiveCentralEmotionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        collectiveCentralEmotionLabel.setMinimumSize(new Dimension(0, 25));
        collectiveEmotionPanel.add(collectiveCentralEmotionLabel, "w 100%, span 2, wrap");

        JLabel maxDistanceEmotionLabel = new JLabel(translation.get("gui.maximum_distance"));
        collectiveEmotionPanel.add(maxDistanceEmotionLabel);

        maxDistanceEmotionValueLabel = new JLabel("-");
        maxDistanceEmotionValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        maxDistanceEmotionValueLabel.setMinimumSize(new Dimension(0, 25));
        collectiveEmotionPanel.add(maxDistanceEmotionValueLabel, "w 100%, wrap");

        JLabel emotionalDispersionLabel = new JLabel(translation.get("gui.emotional_dispersion"));
        collectiveEmotionPanel.add(emotionalDispersionLabel);

        emotionalDispersionValueLabel = new JLabel("-");
        emotionalDispersionValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        emotionalDispersionValueLabel.setMinimumSize(new Dimension(0, 25));
        collectiveEmotionPanel.add(emotionalDispersionValueLabel, "w 100%, wrap");

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

        addMultipleAgentsButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/wand-plus.png")));
        buttonsPanel.add(addMultipleAgentsButton, "wrap");

        deleteAgentButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/minus.png")));
        buttonsPanel.add(deleteAgentButton, "wrap");

        editAgentButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/edit.png")));
        buttonsPanel.add(editAgentButton);

        return agentPanel;
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

    public JMenuItem getEditAgentTypesDefinitionMenu() {
        return editAgentTypesDefinitionMenu;
    }

    public JMenuItem getEditStimuliDefinitionMenu() {
        return editStimuliDefinitionMenu;
    }

    public JMenu getFileMenu() {
        return fileMenu;
    }

    public JMenu getEditMenu() {
        return editMenu;
    }

    public JMenuItem getImportConfigurationMenu() {
        return importConfigurationMenu;
    }

    public JMenuItem getExportConfigurationMenu() {
        return exportConfigurationMenu;
    }

    public JButton getAddMultipleAgentsButton() {
        return addMultipleAgentsButton;
    }

}
