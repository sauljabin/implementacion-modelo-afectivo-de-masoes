/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.option;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;

public class ApplicationOptionTest {

    private ApplicationOption applicationOptionA;
    private ApplicationOption applicationOptionB;

    @Before
    public void setUp() throws Exception {
        applicationOptionA = createApplicationOption("a", "testA", false, "testA", 1);
        applicationOptionB = createApplicationOption("b", null, false, "testB", 2);
    }

    @Test
    public void shouldCorrectCompareObject() {
        assertThat(applicationOptionA.compareTo(applicationOptionA), is(0));
        assertThat(applicationOptionA.compareTo(applicationOptionB), is(lessThan(0)));
        assertThat(applicationOptionB.compareTo(applicationOptionA), is(greaterThan(0)));
    }

    @Test
    public void shouldGetCorrectString() {
        assertThat(applicationOptionA.toString(), is("{option=[-a,--testA], order=1}"));
    }

    @Test
    public void shouldGetCorrectKey() {
        assertThat(applicationOptionA.getKeyOpt(), is(applicationOptionA.getLongOpt()));
        assertThat(applicationOptionB.getKeyOpt(), is(applicationOptionB.getOpt()));
    }

    private ApplicationOption createApplicationOption(final String opt, final String longOpt, final boolean hasArg, final String description, int order) {
        return new ApplicationOption() {

            @Override
            public int getOrder() {
                return order;
            }

            @Override
            public String getLongOpt() {
                return longOpt;
            }

            @Override
            public String getOpt() {
                return opt;
            }

            @Override
            public String getDescription() {
                return description;
            }

            @Override
            public boolean hasArg() {
                return hasArg;
            }

            @Override
            public void exec(String optionValue) {
            }

        };
    }

}