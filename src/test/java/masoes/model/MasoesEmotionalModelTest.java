/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.model;

import masoes.core.BehaviorManager;
import masoes.core.EmotionalConfigurator;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class MasoesEmotionalModelTest {

    @Test
    public void shouldReturnSameObjects() {
        MasoesEmotionalModel masoesEmotionalModel = new MasoesEmotionalModel();
        BehaviorManager behaviorManager = masoesEmotionalModel.getBehaviorManager();
        EmotionalConfigurator emotionalConfigurator = masoesEmotionalModel.getEmotionalConfigurator();

        assertThat(masoesEmotionalModel.getBehaviorManager(), is(behaviorManager));
        assertThat(masoesEmotionalModel.getEmotionalConfigurator(), is(emotionalConfigurator));
    }

}