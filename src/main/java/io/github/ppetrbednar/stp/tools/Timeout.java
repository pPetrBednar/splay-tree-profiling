package io.github.ppetrbednar.stp.tools;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Petr Bednář
 */
public class Timeout {

    /**
     * Run function after delay.
     *
     * @param function Function to run
     * @param delay Delay in ms
     */
    public static void set(Runnable function, long delay) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                function.run();
            }
        }, delay);
    }
}
