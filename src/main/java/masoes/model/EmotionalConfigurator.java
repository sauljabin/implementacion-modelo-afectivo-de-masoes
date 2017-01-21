/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.model;

import masoes.ontology.Stimulus;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class EmotionalConfigurator {

    private EmotionalState emotionalState;
    private EmotionalSpace emotionalSpace;

    public EmotionalConfigurator() {
        emotionalState = new EmotionalState();
        emotionalSpace = new EmotionalSpace();
    }

    public Emotion getEmotion() {
        return emotionalSpace.searchEmotion(emotionalState);
    }

    public EmotionalState getEmotionalState() {
        return emotionalState;
    }

    public void updateEmotion(Stimulus stimulus) {
        emotionalState = calculateEmotionalState(stimulus);
    }

    protected EmotionalState calculateEmotionalState(Stimulus stimulus) {
        return new EmotionalState();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("emotionalState", emotionalState)
                .toString();
    }

}
