/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package functional.test;

public class FunctionalTestMain {

    public static void main(String[] args) {
        FunctionalTestBoot functionalTestBoot = new FunctionalTestBoot();
        functionalTestBoot.boot(args);
    }

}
