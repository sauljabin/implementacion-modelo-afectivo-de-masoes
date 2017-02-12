/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package knowledge;

// TODO: UNIT-TEST

import alice.tuprolog.Prolog;
import alice.tuprolog.Theory;

public class KnowledgeBase extends Prolog {


    public void addTheory(String theory) {
        try {
            addTheory(new Theory(theory));
        } catch (Exception e) {
            throw new KnowledgeBaseException(e);
        }
    }

    public void addTheoryFromPath(String path) {
        try {
            addTheory(new Theory(ClassLoader.getSystemResourceAsStream(path)));
        } catch (Exception e) {
            throw new KnowledgeBaseException(e);
        }
    }

}
