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
    public void setUp() {
        applicationOptionA = createDummyApplicationOption(1, "testA", "a", null, false);
        applicationOptionB = createDummyApplicationOption(2, null, "a", null, false);
    }

    @Test
    public void shouldCorrectCompareObject() {
        assertThat(applicationOptionA.compareTo(applicationOptionA), is(0));
        assertThat(applicationOptionB.compareTo(applicationOptionB), is(0));
        assertThat(applicationOptionA.compareTo(applicationOptionB), is(lessThan(0)));
        assertThat(applicationOptionB.compareTo(applicationOptionA), is(greaterThan(0)));
    }

    @Test
    public void shouldGetCorrectString() {
        assertThat(applicationOptionA.toString(), is("{option=[-a,--testA], order=1}"));
    }

    @Test
    public void shouldGetCorrectKeyWhenOptIsNull() {
        assertThat(applicationOptionA.getKeyOpt(), is(applicationOptionA.getLongOpt()));
    }

    @Test
    public void shouldGetCorrectKeyLongOptIsNull() {
        assertThat(applicationOptionB.getKeyOpt(), is(applicationOptionB.getOpt()));
    }

    private ApplicationOption createDummyApplicationOption(int order, String longOpt, String opt, String description, boolean hasArg) {
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