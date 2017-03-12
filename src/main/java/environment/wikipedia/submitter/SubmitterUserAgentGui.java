/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.submitter;

import environment.wikipedia.EmotionalSpaceChart;
import masoes.EmotionalBehaviour;
import masoes.behavioural.BehaviourType;
import masoes.behavioural.Emotion;
import masoes.behavioural.EmotionType;
import masoes.behavioural.EmotionalState;
import net.miginfocom.swing.MigLayout;
import translate.Translation;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;

public class SubmitterUserAgentGui extends JFrame {

    private static final String INSETS_10 = "insets 10";
    private static final String FIELD_W = "grow, wrap 15";
    private static final Color ORANGE_COLOR = new Color(255, 110, 40);
    private static final Color GREEN_COLOR = new Color(62, 200, 100);
    private JTextArea eventsTextArea;
    private EmotionalSpaceChart emotionalSpaceChart;
    private JLabel agentNameLabel;
    private JLabel emotionNameLabel;
    private JLabel behaviourNameLabel;
    private JLabel emotionTypeNameLabel;
    private JLabel activationValueLabel;
    private JLabel satisfactionValueLabel;
    private Translation translation;

    public SubmitterUserAgentGui() {
        translation = Translation.getInstance();
        setSize(440, 740);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());
        setTitle(translation.get("gui.user"));
        addComponents();
        setLocationRelativeTo(this);
    }

    public static void main(String[] args) {
        SubmitterUserAgentGui submitterUserAgentGui = new SubmitterUserAgentGui();
        submitterUserAgentGui.showGui();
    }

    private void addComponents() {
        addCenterComponent();
        addSouthComponents();
        addEastComponents();
    }

    private void addEastComponents() {
        JPanel eastPanel = new JPanel(new MigLayout(INSETS_10));
        add(eastPanel, BorderLayout.EAST);

        JButton case1Button = new JButton(translation.get("gui.case_1"));
        eastPanel.add(case1Button, "wrap");

        JButton case2Button = new JButton(translation.get("gui.case_2"));
        eastPanel.add(case2Button, "wrap");

        JButton case3Button = new JButton(translation.get("gui.case_3"));
        eastPanel.add(case3Button, "wrap");
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

        JLabel behaviourLabel = new JLabel(translation.get("gui.behaviour"));
        centerPanel.add(behaviourLabel, "grow");

        behaviourNameLabel = new JLabel();
        behaviourNameLabel.setFont(font14);
        centerPanel.add(behaviourNameLabel, FIELD_W);

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

        emotionalSpaceChart = new EmotionalSpaceChart();
        centerPanel.add(emotionalSpaceChart, "w 300, h 300, span 2, wrap");

        JPanel legendPanel = new JPanel(new MigLayout("insets 0"));
        centerPanel.add(legendPanel, "span 2, wrap");

        JLabel blueLabel = new JLabel();
        blueLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        blueLabel.setBackground(Color.BLUE);
        blueLabel.setOpaque(true);
        legendPanel.add(blueLabel, "w 30, grow");
        JLabel legendActualEmotion = new JLabel(translation.get("gui.current_emotion"));
        legendActualEmotion.setFont(font12);
        legendPanel.add(legendActualEmotion);

        JLabel redLabel = new JLabel();
        redLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        redLabel.setBackground(Color.RED);
        redLabel.setOpaque(true);
        legendPanel.add(redLabel, "w 30,  grow");
        JLabel legendLastEmotions = new JLabel(translation.get("gui.previous_emotions"));
        legendLastEmotions.setFont(font12);
        legendPanel.add(legendLastEmotions);
    }

    private void addSouthComponents() {
        JPanel southPanel = new JPanel(new MigLayout(INSETS_10));
        add(southPanel, BorderLayout.SOUTH);

        JLabel eventsLabel = new JLabel(translation.get("gui.events"));
        southPanel.add(eventsLabel, "w 100%, wrap");

        eventsTextArea = new JTextArea();
        eventsTextArea.setEditable(false);
        DefaultCaret caret = (DefaultCaret) eventsTextArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        JScrollPane scrollPane = new JScrollPane(eventsTextArea);
        southPanel.add(scrollPane, "w 100%, h 150");
    }

    public void addEmotionalState(EmotionalState emotionalState) {
        emotionalSpaceChart.addEmotionalState(emotionalState);
        satisfactionValueLabel.setText(String.format("%.5f", emotionalState.getSatisfaction()));
        activationValueLabel.setText(String.format("%.5f", emotionalState.getActivation()));
    }

    public void logEvent(String message) {
        eventsTextArea.append(String.format("- %s\n", message));
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

        if (type.equals(EmotionType.NEGATIVE_HIGH)) {
            emotionTypeNameLabel.setForeground(Color.RED);
            emotionNameLabel.setForeground(Color.RED);
        } else if (type.equals(EmotionType.NEGATIVE_LOW)) {
            emotionTypeNameLabel.setForeground(ORANGE_COLOR);
            emotionNameLabel.setForeground(ORANGE_COLOR);
        } else if (type.equals(EmotionType.POSITIVE)) {
            emotionTypeNameLabel.setForeground(GREEN_COLOR);
            emotionNameLabel.setForeground(GREEN_COLOR);
        }
    }

    public void setBehaviour(EmotionalBehaviour behaviour) {
        BehaviourType type = behaviour.getType();
        String typeName = translation.get(type.toString().toLowerCase());
        behaviourNameLabel.setText(typeName);

        if (type.equals(BehaviourType.COGNITIVE)) {
            behaviourNameLabel.setForeground(ORANGE_COLOR);
        } else if (type.equals(BehaviourType.REACTIVE)) {
            behaviourNameLabel.setForeground(Color.RED);
        } else if (type.equals(BehaviourType.IMITATIVE)) {
            behaviourNameLabel.setForeground(GREEN_COLOR);
        }
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

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

}
