/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application.option;

import application.ApplicationOption;
import application.ApplicationSettings;
import application.ArgumentType;
import translate.Translation;

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
        return Translation.getInstance().get("option.version.description");
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
        printVersion(ApplicationSettings.APP_VERSION);
        printRevision(ApplicationSettings.APP_REVISION);
        System.out.println(LINE);
        System.out.println("JADE");
        printVersion(ApplicationSettings.JADE_VERSION);
        printRevision(ApplicationSettings.JADE_REVISION);
        System.out.println(LINE);
    }

    private void printVersion(String key) {
        System.out.printf("%s: %s\n", Translation.getInstance().get("gui.version"), applicationSettings.get(key));
    }

    private void printRevision(String key) {
        System.out.printf("%s: %s\n", Translation.getInstance().get("gui.revision"), applicationSettings.get(key));
    }

}
