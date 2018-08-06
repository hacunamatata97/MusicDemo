package com.badasspsycho.musicdemo;

import java.util.Locale;

public final class Utilities {

    public static final String UPDATE_PROGRESS = "com.badasspsycho.musicdemo.UPDATE_PROGRESS";
    public static final String CURRENT_TIME = "CURRENT_TIME";

    public static String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Change time to format: Hours:Minutes:Seconds
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hour if included
        if (hours > 0) {
            finalTimerString = String.format(Locale.getDefault(), "%d:", hours);
        }

        // Add "0" to single-digit number
        secondsString = String.format(Locale.getDefault(), "%02d", seconds);
        finalTimerString = String.format(Locale.getDefault(), "%s%d:%s", finalTimerString, minutes,
                secondsString);

        return finalTimerString;
    }

    public static int getProgressPercentage(long currentDuration, long totalDuration) {
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        // Calculate percentage
        percentage = (((double) currentSeconds) / totalSeconds) * 100;

        return percentage.intValue();
    }

    public static int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double) progress) / 100) * totalDuration);

        // Return current time to millisecond
        return currentDuration * 1000;
    }
}
