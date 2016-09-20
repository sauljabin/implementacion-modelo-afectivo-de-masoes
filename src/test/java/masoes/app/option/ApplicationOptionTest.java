/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.option;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;

public class ApplicationOptionTest {

    private ApplicationOption applicationOptionA;
    private ApplicationOption applicationOptionB;

    @Before
    public void setUp() throws Exception {
        applicationOptionA = createApplicationOption("a", "testA", false, "testA", 1);
        applicationOptionB = createApplicationOption("b", "testB", false, "testB", 2);
    }

    @Test
    public void shouldCorrectCompareObject() {
        assertThat(applicationOptionA.compareTo(applicationOptionB), is(lessThan(0)));
    }

    @Test
    public void shouldGetCorrectString() {
        assertThat(applicationOptionA.toString(), is("{option=[-a,--testA], order=1}"));
    }

    private ApplicationOption createApplicationOption(final String opt, final String longOpt, final boolean hasArg, final String description, int order) {
        return new ApplicationOption() {
            @Override
            public void exec(String optionValue) {
            }

            @Override
            public int getOrder() {
                return order;
            }

            @Override
            public String getOpt() {
                return opt;
            }

            @Override
            public String getLongOpt() {
                return longOpt;
            }

            @Override
            public String getDescription() {
                return description;
            }

            @Override
            public boolean hasArg() {
                return false;
            }
        };
    }

}