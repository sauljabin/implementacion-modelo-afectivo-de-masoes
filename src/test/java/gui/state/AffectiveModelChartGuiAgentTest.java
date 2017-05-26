/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.state;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class AffectiveModelChartGuiAgentTest {

    private AffectiveModelChartGui affectiveModelChartGuiMock;
    private AffectiveModelChartGuiAgent affectiveModelChartGuiAgent;

    @Before
    public void setUp() throws Exception {
        affectiveModelChartGuiMock = mock(AffectiveModelChartGui.class);

        affectiveModelChartGuiAgent = new AffectiveModelChartGuiAgent();
        setFieldValue(affectiveModelChartGuiAgent, "affectiveModelChartGui", affectiveModelChartGuiMock);
    }

    @Test
    public void shouldInvokeCloseGuiWhenDeleteAgent() {
        affectiveModelChartGuiAgent.takeDown();
        verify(affectiveModelChartGuiMock).closeGui();
    }

}