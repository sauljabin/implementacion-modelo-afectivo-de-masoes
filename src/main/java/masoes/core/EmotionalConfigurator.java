/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core;

import com.vividsolutions.jts.geom.Point;

import java.util.List;

public interface EmotionalConfigurator {

    List<Emotion> getEmotions();

    Point getEmotionalPoint();

    Emotion getEmotionalState();

    void evaluateStimulus(Stimulus stimulus);
}
