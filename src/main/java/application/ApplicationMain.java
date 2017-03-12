/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application;

public class ApplicationMain {

    public static void main(String[] args) {
        ApplicationBoot applicationBoot = new ApplicationBoot();
        applicationBoot.boot(args);
    }

}
