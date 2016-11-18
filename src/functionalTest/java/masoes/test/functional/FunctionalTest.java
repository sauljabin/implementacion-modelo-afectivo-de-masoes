/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.test.functional;

import jade.core.Agent;
import test.common.Test;

public class FunctionalTest extends Test {

    private boolean hasError;

    public FunctionalTest() {
        hasError = false;
    }

    @Override
    public void clean(Agent agent) {
        if (agent instanceof FunctionalTesterAgent) {
            ((FunctionalTesterAgent) agent).setHasError(hasError);
        }
    }

    public void assertEquals(String value, Object expected, Object actual) {
        if (actual.equals(expected)) {
            assertPass(String.format("Assert equals: %s", value));
        } else {
            assertError(String.format("Assert equals: %s\nExpected: %s \n and was: %s", value, expected, actual));
        }
    }

    private void assertPass(String message) {
        if (!hasError) {
            passed(message);
        }
    }

    private void assertError(String message) {
        if (!hasError) {
            hasError = true;
            failed(message);
        }
    }

}
