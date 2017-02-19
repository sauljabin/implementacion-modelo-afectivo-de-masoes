/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.dummy;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class DummyEmotionalAgentTest {

    private DummyEmotionalAgent dummyEmotionalAgent;

    @Before
    public void setUp() {
        dummyEmotionalAgent = new DummyEmotionalAgent();
        dummyEmotionalAgent.setUp();
    }

    @Test
    public void shouldGetCorrectConfig() throws Exception {
        assertThat(dummyEmotionalAgent.getKnowledgePath(), is("theories/dummy/dummyEmotionalAgent.prolog"));
        assertThat(dummyEmotionalAgent.getCognitiveBehaviour(), is(instanceOf(DummyCognitiveBehaviour.class)));
        assertThat(dummyEmotionalAgent.getImitativeBehaviour(), is(instanceOf(DummyImitativeBehaviour.class)));
        assertThat(dummyEmotionalAgent.getReactiveBehaviour(), is(instanceOf(DummyReactiveBehaviour.class)));
    }

}