/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.util.string;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

public class StringGeneratorTest {

    private StringGenerator stringGenerator;

    @Before
    public void setUp() throws Exception {
        stringGenerator = new StringGenerator();
    }

    @Test
    public void shouldReturnRandomString() throws Exception {
        int length = 100;
        String actualString = stringGenerator.getString(length);
        String actualStringAgain = stringGenerator.getString(length);
        assertThat(actualString.length(), is(length));
        assertThat(actualString, is(not(actualStringAgain)));
    }

}