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
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class JadeBootTest {

    private ProfileImpl mockJadeProfile;
    private Runtime mockJadeRuntime;
    private JadeBoot jadeBoot;
    private SettingsLoader settingsLoader;

    @Before
    public void setUp() throws Exception {
        settingsLoader = SettingsLoader.getInstance();
        settingsLoader.load();
        mockJadeProfile = mock(ProfileImpl.class);
        mockJadeRuntime = mock(Runtime.class);
        jadeBoot = new JadeBoot();
        setFieldValue(jadeBoot, "jadeProfile", mockJadeProfile);
        setFieldValue(jadeBoot, "jadeRuntime", mockJadeRuntime);
    }

    @Test
    public void shouldSetCorrectProfileVariables() {
        String expectedArguments = "arguments";

        jadeBoot.boot(expectedArguments);

        verify(mockJadeProfile).setParameter("agents", expectedArguments);
        verify(mockJadeProfile).setParameter("gui", Setting.JADE_GUI.getValue());
        verify(mockJadeProfile).setParameter("port", Setting.JADE_PORT.getValue());
        verify(mockJadeProfile).setParameter("jade_mtp_http_port", Setting.JADE_MTP_PORT.getValue());
        verify(mockJadeProfile).setParameter("jade_domain_df_autocleanup", Setting.JADE_DF_AUTOCLEANUP.getValue());
        verify(mockJadeRuntime).setCloseVM(true);
        verify(mockJadeRuntime).createMainContainer(mockJadeProfile);
    }

}