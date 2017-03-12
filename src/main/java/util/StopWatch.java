/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package util;

public class StopWatch {

    private long startTime = -1;
    private long stopTime = -1;

    public void start() {
        stopTime = -1;
        startTime = System.currentTimeMillis();
    }

    public void stop() {
        stopTime = System.currentTimeMillis();
    }

    public void reset() {
        startTime = -1;
        stopTime = -1;
    }

    public long getTime() {
        if (startTime <= 0) {
            return 0;
        } else if (stopTime <= 0) {
            return System.currentTimeMillis() - this.startTime;
        }
        return this.stopTime - this.startTime;
    }

}
