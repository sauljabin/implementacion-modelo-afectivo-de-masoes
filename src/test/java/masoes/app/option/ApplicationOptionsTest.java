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

public class ApplicationOptionsTest {

    private ApplicationOptions applicationOptions;
    private List<ApplicationOption> expectedOptions;

    @Before
    public void setUp() throws Exception {
        applicationOptions = ApplicationOptions.getInstance();
        expectedOptions = new ArrayList<>();
        expectedOptions.add(new VersionOption());
        expectedOptions.add(new HelpOption());
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
        expectedOptions.addOption(new JadeOption().toOption());
        expectedOptions.addOption(new VersionOption().toOption());
        expectedOptions.addOption(new HelpOption().toOption());
        assertReflectionEquals(expectedOptions, applicationOptions.toOptions());
    }

    @Test
    public void shouldReturnSameObject() {
        assertThat(ApplicationOptions.getInstance(), is(applicationOptions));
    }

}