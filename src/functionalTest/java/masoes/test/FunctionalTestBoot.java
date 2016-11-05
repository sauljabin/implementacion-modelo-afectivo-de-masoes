/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.test;

import masoes.app.setting.Setting;
import masoes.app.setting.SettingsLoader;
import masoes.jade.JadeBoot;

public class FunctionalTestBoot {

    public static void main(String[] args) {
        try {
            SettingsLoader settingsLoader = SettingsLoader.getInstance();
            settingsLoader.load();
            Setting.JADE_GUI.setValue("false");
            JadeBoot jadeBoot = new JadeBoot();
            jadeBoot.boot("tester:masoes.test.FunctionalTester");
        } catch (Exception e) {
            System.exit(-1);
        }

    }

}
