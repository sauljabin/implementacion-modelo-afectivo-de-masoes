/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.option;

import masoes.app.setting.Setting;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class CaseOptionTest {

    private CaseOption caseOption;

    @Before
    public void setUp() throws Exception {
        caseOption = new CaseOption();
    }

    @Test
    public void shouldGetCorrectConfiguration() {
        assertThat(caseOption.getOpt(), is("c"));
        assertThat(caseOption.getLongOpt(), is("case"));
        assertThat(caseOption.getDescription(), is("Sets the case study"));
        assertTrue(caseOption.hasArg());
        assertThat(caseOption.getOrder(), is(50));
    }

    @Test
    public void shouldSetCaseSettingValue() {
        String expectedCaseStudy = "dafault";
        caseOption.exec(expectedCaseStudy);
        assertThat(Setting.MASOES_CASE.getValue(), is(expectedCaseStudy));
        assertThat(Setting.get("masoes.case"), is(expectedCaseStudy));
    }

}