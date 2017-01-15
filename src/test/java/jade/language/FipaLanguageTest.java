/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.language;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class FipaLanguageTest {

    private FipaLanguage fipaLanguage;

    @Before
    public void setUp() {
        fipaLanguage = FipaLanguage.getInstance();
    }

    @After
    public void tearDown() throws Exception {
        setFieldValue(fipaLanguage, "INSTANCE", null);
    }

    @Test
    public void shouldGetSameInstance() {
        assertThat(FipaLanguage.getInstance(), is(fipaLanguage));
    }

}