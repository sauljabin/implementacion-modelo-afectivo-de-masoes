/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.option;

import masoes.app.setting.Setting;

public class VersionOption extends ApplicationOption {

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
    public void exec() {
        String line = "--------------------------------------------------";
        System.out.println(line);
        System.out.println(Setting.APP_NAME.getValue().toUpperCase());
        System.out.printf("Version: %s\n", Setting.APP_VERSION.getValue());
        System.out.printf("Revision: %s\n", Setting.APP_REVISION.getValue());
        System.out.println(line);
        System.out.println("JADE");
        System.out.printf("Version: %s\n", Setting.JADE_VERSION.getValue());
        System.out.printf("Revision: %s\n", Setting.JADE_REVISION.getValue());
        System.out.println(line);
    }

}
