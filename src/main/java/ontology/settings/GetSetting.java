/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package ontology.settings;

import jade.content.AgentAction;
import util.ToStringBuilder;

public class GetSetting implements AgentAction {

    private String key;

    public GetSetting() {
    }

    public GetSetting(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("key", key)
                .toString();
    }

}
