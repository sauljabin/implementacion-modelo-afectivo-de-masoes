/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.option;

import masoes.app.settings.ApplicationSettings;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class EnvironmentOptionTest {

    private EnvironmentOption environmentOption;
    private ApplicationSettings applicationSettings;

    @Before
    public void setUp() {
        applicationSettings = ApplicationSettings.getInstance();
        applicationSettings.load();
        environmentOption = new EnvironmentOption();
    }

    @Test
    public void shouldGetCorrectConfiguration() {
        assertThat(environmentOption.getOpt(), is("e"));
        assertThat(environmentOption.getLongOpt(), is("env"));
        assertThat(environmentOption.getDescription(), is("Sets the environment for case study"));
        assertThat(environmentOption.getArgType(), is(ArgumentType.ONE_ARG));
        assertThat(environmentOption.getOrder(), is(50));
    }

    @Test
    public void shouldSetEnvironmentSettingValue() {
        String expectedCaseStudy = "default";
        environmentOption.setValue(expectedCaseStudy);
        environmentOption.exec();
        assertThat(applicationSettings.get("masoes.env"), is(expectedCaseStudy));
    }

}