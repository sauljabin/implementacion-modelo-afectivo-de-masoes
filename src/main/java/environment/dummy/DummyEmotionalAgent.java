/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.dummy;

import masoes.CognitiveBehaviour;
import masoes.EmotionalAgent;
import masoes.ImitativeBehaviour;
import masoes.ReactiveBehaviour;

public class DummyEmotionalAgent extends EmotionalAgent {

    private DummyReactiveBehaviour dummyReactiveBehaviour;
    private DummyImitativeBehaviour dummyImitativeBehaviour;
    private DummyCognitiveBehaviour dummyCognitiveBehaviour;

    @Override
    public void setUp() {
        dummyImitativeBehaviour = new DummyImitativeBehaviour();
        dummyReactiveBehaviour = new DummyReactiveBehaviour();
        dummyCognitiveBehaviour = new DummyCognitiveBehaviour();
    }

    @Override
    public String getKnowledgePath() {
        return "theories/dummy/dummyEmotionalAgent.prolog";
    }

    @Override
    public ImitativeBehaviour getImitativeBehaviour() {
        return dummyImitativeBehaviour;
    }

    @Override
    public ReactiveBehaviour getReactiveBehaviour() {
        return dummyReactiveBehaviour;
    }

    @Override
    public CognitiveBehaviour getCognitiveBehaviour() {
        return dummyCognitiveBehaviour;
    }

}
