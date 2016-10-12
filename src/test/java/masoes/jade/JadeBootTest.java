/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.jade;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import masoes.app.setting.Setting;
import masoes.app.setting.SettingsLoader;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class JadeBootTest {

    private ProfileImpl bootProfile;
    private Runtime mockRuntime;
    private JadeBoot jadeBoot;
    private SettingsLoader settingsLoader = SettingsLoader.getInstance();

    @Before
    public void setUp() {
        settingsLoader.load();
        bootProfile = mock(ProfileImpl.class);
        mockRuntime = mock(Runtime.class);
        jadeBoot = new JadeBoot(bootProfile, mockRuntime);
    }

    @Test
    public void shouldSetCorrectProfileVariables() {
        String expectedArguments = "arguments";

        jadeBoot.boot(expectedArguments);

        verify(bootProfile).setParameter("gui", "true");
        verify(bootProfile).setParameter("agents", expectedArguments);
        verify(bootProfile).setParameter("port", Setting.JADE_PORT.getValue());
        verify(mockRuntime).setCloseVM(true);
        verify(mockRuntime).createMainContainer(bootProfile);
    }

}