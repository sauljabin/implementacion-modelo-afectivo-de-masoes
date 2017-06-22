/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.configurator.agent;

import net.miginfocom.swing.MigLayout;
import translate.Translation;

import javax.swing.*;
import java.awt.*;

public class AgentView extends JFrame {

    private static final String FIELDS_SIZE = "w 100%, h 30, wrap";
    private static final String EMOTION_FIELDS_SIZE = "w 100%, h 30";
    private static final String PANELS_SIZE = "w 100%, wrap";

    private Translation translation = Translation.getInstance();
    private JComboBox<AgentType> agentTypesCombo;
    private JTextField nameField;
    private JSpinner activationSpinner;
    private JLabel emotionLabel;
    private JSpinner satisfactionSpinner;
    private JButton activationRandomButton;
    private JButton satisfactionRandomButton;

    public AgentView() {
        setTitle(translation.get("gui.agent"));
        setSize(400, 500);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new MigLayout());
        add(mainPanel, BorderLayout.CENTER);

        mainPanel.add(createAgentFieldsPanel(), PANELS_SIZE);

        mainPanel.add(createInitialEmotionPanel(), PANELS_SIZE);

        /*
        mainPanel.add(createButtonsPanel(), PANELS_SIZE);*/

        setLocationRelativeTo(this);
    }

    private JPanel createInitialEmotionPanel() {
        JPanel emotionPanel = new JPanel(new MigLayout("insets 0"));

        JLabel activationLabel = new JLabel(translation.get("gui.activation_x"));
        emotionPanel.add(activationLabel);

        activationSpinner = new JSpinner();
        activationSpinner.setModel(new SpinnerNumberModel(0, -1., 1., .01));
        emotionPanel.add(activationSpinner, EMOTION_FIELDS_SIZE);

        activationRandomButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/random.png")));
        activationRandomButton.setMaximumSize(new Dimension(30, 30));
        emotionPanel.add(activationRandomButton, "h 30, w 30, wrap");

        JLabel satisfactionLabel = new JLabel(translation.get("gui.satisfaction_y"));
        emotionPanel.add(satisfactionLabel);

        satisfactionSpinner = new JSpinner();
        satisfactionSpinner.setModel(new SpinnerNumberModel(0, -1., 1., .01));
        emotionPanel.add(satisfactionSpinner, EMOTION_FIELDS_SIZE);

        satisfactionRandomButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/random.png")));
        satisfactionRandomButton.setMaximumSize(new Dimension(30, 30));
        emotionPanel.add(satisfactionRandomButton, "h 30, w 30, wrap");

        emotionLabel = new JLabel();
        emotionLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        emotionLabel.setBackground(new Color(210, 210, 210));
        emotionLabel.setOpaque(true);
        emotionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        emotionPanel.add(emotionLabel, "h 50, w 100%, span 3, wrap");

        return emotionPanel;
    }

    private JPanel createAgentFieldsPanel() {
        JPanel agentPanel = new JPanel(new MigLayout("insets 0"));

        JLabel agentLabel = new JLabel(translation.get("gui.agent_type"));
        agentPanel.add(agentLabel);

        agentTypesCombo = new JComboBox<>(AgentType.values());
        agentPanel.add(agentTypesCombo, FIELDS_SIZE);

        JLabel nameLabel = new JLabel(translation.get("gui.name"));
        agentPanel.add(nameLabel);

        nameField = new JTextField();
        agentPanel.add(nameField, FIELDS_SIZE);

        return agentPanel;
    }

}
