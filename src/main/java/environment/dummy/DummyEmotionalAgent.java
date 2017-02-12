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

// TODO: UNIT-TEST

public class DummyEmotionalAgent extends EmotionalAgent {

    @Override
    public void setUp() {
    }

    @Override
    public String getKnowledgePath() {
        return "theories/dummyAgent.pl";
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
