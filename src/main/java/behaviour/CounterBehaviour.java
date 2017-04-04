/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package behaviour;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;

public abstract class CounterBehaviour extends Behaviour {

    private int maxCount;
    private int count;

    public CounterBehaviour(Agent a, int maxCount) {
        super(a);
        this.maxCount = maxCount;
    }

    public CounterBehaviour(int maxCount) {
        this.maxCount = maxCount;
    }

    @Override
    public void onStart() {
        count = 0;
    }

    @Override
    public void action() {
        count(++count);
    }

    public abstract void count(int i);

    @Override
    public boolean done() {
        return count >= maxCount;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public int getCount() {
        return count;
    }

}
