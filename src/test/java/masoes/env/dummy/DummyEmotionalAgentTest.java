/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.env.dummy;

import masoes.core.EmotionalModel;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;


public class DummyEmotionalAgentTest {

    private DummyEmotionalAgent dummyEmotionalAgent;

    @Before
    public void setUp() {
        dummyEmotionalAgent = new DummyEmotionalAgent();
    }

    @Test
    public void shouldReturnCorrectEmotionalModel() {
        EmotionalModel actualEmotionalModel = dummyEmotionalAgent.createEmotionalModel();
        assertThat(actualEmotionalModel.getEmotionalConfigurator(), is(instanceOf(DummyEmotionalConfigurator.class)));
        assertThat(actualEmotionalModel.getBehaviourManager(), is(instanceOf(DummyBehaviourManager.class)));
    }

}