/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application.option;

import application.ApplicationOption;
import application.ApplicationSettings;
import application.ArgumentType;

public class VersionOption extends ApplicationOption {

    private static final String LINE = "--------------------------------------------------";
    private ApplicationSettings applicationSettings;

    public VersionOption() {
        applicationSettings = ApplicationSettings.getInstance();
    }

    @Override
    public int getOrder() {
        return 10;
    }

    @Override
    public String getLongOpt() {
        return "version";
    }

    @Override
    public String getOpt() {
        return "v";
    }

    @Override
    public String getDescription() {
        return "Shows the application version";
    }

    @Override
    public ArgumentType getArgType() {
        return ArgumentType.NO_ARGS;
    }

    @Override
    public boolean isFinalOption() {
        return true;
    }

    @Override
    public void exec() {
        System.out.println(LINE);
        System.out.println(applicationSettings.get(ApplicationSettings.APP_NAME).toUpperCase());
        System.out.printf("Version: %s\n", applicationSettings.get(ApplicationSettings.APP_VERSION));
        System.out.printf("Revision: %s\n", applicationSettings.get(ApplicationSettings.APP_REVISION));
        System.out.println(LINE);
        System.out.println("JADE");
        System.out.printf("Version: %s\n", applicationSettings.get(ApplicationSettings.JADE_VERSION));
        System.out.printf("Revision: %s\n", applicationSettings.get(ApplicationSettings.JADE_REVISION));
        System.out.println(LINE);
    }

}
