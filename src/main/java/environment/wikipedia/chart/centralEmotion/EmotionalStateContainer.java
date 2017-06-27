/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.chart.centralEmotion;

import masoes.component.behavioural.EmotionalState;
import util.Colors;

import javax.swing.*;
import java.awt.*;

public class EmotionalStateContainer {

    private String agentName;
    private int sequence;
    private Color color;
    private EmotionalState emotionalState;
    private JLabel label;
    private boolean isCentralEmotion;

    public EmotionalStateContainer(String agentName, int sequence) {
        this.agentName = agentName;
        this.sequence = sequence;
    }

    public String getAgentName() {
        return agentName;
    }

    public Color getColor() {
        if (color == null) {
            color = Colors.getColor(sequence);
        }
        return color;
    }

    public EmotionalState getEmotionalState() {
        return emotionalState;
    }

    public void setEmotionalState(EmotionalState emotionalState) {
        this.emotionalState = emotionalState;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof EmotionalStateContainer)) {
            return false;
        }
        return getAgentName().equals(((EmotionalStateContainer) obj).getAgentName());
    }

    public boolean sameName(String name) {
        return getAgentName().equals(name);
    }

    public JLabel getLabel() {
        if (label == null) {
            label = new JLabel(getAgentName());
            label.setForeground(getColor());
        }
        return label;
    }

    public boolean isCentralEmotion() {
        return isCentralEmotion;
    }

    public void setCentralEmotion(boolean centralEmotion) {
        isCentralEmotion = centralEmotion;
    }

}
