/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class ApplicationOptionsTest {

    private ApplicationOptions applicationOptions;
    private Options options;

    @Before
    public void setUp() throws Exception {
        options = new Options();
        options.addOption(new VersionOption());
        options.addOption(new HelpOption(options));
        options.addOption(new JadeOption());
        applicationOptions = new ApplicationOptions();
    }

    @Test
    public void shouldReturnCorrectOptions() {
        assertReflectionEquals(options, applicationOptions);
    }

    @Test
    public void shouldReturnSortOption() {
        List<ApplicationOption> expectedOptions = new ArrayList<>();
        expectedOptions.add(new JadeOption());
        expectedOptions.add(new VersionOption());
        expectedOptions.add(new HelpOption(options));
        Collections.sort(expectedOptions);
        Collection<Option> options = applicationOptions.getOptions();
        assertReflectionEquals(expectedOptions, options);
    }

}