/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.state;

import masoes.component.behavioural.EmotionalState;
import masoes.ontology.state.AgentState;
import net.miginfocom.swing.MigLayout;
import translate.Translation;

import javax.swing.*;
import java.awt.*;

public class AffectiveModelChartGui extends JFrame {

    private static final String INSETS_10 = "insets 10";
    private static final String FIELD_W = "grow, wrap 15";
    private AffectiveModelChart affectiveModelChart;
    private JLabel agentNameLabel;
    private JLabel emotionNameLabel;
    private JLabel behaviourTypeLabel;
    private JLabel emotionTypeNameLabel;
    private JLabel activationValueLabel;
    private JLabel satisfactionValueLabel;
    private Translation translation;

    public AffectiveModelChartGui() {
        translation = Translation.getInstance();
        setSize(320, 550);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());
        setTitle(translation.get("gui.agent_state"));
        addComponents();
        setLocationRelativeTo(this);
    }

    public static void main(String[] args) {
        AffectiveModelChartGui affectiveModelChartGui = new AffectiveModelChartGui();
        affectiveModelChartGui.showGui();
    }

    private void addComponents() {
        addCenterComponent();
    }

    private void addCenterComponent() {
        JPanel centerPanel = new JPanel(new MigLayout(INSETS_10));
        add(centerPanel, BorderLayout.CENTER);

        JLabel agentLabel = new JLabel(translation.get("gui.agent"));
        centerPanel.add(agentLabel, "w 100");

        agentNameLabel = new JLabel();
        centerPanel.add(agentNameLabel, FIELD_W);

        JLabel emotionLabel = new JLabel(translation.get("gui.emotion"));
        centerPanel.add(emotionLabel, "grow");

        emotionNameLabel = new JLabel();
        centerPanel.add(emotionNameLabel, FIELD_W);

        JLabel emotionTypeLabel = new JLabel(translation.get("gui.emotion_type"));
        centerPanel.add(emotionTypeLabel, "grow");

        emotionTypeNameLabel = new JLabel();
        centerPanel.add(emotionTypeNameLabel, FIELD_W);

        JLabel activationLabel = new JLabel(translation.get("gui.activation_x"));
        centerPanel.add(activationLabel, "grow");

        activationValueLabel = new JLabel();
        centerPanel.add(activationValueLabel, FIELD_W);

        JLabel satisfactionLabel = new JLabel(translation.get("gui.satisfaction_y"));
        centerPanel.add(satisfactionLabel, "grow");

        satisfactionValueLabel = new JLabel();
        centerPanel.add(satisfactionValueLabel, FIELD_W);

        JLabel behaviourLabel = new JLabel(translation.get("gui.behaviour"));
        centerPanel.add(behaviourLabel, "grow");

        behaviourTypeLabel = new JLabel();
        centerPanel.add(behaviourTypeLabel, FIELD_W);

        affectiveModelChart = new AffectiveModelChart();
        centerPanel.add(affectiveModelChart, "w 300, h 300, span 2, wrap");

        JPanel legendPanel = new JPanel(new MigLayout("insets 0"));
        centerPanel.add(legendPanel, "span 2, wrap");

        JLabel blueLabel = new JLabel();
        blueLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        blueLabel.setBackground(Color.RED);
        blueLabel.setOpaque(true);
        legendPanel.add(blueLabel, "w 30, grow");
        JLabel legendActualEmotion = new JLabel(translation.get("gui.current_emotion"));
        legendPanel.add(legendActualEmotion);
    }

    public void setAgentState(AgentState agentState) {
        agentNameLabel.setText(agentState.getAgent().getLocalName());

        EmotionalState emotionalState = new EmotionalState(agentState.getEmotionState().getActivation(), agentState.getEmotionState().getSatisfaction());
        affectiveModelChart.setEmotionalState(emotionalState);
        satisfactionValueLabel.setText(String.format("%.3f", emotionalState.getSatisfaction()));
        activationValueLabel.setText(String.format("%.3f", emotionalState.getActivation()));

        String typeName = translation.get(agentState.getEmotionState().getType().toLowerCase());
        String name = translation.get(agentState.getEmotionState().getName().toLowerCase());

        emotionTypeNameLabel.setText(typeName);
        emotionNameLabel.setText(name);

        String behaviourTypeName = translation.get(agentState.getBehaviourState().getType().toLowerCase());
        behaviourTypeLabel.setText(behaviourTypeName);
    }

    public void closeGui() {
        affectiveModelChart.stop();
        setVisible(false);
        dispose();
    }

    public void showGui() {
        setVisible(true);
        affectiveModelChart.start();
    }

}
