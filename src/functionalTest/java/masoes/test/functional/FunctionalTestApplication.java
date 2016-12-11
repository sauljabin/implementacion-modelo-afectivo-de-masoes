/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.test.functional;

import masoes.app.SystemExitStatus;
import masoes.app.logger.ApplicationLogger;
import masoes.app.setting.Setting;
import masoes.app.setting.SettingsLoader;
import masoes.env.EnvironmentAgentInfo;
import masoes.jade.JadeBoot;
import masoes.jade.settings.JadeSettings;
import org.slf4j.LoggerFactory;

public class FunctionalTestApplication {

    private static final String FUNCTIONAL_TEST_ENV = "functionalTest";
    private final JadeSettings jadeSettings;
    private final JadeBoot jadeBoot;
    private ApplicationLogger logger;
    private SettingsLoader settingsLoader;

    public FunctionalTestApplication() {
        logger = new ApplicationLogger(LoggerFactory.getLogger(FunctionalTestApplication.class));
        settingsLoader = SettingsLoader.getInstance();
        jadeSettings = JadeSettings.getInstance();
        jadeBoot = new JadeBoot();
    }

    public void run(String[] args) {
        try {
            settingsLoader.load();
            jadeSettings.load();
            setSettings();
            logger.startingApplication(args);
            jadeBoot.boot();
        } catch (Exception e) {
            logger.cantNotStartApplication(e);
            System.exit(SystemExitStatus.FAILURE.getValue());
        }
    }

    private void setSettings() {
        Setting.MASOES_ENV.setValue(FUNCTIONAL_TEST_ENV);
        jadeSettings.set(JadeSettings.AGENTS, getEnvironmentAgentInfo());
        jadeSettings.set(JadeSettings.GUI, Boolean.FALSE.toString());
    }

    private String getEnvironmentAgentInfo() {
        return new EnvironmentAgentInfo(FunctionalTesterAgent.class.getSimpleName(), FunctionalTesterAgent.class).toString();
    }

}
