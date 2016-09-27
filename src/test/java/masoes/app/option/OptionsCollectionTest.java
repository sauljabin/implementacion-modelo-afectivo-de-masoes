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

public class OptionsCollectionTest {

    private OptionsCollection optionsCollection;
    private List<ApplicationOption> expectedOptions;

    @Before
    public void setUp() throws Exception {
        optionsCollection = OptionsCollection.getInstance();
        expectedOptions = new ArrayList<>();
        expectedOptions.add(new VersionOption());
        expectedOptions.add(new HelpOption());
        expectedOptions.add(new JadeOption());
        expectedOptions.add(new SettingsOption());
        expectedOptions.add(new CaseOption());
        Collections.sort(expectedOptions);
    }

    @Test
    public void shouldReturnSortOption() {
        assertReflectionEquals(expectedOptions, optionsCollection.getApplicationOptions());
    }

    @Test
    public void shouldReturnOptionsObject() {
        Options expectedOptions = createOptions();
        assertReflectionEquals(expectedOptions, optionsCollection.toOptions());
    }

    private Options createOptions() {
        Options options = new Options();
        expectedOptions.forEach(option -> options.addOption(option.toOption()));
        return options;
    }

    @Test
    public void shouldReturnSameObject() {
        assertThat(OptionsCollection.getInstance(), is(optionsCollection));
    }

}