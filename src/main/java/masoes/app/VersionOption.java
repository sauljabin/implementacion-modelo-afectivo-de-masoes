/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app;

import masoes.setting.Setting;

public class VersionOption extends ApplicationOption {

    public VersionOption() {
        super("v", "version", false, "Shows the application version");
    }

    @Override
    public void exec(String optionValue) {
        System.out.println(Setting.APP_NAME.getValue().toUpperCase());
        System.out.printf("Version: %s\n", Setting.APP_VERSION.getValue());
        System.out.printf("Revision: %s\n", Setting.APP_REVISION.getValue());
        System.out.println();
        System.out.println("JADE");
        System.out.printf("Version: %s\n", jade.core.Runtime.getVersion());
        System.out.printf("Revision: %s\n", jade.core.Runtime.getRevision());
    }
}
