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
import org.slf4j.LoggerFactory;

public class FunctionalTestApplication {

    private static final String FUNCTIONAL_TEST_ENV = "functionalTest";
    private ApplicationLogger logger;
    private SettingsLoader settingsLoader;

    public FunctionalTestApplication() {
        logger = new ApplicationLogger(LoggerFactory.getLogger(FunctionalTestApplication.class));
        settingsLoader = SettingsLoader.getInstance();
    }

    public void run(String[] args) {
        try {
            settingsLoader.load();
            Setting.JADE_GUI.setValue(Boolean.FALSE.toString());
            Setting.MASOES_ENV.setValue(FUNCTIONAL_TEST_ENV);
            logger.startingApplication(args);
            EnvironmentAgentInfo functionalTestEnvironment = new EnvironmentAgentInfo(FunctionalTesterAgent.class.getSimpleName(), FunctionalTesterAgent.class, null);
            JadeBoot jadeBoot = new JadeBoot();
            jadeBoot.boot(functionalTestEnvironment.toString());
        } catch (Exception e) {
            logger.cantNotStartApplication(e);
            System.exit(SystemExitStatus.FAILURE.getValue());
        }
    }

}
