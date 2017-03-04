/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package knowledge;

import alice.tuprolog.Prolog;
import alice.tuprolog.Theory;

import java.nio.file.Path;

public class KnowledgeBase extends Prolog {

    public void addKnowledge(Knowledge knowledge) {
        if (knowledge != null) {
            addTheory(knowledge.toString().trim());
        }
    }

    public void addTheory(String theory) {
        try {
            addTheory(new Theory(theory));
        } catch (Exception e) {
            throw new KnowledgeException(e);
        }
    }

    @Override
    public void addTheory(Theory th) {
        try {
            super.addTheory(th);
        } catch (Exception e) {
            throw new KnowledgeException(e);
        }
    }

    public void addTheory(Path path) {
        try {
            addTheory(new Theory(ClassLoader.getSystemResourceAsStream(path.toString())));
        } catch (Exception e) {
            throw new KnowledgeException(e);
        }
    }

}
