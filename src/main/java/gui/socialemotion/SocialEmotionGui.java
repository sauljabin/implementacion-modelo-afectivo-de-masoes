/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.socialemotion;

import gui.chart.affectivemodel.AffectiveModelChart;
import masoes.ontology.state.collective.SocialEmotion;
import net.miginfocom.swing.MigLayout;
import translate.Translation;

import javax.swing.*;
import java.awt.*;

public class SocialEmotionGui extends JFrame {

    private static final String INSETS_10 = "insets 10";
    private static final Font FONT_9 = new Font("Arial", Font.BOLD, 9);
    private AffectiveModelChart affectiveModelChart;
    private Translation translation = Translation.getInstance();
    private JLabel centralEmotionalValueLabel;
    private JLabel centralEmotionNameLabel;
    private JLabel maximumDistanceValueLabel;
    private JLabel emotionalDispersionValueLabel;

    public SocialEmotionGui() {
        setSize(400, 590);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());
        setTitle(translation.get("gui.social_emotion"));
        addComponents();
        setLocationRelativeTo(this);
    }

    public static void main(String[] args) {
        SocialEmotionGui agentStateGui = new SocialEmotionGui();
        agentStateGui.showGui();
    }

    private void addComponents() {
        addCenterComponent();
    }

    private void addCenterComponent() {
        JPanel centerPanel = new JPanel(new MigLayout(INSETS_10));
        add(centerPanel, BorderLayout.CENTER);

        JLabel centralEmotionLabel = new JLabel(translation.get("gui.central_emotion"));
        centerPanel.add(centralEmotionLabel);

        centralEmotionalValueLabel = new JLabel("-");
        centralEmotionalValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        centerPanel.add(centralEmotionalValueLabel, "w 100%, wrap 15");

        centralEmotionNameLabel = new JLabel("-");
        centralEmotionNameLabel.setFont(FONT_9);
        centralEmotionNameLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        centralEmotionNameLabel.setBackground(new Color(210, 210, 210));
        centralEmotionNameLabel.setOpaque(true);
        centralEmotionNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        centralEmotionNameLabel.setMinimumSize(new Dimension(0, 25));
        centerPanel.add(centralEmotionNameLabel, "w 100%, span 2, wrap 15");

        JLabel maxDistanceEmotionLabel = new JLabel(translation.get("gui.maximum_distance"));
        centerPanel.add(maxDistanceEmotionLabel);

        maximumDistanceValueLabel = new JLabel("-");
        maximumDistanceValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        maximumDistanceValueLabel.setMinimumSize(new Dimension(0, 25));
        centerPanel.add(maximumDistanceValueLabel, "w 100%, wrap 15");

        JLabel emotionalDispersionLabel = new JLabel(translation.get("gui.emotional_dispersion"));
        centerPanel.add(emotionalDispersionLabel);

        emotionalDispersionValueLabel = new JLabel("-");
        emotionalDispersionValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        emotionalDispersionValueLabel.setMinimumSize(new Dimension(0, 25));
        centerPanel.add(emotionalDispersionValueLabel, "w 100%, wrap 15");

        affectiveModelChart = new AffectiveModelChart();
        centerPanel.add(affectiveModelChart, "w 380, h 380, span 2, wrap");

        JPanel legendPanel = new JPanel(new MigLayout("insets 0"));
        centerPanel.add(legendPanel, "span 2, wrap");

        JLabel legend = new JLabel();
        legend.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        legend.setBackground(Color.RED);
        legend.setOpaque(true);
        legendPanel.add(legend, "w 30, grow");
        JLabel legendActualEmotion = new JLabel(translation.get("gui.current_emotion"));
        legendPanel.add(legendActualEmotion);
    }

    public void setSocialEmotion(SocialEmotion socialEmotion) {
        if (socialEmotion.getCentralEmotion() == null
                || socialEmotion.getMaximumDistance() == null
                || socialEmotion.getEmotionalDispersion() == null) {

            centralEmotionalValueLabel.setText("-");
            centralEmotionNameLabel.setText("-");
            emotionalDispersionValueLabel.setText("-");
            maximumDistanceValueLabel.setText("-");
            affectiveModelChart.clear();
            return;
        }

        centralEmotionalValueLabel.setText(socialEmotion.getCentralEmotion().toStringPoint());

        centralEmotionNameLabel.setText(
                translation.get(socialEmotion.getCentralEmotion().getEmotion().getName().toLowerCase()) +
                        " - " +
                        translation.get(socialEmotion.getCentralEmotion().getEmotion().getType().toString().toLowerCase())
        );

        emotionalDispersionValueLabel.setText(socialEmotion.getEmotionalDispersion().toStringPoint());

        maximumDistanceValueLabel.setText(socialEmotion.getMaximumDistance().toStringPoint());

        affectiveModelChart.setEmotionalState(socialEmotion.getCentralEmotion().toEmotionalState());
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
