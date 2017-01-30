/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package settings;

import jade.lang.acl.MessageTemplate;

public class SettingsRequestMessageTemplate extends MessageTemplate {

    public SettingsRequestMessageTemplate() {
        super(new SettingsRequestMatchExpression());
    }

}
