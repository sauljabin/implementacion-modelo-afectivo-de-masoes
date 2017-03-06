/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.creator;

import environment.wikipedia.EmotionalSpaceChart;
import masoes.EmotionalBehaviour;
import masoes.behavioural.BehaviourType;
import masoes.behavioural.Emotion;
import masoes.behavioural.EmotionType;
import masoes.behavioural.EmotionalState;
import masoes.behavioural.emotion.AdmirationEmotion;
import masoes.behavioural.emotion.AngerEmotion;
import masoes.behavioural.emotion.CompassionEmotion;
import masoes.behavioural.emotion.DepressionEmotion;
import masoes.behavioural.emotion.HappinessEmotion;
import masoes.behavioural.emotion.JoyEmotion;
import masoes.behavioural.emotion.RejectionEmotion;
import masoes.behavioural.emotion.SadnessEmotion;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;

public class CreatorUserAgentGui extends JFrame {

    private static final String INSETS_10 = "insets 10";
    private static final String FIELD_W = "grow, wrap 15";
    private JTextArea eventsTextArea;
    private EmotionalSpaceChart emotionalSpaceChart;
    private JLabel agentNameLabel;
    private JLabel emotionNameLabel;
    private JLabel behaviourNameLabel;
    private JLabel emotionTypeNameLabel;
    private JLabel activationValueLabel;
    private JLabel satisfactionValueLabel;

    public CreatorUserAgentGui() {
        setSize(440, 760);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());
        setTitle("Usuario Creador de Contenido");
        addComponents();
        setLocationRelativeTo(this);
    }

    public static void main(String[] args) {
        CreatorUserAgentGui creatorUserAgentGui = new CreatorUserAgentGui();
        creatorUserAgentGui.showGui();
    }

    private void addComponents() {
        addCenterComponent();
        addSouthComponents();
        addEastComponents();
    }

    private void addEastComponents() {
        JPanel eastPanel = new JPanel(new MigLayout(INSETS_10));
        add(eastPanel, BorderLayout.EAST);
        JButton case1Button = new JButton("Caso 1");
        eastPanel.add(case1Button, "wrap");

        JButton case2Button = new JButton("Caso 2");
        eastPanel.add(case2Button, "wrap");

        JButton case3Button = new JButton("Caso 3");
        eastPanel.add(case3Button, "wrap");
    }

    private void addCenterComponent() {
        Font font14 = new Font("Arial", Font.BOLD, 14);
        Font font12 = new Font("Arial", Font.BOLD, 12);

        JPanel centerPanel = new JPanel(new MigLayout(INSETS_10));
        add(centerPanel, BorderLayout.CENTER);

        JLabel agentLabel = new JLabel("Agente:");
        centerPanel.add(agentLabel, "w 100");

        agentNameLabel = new JLabel();
        agentNameLabel.setFont(font14);
        centerPanel.add(agentNameLabel, FIELD_W);

        JLabel emotionLabel = new JLabel("Emoción:");
        centerPanel.add(emotionLabel, "grow");

        emotionNameLabel = new JLabel();
        emotionNameLabel.setFont(font14);
        centerPanel.add(emotionNameLabel, FIELD_W);

        JLabel emotionTypeLabel = new JLabel("Tipo de emoción:");
        centerPanel.add(emotionTypeLabel, "grow");

        emotionTypeNameLabel = new JLabel();
        emotionTypeNameLabel.setFont(font14);
        centerPanel.add(emotionTypeNameLabel, FIELD_W);

        JLabel behaviourLabel = new JLabel("Comportamiento:");
        centerPanel.add(behaviourLabel, "grow");

        behaviourNameLabel = new JLabel();
        behaviourNameLabel.setFont(font14);
        centerPanel.add(behaviourNameLabel, FIELD_W);

        JLabel activationLabel = new JLabel("Activación (x):");
        centerPanel.add(activationLabel, "grow");

        activationValueLabel = new JLabel();
        activationValueLabel.setFont(font14);
        centerPanel.add(activationValueLabel, FIELD_W);

        JLabel satisfactionLabel = new JLabel("Satisfación (y):");
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
        JLabel legendActualEmotion = new JLabel("Emoción actual");
        legendActualEmotion.setFont(font12);
        legendPanel.add(legendActualEmotion);

        JLabel redLabel = new JLabel();
        redLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        redLabel.setBackground(Color.RED);
        redLabel.setOpaque(true);
        legendPanel.add(redLabel, "w 30,  grow");
        JLabel legendLastEmotions = new JLabel("Emociones anteriores");
        legendLastEmotions.setFont(font12);
        legendPanel.add(legendLastEmotions);
    }

    private void addSouthComponents() {
        JPanel southPanel = new JPanel(new MigLayout(INSETS_10));
        add(southPanel, BorderLayout.SOUTH);

        JLabel eventsLabel = new JLabel("Eventos:");
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
        if (type.equals(EmotionType.NEGATIVE_HIGH)) {
            emotionTypeNameLabel.setText("ALTAMENTE NEGATIVA");
        } else if (type.equals(EmotionType.NEGATIVE_LOW)) {
            emotionTypeNameLabel.setText("NEGATIVA");
        } else if (type.equals(EmotionType.POSITIVE)) {
            emotionTypeNameLabel.setText("POSITIVA");
        }

        if (emotion instanceof HappinessEmotion) {
            emotionNameLabel.setText("FELICIDAD");
        } else if (emotion instanceof JoyEmotion) {
            emotionNameLabel.setText("ALEGRIA");
        } else if (emotion instanceof CompassionEmotion) {
            emotionNameLabel.setText("COMPASIÓN");
        } else if (emotion instanceof AdmirationEmotion) {
            emotionNameLabel.setText("ADMIRACIÓN");
        } else if (emotion instanceof SadnessEmotion) {
            emotionNameLabel.setText("TRISTEZA");
        } else if (emotion instanceof DepressionEmotion) {
            emotionNameLabel.setText("DEPRESIÓN");
        } else if (emotion instanceof RejectionEmotion) {
            emotionNameLabel.setText("RECHAZO");
        } else if (emotion instanceof AngerEmotion) {
            emotionNameLabel.setText("IRA");
        }

    }

    public void setBehaviour(EmotionalBehaviour behaviour) {
        BehaviourType type = behaviour.getType();
        if (type.equals(BehaviourType.COGNITIVE)) {
            behaviourNameLabel.setText("COGNITIVO");
        } else if (type.equals(BehaviourType.REACTIVE)) {
            behaviourNameLabel.setText("REACTIVO");
        } else if (type.equals(BehaviourType.IMITATIVE)) {
            behaviourNameLabel.setText("IMITATIVO");
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
