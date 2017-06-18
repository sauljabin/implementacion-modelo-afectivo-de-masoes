/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

}