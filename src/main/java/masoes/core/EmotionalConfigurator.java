/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core;

import java.util.List;

import com.vividsolutions.jts.geom.Point;

public interface EmotionalConfigurator {

	List<Emotion> getEmotions();

	Point getEmotionalPoint();

	Emotion getEmotionalState();

	void evaluateStimulus(Stimulus stimulus);
}
