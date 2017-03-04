/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package knowledge;

import org.junit.Test;

import java.nio.file.Paths;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

public class KnowledgeTest {

    @Test
    public void shouldLoadKnowledgeFromPath() {
        Knowledge knowledge = new Knowledge(Paths.get("testTheory.prolog"));
        assertThat(knowledge.toString(), containsString("expectedTheory(X)."));
    }

}