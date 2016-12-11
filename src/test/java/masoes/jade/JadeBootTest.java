/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.jade;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import masoes.app.settings.ApplicationSettings;
import masoes.jade.settings.JadeSettings;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class JadeBootTest {

    private ProfileImpl mockJadeProfile;
    private Runtime mockJadeRuntime;
    private JadeBoot jadeBoot;
    private ApplicationSettings applicationSettings;
    private JadeSettings jadeSettings;

    @Before
    public void setUp() throws Exception {
        applicationSettings = ApplicationSettings.getInstance();
        applicationSettings.load();

        jadeSettings = JadeSettings.getInstance();
        jadeSettings.load();

        mockJadeProfile = mock(ProfileImpl.class);
        mockJadeRuntime = mock(Runtime.class);

        jadeBoot = new JadeBoot();
        setFieldValue(jadeBoot, "jadeProfile", mockJadeProfile);
        setFieldValue(jadeBoot, "jadeRuntime", mockJadeRuntime);
        setFieldValue(jadeBoot, "jadeSettings", jadeSettings);
    }

    @Test
    public void shouldSetCorrectProfileVariables() {
        String expectedArguments = "arguments";

        jadeSettings.set(JadeSettings.AGENTS, expectedArguments);

        jadeBoot.boot();

        verify(mockJadeProfile).setParameter("agents", expectedArguments);
        verify(mockJadeProfile).setParameter("gui", jadeSettings.get(JadeSettings.GUI));
        verify(mockJadeProfile).setParameter("port", jadeSettings.get(JadeSettings.PORT));
        verify(mockJadeProfile).setParameter("jade_mtp_http_port", jadeSettings.get(JadeSettings.JADE_MTP_HTTP_PORT));
        verify(mockJadeProfile).setParameter("jade_domain_df_autocleanup", jadeSettings.get(JadeSettings.JADE_DOMAIN_DF_AUTOCLEANUP));
        verify(mockJadeRuntime).setCloseVM(true);
        verify(mockJadeRuntime).startUp(mockJadeProfile);
    }

}