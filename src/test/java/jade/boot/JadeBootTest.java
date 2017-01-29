/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.boot;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import org.junit.Before;
import org.junit.Test;
import settings.loader.JadeSettings;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class JadeBootTest {

    private ProfileImpl jadeProfileMock;
    private Runtime jadeRuntimeMock;
    private JadeBoot jadeBoot;
    private JadeSettings jadeSettingsMock;

    @Before
    public void setUp() throws Exception {
        jadeSettingsMock = mock(JadeSettings.class);
        jadeProfileMock = mock(ProfileImpl.class);
        jadeRuntimeMock = mock(Runtime.class);

        jadeBoot = new JadeBoot();
        setFieldValue(jadeBoot, "jadeProfile", jadeProfileMock);
        setFieldValue(jadeBoot, "jadeRuntime", jadeRuntimeMock);
        setFieldValue(jadeBoot, "jadeSettings", jadeSettingsMock);
    }

    @Test
    public void shouldSetJadeSettingsInJadeProfile() {
        String expectedKey1 = "key";
        String expectedKey2 = "key";
        String expectedValue1 = "key";
        String expectedValue2 = "key";

        Map<String, String> map = new HashMap<>();
        map.put(expectedKey1, expectedValue1);
        map.put(expectedKey2, expectedValue2);

        doReturn(map).when(jadeSettingsMock).toMap();

        jadeBoot.boot();

        verify(jadeProfileMock).setParameter(expectedKey1, expectedValue1);
        verify(jadeProfileMock).setParameter(expectedKey2, expectedValue2);
        verify(jadeRuntimeMock).setCloseVM(true);
        verify(jadeRuntimeMock).startUp(jadeProfileMock);
    }

}