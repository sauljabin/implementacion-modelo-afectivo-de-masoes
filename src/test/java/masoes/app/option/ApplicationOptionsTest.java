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

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class ApplicationOptionsTest {

    private ApplicationOptions applicationOptions;
    private List<ApplicationOption> expectedOptions;
    private Options expectedOptionsObject;

    @Before
    public void setUp() throws Exception {
        applicationOptions = new ApplicationOptions();
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
    public void shouldReturnSortOption() {
        assertReflectionEquals(expectedOptions, applicationOptions.getApplicationOptionList());
    }

    @Test
    public void shouldReturnOptionsObject() {
        assertReflectionEquals(expectedOptionsObject, applicationOptions.toOptions());
    }

    @Test
    public void shouldReturnHelpDefaultOption() {
        assertReflectionEquals(new HelpOption(), applicationOptions.getDefaultApplicationOption());
    }

}