/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.language;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class FipaLanguageTest {

    private FipaLanguage fipaLanguage;

    @Before
    public void setUp() {
        fipaLanguage = FipaLanguage.getInstance();
    }

    @Test
    public void shouldGetSameInstance() {
        assertThat(FipaLanguage.getInstance(), is(fipaLanguage));
    }

}