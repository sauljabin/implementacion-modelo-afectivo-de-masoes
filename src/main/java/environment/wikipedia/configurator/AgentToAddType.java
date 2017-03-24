/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.configurator;

import translate.Translation;

public enum AgentToAddType {

    CONTRIBUTOR("gui.contributor"), REVIEWER("gui.reviewer");

    private String translationKey;

    AgentToAddType(String translationKey) {
        this.translationKey = translationKey;
    }

    @Override
    public String toString() {
        return Translation.getInstance().get(translationKey);
    }

}
