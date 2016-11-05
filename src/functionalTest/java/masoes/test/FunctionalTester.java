/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.test;

import jade.core.Agent;
import test.common.TestGroup;
import test.common.TesterAgent;

public class FunctionalTester extends TesterAgent {

    public static final String FUNCTIONAL_TEST_LIST_FILE = "functionalTestList.xml";

    @Override
    protected TestGroup getTestGroup() {
        return new TestGroup(FUNCTIONAL_TEST_LIST_FILE) {
            @Override
            protected void shutdown(Agent agent) {
                System.exit(0);
            }
        };
    }
}
