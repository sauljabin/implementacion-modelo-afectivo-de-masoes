/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.configurator;

import masoes.component.behavioural.Emotion;
import translate.Translation;

public class EmotionToAdd {

    private Emotion emotion;

    public EmotionToAdd() {
    }

    public EmotionToAdd(Emotion emotion) {
        this.emotion = emotion;
    }

    public Emotion getEmotion() {
        return emotion;
    }

    public void setEmotion(Emotion emotion) {
        this.emotion = emotion;
    }

    @Override
    public String toString() {
        return String.format("%s - %s",
                Translation.getInstance().get(emotion.getName().toLowerCase()),
                Translation.getInstance().get(emotion.getType().toString().toLowerCase())
        );
    }

}
