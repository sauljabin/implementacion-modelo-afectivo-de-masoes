/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.option;

import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class ApplicationOptionManagerTest {

    private ApplicationOptionManager applicationOptionManager;
    private List<ApplicationOption> expectedOptions;
    private Options expectedOptionsObject;

    @Before
    public void setUp() throws Exception {
        applicationOptionManager = ApplicationOptionManager.getInstance();
        expectedOptions = new ArrayList<>();
        expectedOptions.add(new VersionOption());
        expectedOptions.add(new HelpOption());
        expectedOptions.add(new AgentsOption());
        expectedOptions.add(new SettingsOption());
        expectedOptions.add(new EnvironmentOption());
        expectedOptions.add(new BootOption());
        Collections.sort(expectedOptions);

        expectedOptionsObject = new Options();
        expectedOptions.forEach(option -> expectedOptionsObject.addOption(option.toOption()));
    }

    @Test
    public void shouldReturnSameObject() {
        assertThat(ApplicationOptionManager.getInstance(), is(applicationOptionManager));
    }

    @Test
    public void shouldReturnSortOption() {
        assertReflectionEquals(expectedOptions, applicationOptionManager.getApplicationOptions());
    }

    @Test
    public void shouldReturnOptionsObject() {
        assertReflectionEquals(expectedOptionsObject, applicationOptionManager.toOptions());
    }

    @Test
    public void shouldReturnHelpDefaultOption() {
        assertReflectionEquals(new HelpOption(), applicationOptionManager.getDefaultOption());
    }

}