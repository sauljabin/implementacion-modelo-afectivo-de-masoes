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
    private JTextArea eventsTextArea;
    private EmotionalSpaceChart emotionalSpaceChart;
    private JLabel agentNameLabel;
    private JLabel emotionNameLabel;
    private JLabel behaviourNameLabel;
    private JLabel emotionTypeNameLabel;

    public CreatorUserAgentGui() {
        setSize(400, 700);
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
    }

    private void addCenterComponent() {
        Font font = new Font("Arial", Font.BOLD, 14);

        JPanel centerPanel = new JPanel(new MigLayout(INSETS_10));
        add(centerPanel, BorderLayout.CENTER);

        JLabel agentLabel = new JLabel("Agente:");
        centerPanel.add(agentLabel, "w 80");

        agentNameLabel = new JLabel();
        agentNameLabel.setFont(font);
        centerPanel.add(agentNameLabel, "w 100%, wrap 20");

        JLabel emotionalStateLabel = new JLabel("Estado emocional");
        centerPanel.add(emotionalStateLabel, "span 2, wrap");

        emotionalSpaceChart = new EmotionalSpaceChart();
        centerPanel.add(emotionalSpaceChart, "w 300, h 300, span 2, wrap 20");

        JLabel emotionLabel = new JLabel("Emoción:");
        centerPanel.add(emotionLabel, "w 80");

        emotionNameLabel = new JLabel();
        emotionNameLabel.setFont(font);
        centerPanel.add(emotionNameLabel, "w 100%, wrap 20");

        JLabel emotionTypeLabel = new JLabel("Tipo de emoción:");
        centerPanel.add(emotionTypeLabel, "w 80");

        emotionTypeNameLabel = new JLabel();
        emotionTypeNameLabel.setFont(font);
        centerPanel.add(emotionTypeNameLabel, "w 100%, wrap 20");

        JLabel behaviourLabel = new JLabel("Comportamiento:");
        centerPanel.add(behaviourLabel, "w 80");

        behaviourNameLabel = new JLabel();
        behaviourNameLabel.setFont(font);
        centerPanel.add(behaviourNameLabel, "w 100%, wrap 20");
    }

    private void addSouthComponents() {
        JPanel southPanel = new JPanel(new MigLayout(INSETS_10));
        add(southPanel, BorderLayout.SOUTH);

        JLabel eventsLabel = new JLabel("Eventos");
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
