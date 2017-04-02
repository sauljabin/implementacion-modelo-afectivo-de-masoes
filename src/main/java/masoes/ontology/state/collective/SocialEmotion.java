/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.ontology.state.collective;

import jade.content.Predicate;
import util.ToStringBuilder;

public class SocialEmotion implements Predicate {

    private CentralEmotion centralEmotion;
    private EmotionalDispersion emotionalDispersion;
    private MaximumDistances maximumDistances;

    public SocialEmotion() {
    }

    public SocialEmotion(CentralEmotion centralEmotion, EmotionalDispersion emotionalDispersion, MaximumDistances maximumDistances) {
        this.centralEmotion = centralEmotion;
        this.emotionalDispersion = emotionalDispersion;
        this.maximumDistances = maximumDistances;
    }

    public CentralEmotion getCentralEmotion() {
        return centralEmotion;
    }

    public void setCentralEmotion(CentralEmotion centralEmotion) {
        this.centralEmotion = centralEmotion;
    }

    public EmotionalDispersion getEmotionalDispersion() {
        return emotionalDispersion;
    }

    public void setEmotionalDispersion(EmotionalDispersion emotionalDispersion) {
        this.emotionalDispersion = emotionalDispersion;
    }

    public MaximumDistances getMaximumDistances() {
        return maximumDistances;
    }

    public void setMaximumDistances(MaximumDistances maximumDistances) {
        this.maximumDistances = maximumDistances;
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("centralEmotion", centralEmotion)
                .append("emotionalDispersion", emotionalDispersion)
                .append("maximumDistances", maximumDistances)
                .toString();
    }

}
