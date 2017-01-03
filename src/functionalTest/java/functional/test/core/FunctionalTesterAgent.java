/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package functional.test.core;

import test.common.TestGroup;
import test.common.TesterAgent;

public class FunctionalTesterAgent extends TesterAgent {

    private static final String FUNCTIONAL_TEST_LIST_FILE = "functionalTestList.xml";
    private static final int STATUS_SUCCESS = 0;
    private static final int STATUS_FAILURE = -1;
    private boolean hasError;

    public FunctionalTesterAgent() {
        this.hasError = false;
    }

    public void setHasError() {
        this.hasError = true;
    }

    @Override
    protected TestGroup getTestGroup() {
        return new TestGroup(FUNCTIONAL_TEST_LIST_FILE);
    }

    @Override
    protected void takeDown() {
        super.takeDown();
        if (hasError) {
            System.exit(STATUS_FAILURE);
        } else {
            System.exit(STATUS_SUCCESS);
        }
    }

}
