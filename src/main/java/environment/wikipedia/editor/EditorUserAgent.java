/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.editor;

import environment.dummy.DummyCognitiveBehaviour;
import environment.dummy.DummyImitativeBehaviour;
import environment.dummy.DummyReactiveBehaviour;
import knowledge.Knowledge;
import masoes.CognitiveBehaviour;
import masoes.EmotionalAgent;
import masoes.ImitativeBehaviour;
import masoes.ReactiveBehaviour;

public class EditorUserAgent extends EmotionalAgent {

    @Override
    public void setUp() {

    }

    @Override
    public Knowledge getKnowledge() {
        return null;
    }

    @Override
    public ImitativeBehaviour getImitativeBehaviour() {
        return new DummyImitativeBehaviour();
    }

    @Override
    public ReactiveBehaviour getReactiveBehaviour() {
        return new DummyReactiveBehaviour();
    }

    @Override
    public CognitiveBehaviour getCognitiveBehaviour() {
        return new DummyCognitiveBehaviour();
    }

}