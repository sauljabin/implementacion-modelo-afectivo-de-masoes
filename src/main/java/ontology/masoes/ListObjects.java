/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package ontology.masoes;

import jade.content.Predicate;
import jade.util.leap.List;
import util.ToStringBuilder;

public class ListObjects implements Predicate {

    private List objects;

    public ListObjects() {
    }

    public ListObjects(List objects) {
        this.objects = objects;
    }

    public List getObjects() {
        return objects;
    }

    public void setObjects(List objects) {
        this.objects = objects;
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("objects", objects)
                .toString();
    }

}
