/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.jade.setting;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class SettingsAgentTest {

    private SettingsAgent mockSettingsAgent;

    @Before
    public void setUp() {
        mockSettingsAgent = mock(SettingsAgent.class);
        doNothing().when(mockSettingsAgent).addBehaviour(any(SettingsBehaviour.class));
        doCallRealMethod().when(mockSettingsAgent).setup();
    }

    @Test
    public void shouldAddSettingsBehaviour() {
        mockSettingsAgent.setup();
        verify(mockSettingsAgent).addBehaviour(any(SettingsBehaviour.class));
    }

}