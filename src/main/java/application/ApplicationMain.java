/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application;

import java.util.Locale;

public class ApplicationMain {

    public static void main(String[] args) {
        Locale.setDefault(Locale.Category.FORMAT, Locale.ENGLISH);
        ApplicationBoot applicationBoot = new ApplicationBoot();
        applicationBoot.boot(args);
    }

}
