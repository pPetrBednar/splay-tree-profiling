package io.github.ppetrbednar.stp.tools;

/**
 *
 * @author Petr Bednář
 */
public class TimeFormat {

    public static String formatTimeOutputSeconds(int timeSeconds) {
        int mins = (int) Math.floor(timeSeconds / 60.0);
        return mins > 0 ? mins + "m " + (timeSeconds - mins * 60) + "s" : timeSeconds + "s";
    }

    public static String formatTimeOutputMinutes(int timeMinutes) {
        int hours = (int) Math.floor(timeMinutes / 60.0);
        return hours > 0 ? hours + "h " + (timeMinutes - hours * 60) + "m" : timeMinutes + "m";
    }
}
