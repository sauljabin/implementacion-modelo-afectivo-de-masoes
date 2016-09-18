/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app;

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

    @Before
    public void setUp() throws Exception {
        applicationOptions = new ApplicationOptions();
        expectedOptions = new ArrayList<>();
        expectedOptions.add(new VersionOption());
        expectedOptions.add(new HelpOption(applicationOptions));
        expectedOptions.add(new JadeOption());
        Collections.sort(expectedOptions);
    }

    @Test
    public void shouldReturnSortOption() {
        assertReflectionEquals(expectedOptions, applicationOptions.getApplicationOptions());
    }

    @Test
    public void shouldReturnOptionsObject() {
        Options expectedOptions = new Options();
        expectedOptions.addOption(new JadeOption());
        expectedOptions.addOption(new VersionOption());
        expectedOptions.addOption(new HelpOption(applicationOptions));
        assertReflectionEquals(expectedOptions, applicationOptions.toOptions());
    }

}