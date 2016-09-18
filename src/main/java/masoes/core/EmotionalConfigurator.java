/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core;

import com.vividsolutions.jts.geom.Point;

import java.util.List;

public abstract class EmotionalConfigurator {

    public abstract List<Emotion> getEmotions();

    public abstract Point getEmotionalPoint();

    public abstract Emotion getEmotionalState();

    public abstract void evaluateStimulus(Stimulus stimulus);

    public abstract double getActivation();

    public abstract double getSatisfaction();

}
