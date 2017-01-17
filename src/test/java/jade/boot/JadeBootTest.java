/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.boot;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.settings.JadeSettings;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class JadeBootTest {

    private ProfileImpl mockJadeProfile;
    private Runtime mockJadeRuntime;
    private JadeBoot jadeBoot;
    private JadeSettings mockJadeSettings;

    @Before
    public void setUp() throws Exception {
        mockJadeSettings = mock(JadeSettings.class);
        mockJadeProfile = mock(ProfileImpl.class);
        mockJadeRuntime = mock(Runtime.class);

        jadeBoot = new JadeBoot();
        setFieldValue(jadeBoot, "jadeProfile", mockJadeProfile);
        setFieldValue(jadeBoot, "jadeRuntime", mockJadeRuntime);
        setFieldValue(jadeBoot, "jadeSettings", mockJadeSettings);
    }

    @Test
    public void shouldSetCorrectProfileVariables() {
        String expectedKey = "key";
        String expectedValue = "key";

        Map<String, String> map = new HashMap<>();
        map.put(expectedKey, expectedValue);

        doReturn(map).when(mockJadeSettings).toMap();
        jadeBoot.boot();

        verify(mockJadeProfile).setParameter(expectedKey, expectedValue);
        verify(mockJadeRuntime).setCloseVM(true);
        verify(mockJadeRuntime).startUp(mockJadeProfile);
    }

}