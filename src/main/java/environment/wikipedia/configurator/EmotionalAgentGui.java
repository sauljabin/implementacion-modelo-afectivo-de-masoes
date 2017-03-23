/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.configurator;

import masoes.component.behavioural.BehaviourType;
import masoes.component.behavioural.Emotion;
import masoes.component.behavioural.EmotionType;
import masoes.component.behavioural.EmotionalState;
import net.miginfocom.swing.MigLayout;
import translate.Translation;

import javax.swing.*;
import java.awt.*;

public class EmotionalAgentGui extends JFrame {

    private static final String INSETS_10 = "insets 10";
    private static final String FIELD_W = "grow, wrap 15";
    private EmotionalSpaceChart emotionalSpaceChart;
    private JLabel agentNameLabel;
    private JLabel emotionNameLabel;
    private JLabel behaviourTypeLabel;
    private JLabel emotionTypeNameLabel;
    private JLabel activationValueLabel;
    private JLabel satisfactionValueLabel;
    private Translation translation;

    public EmotionalAgentGui() {
        translation = Translation.getInstance();
        setSize(320, 550);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());
        setTitle(translation.get("gui.agent_state"));
        addComponents();
        setLocationRelativeTo(this);
    }

    public static void main(String[] args) {
        EmotionalAgentGui emotionalAgentGui = new EmotionalAgentGui();
        emotionalAgentGui.showGui();
    }

    private void addComponents() {
        addCenterComponent();
    }

    private void addCenterComponent() {
        Font font14 = new Font("Arial", Font.BOLD, 14);
        Font font12 = new Font("Arial", Font.BOLD, 12);

        JPanel centerPanel = new JPanel(new MigLayout(INSETS_10));
        add(centerPanel, BorderLayout.CENTER);

        JLabel agentLabel = new JLabel(translation.get("gui.agent"));
        centerPanel.add(agentLabel, "w 100");

        agentNameLabel = new JLabel();
        agentNameLabel.setFont(font14);
        centerPanel.add(agentNameLabel, FIELD_W);

        JLabel emotionLabel = new JLabel(translation.get("gui.emotion"));
        centerPanel.add(emotionLabel, "grow");

        emotionNameLabel = new JLabel();
        emotionNameLabel.setFont(font14);
        centerPanel.add(emotionNameLabel, FIELD_W);

        JLabel emotionTypeLabel = new JLabel(translation.get("gui.emotion_type"));
        centerPanel.add(emotionTypeLabel, "grow");

        emotionTypeNameLabel = new JLabel();
        emotionTypeNameLabel.setFont(font14);
        centerPanel.add(emotionTypeNameLabel, FIELD_W);

        JLabel activationLabel = new JLabel(translation.get("gui.activation_x"));
        centerPanel.add(activationLabel, "grow");

        activationValueLabel = new JLabel();
        activationValueLabel.setFont(font14);
        centerPanel.add(activationValueLabel, FIELD_W);

        JLabel satisfactionLabel = new JLabel(translation.get("gui.satisfaction_y"));
        centerPanel.add(satisfactionLabel, "grow");

        satisfactionValueLabel = new JLabel();
        satisfactionValueLabel.setFont(font14);
        centerPanel.add(satisfactionValueLabel, FIELD_W);

        JLabel behaviourLabel = new JLabel(translation.get("gui.behaviour"));
        centerPanel.add(behaviourLabel, "grow");

        behaviourTypeLabel = new JLabel();
        behaviourTypeLabel.setFont(font14);
        centerPanel.add(behaviourTypeLabel, FIELD_W);

        emotionalSpaceChart = new EmotionalSpaceChart();
        centerPanel.add(emotionalSpaceChart, "w 300, h 300, span 2, wrap");

        JPanel legendPanel = new JPanel(new MigLayout("insets 0"));
        centerPanel.add(legendPanel, "span 2, wrap");

        JLabel blueLabel = new JLabel();
        blueLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        blueLabel.setBackground(Color.RED);
        blueLabel.setOpaque(true);
        legendPanel.add(blueLabel, "w 30, grow");
        JLabel legendActualEmotion = new JLabel(translation.get("gui.current_emotion"));
        legendActualEmotion.setFont(font12);
        legendPanel.add(legendActualEmotion);
    }

    public void setEmotionalState(EmotionalState emotionalState) {
        emotionalSpaceChart.setEmotionalState(emotionalState);
        satisfactionValueLabel.setText(String.format("%.3f", emotionalState.getSatisfaction()));
        activationValueLabel.setText(String.format("%.3f", emotionalState.getActivation()));
    }

    public void setAgentName(String name) {
        agentNameLabel.setText(name);
    }

    public void setEmotion(Emotion emotion) {
        EmotionType type = emotion.getType();
        String typeName = translation.get(type.toString().toLowerCase());
        String name = translation.get(emotion.getName().toLowerCase());

        emotionTypeNameLabel.setText(typeName);
        emotionNameLabel.setText(name);
    }

    public void setBehaviourType(BehaviourType type) {
        String typeName = translation.get(type.toString().toLowerCase());
        behaviourTypeLabel.setText(typeName);
    }

    public void closeGui() {
        emotionalSpaceChart.stop();
        setVisible(false);
        dispose();
    }

    public void showGui() {
        setVisible(true);
        emotionalSpaceChart.start();
    }

}