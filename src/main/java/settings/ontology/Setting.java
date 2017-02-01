/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package settings.ontology;

import jade.content.Concept;
import util.ToStringBuilder;

public class Setting implements Concept {

    private String value;
    private String key;

    public Setting() {
    }

    public Setting(String key, String value) {
        this.value = value;
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
                .append("value", value)
                .append("key", key)
                .toString();
    }

}
