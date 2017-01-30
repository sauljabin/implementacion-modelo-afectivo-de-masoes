/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application;

import application.option.BootOption;
import application.option.EnvironmentOption;
import application.option.HelpOption;
import application.option.JadeOption;
import application.option.SettingsOption;
import application.option.VersionOption;
import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class OptionsContainerTest {

    private OptionsContainer optionsContainer;
    private List<ApplicationOption> expectedOptions;
    private Options expectedOptionsObject;
    private ApplicationOption expectedDefaultOption;

    @Before
    public void setUp() {
        optionsContainer = new OptionsContainer();
        expectedOptionsObject = new Options();
        expectedDefaultOption = new BootOption();

        expectedOptions = new ArrayList<>();
        expectedOptions.add(new VersionOption());
        expectedOptions.add(new HelpOption());
        expectedOptions.add(new JadeOption());
        expectedOptions.add(new SettingsOption());
        expectedOptions.add(new EnvironmentOption());
        expectedOptions.add(new BootOption());
        expectedOptions.forEach(option -> expectedOptionsObject.addOption(option.toOption()));

        Collections.sort(expectedOptions);
    }

    @Test
    public void shouldReturnSortOptionList() {
        assertReflectionEquals(expectedOptions, optionsContainer.getApplicationOptionList());
    }

    @Test
    public void shouldConvertOptionListToOptions() {
        assertReflectionEquals(expectedOptionsObject, optionsContainer.toOptions());
    }

    @Test
    public void shouldReturnCorrectDefaultOption() {
        assertReflectionEquals(expectedDefaultOption, optionsContainer.getDefaultApplicationOption());
    }

}