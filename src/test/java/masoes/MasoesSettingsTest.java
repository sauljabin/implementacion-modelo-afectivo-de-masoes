/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class MasoesSettingsTest {

    private MasoesSettings masoesSettings;

    @Before
    public void setUp() {
        masoesSettings = MasoesSettings.getInstance();
    }

    @After
    public void tearDown() throws Exception {
        setFieldValue(masoesSettings, "INSTANCE", null);
    }

    @Test
    public void shouldGetSameInstance() {
        assertThat(MasoesSettings.getInstance(), is(masoesSettings));
    }

    @Test
    public void shouldLoadInitValues() {
        Map<String, String> expectedValues = new HashMap<>();
        expectedValues.put(MasoesSettings.MASOES_SATISFACTION_INCREASE, "0.1");
        expectedValues.put(MasoesSettings.MASOES_ACTIVATION_INCREASE, "0.1");
        expectedValues.keySet().forEach(
                key -> assertThat(masoesSettings.get(key), is(expectedValues.get(key)))
        );
    }

}