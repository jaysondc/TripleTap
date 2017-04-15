package com.shakeup.setofthree.Utilities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Jayson on 4/14/2017.
 */

public class Utilities {

    /**
     * Convert miliseconds to the format mm:ss:dd
     * @param millis The time to convert
     * @return A string conversion of the given time
     */
    public static String scoreTimeToString(long millis){

        // Calculate the minutes, seconds, and hundreths manually
        long scoreMinutes =
                TimeUnit.MILLISECONDS.toMinutes(millis);
        long scoreSeconds =
                TimeUnit.MILLISECONDS.toSeconds(millis)
                        - TimeUnit.MINUTES.toSeconds(scoreMinutes);
        long scoreHundreths = (millis
                        - TimeUnit.MINUTES.toMillis(scoreMinutes)
                        - TimeUnit.SECONDS.toMillis(scoreSeconds))
                        /10;

        // Format the string
        return String.format(
                Locale.getDefault(),
                "%02d:%02d:%02d",
                scoreMinutes,
                scoreSeconds,
                scoreHundreths
        );
    }

    /**
     * Converts a timestamp to a time and date in the form
     * MM/dd/yyyy hh:mm a
     * @param millis The time and date to convert in milliseconds
     * @return A string representation of the input time
     */
    public static String dateToString(long millis){
        return new SimpleDateFormat(
                "MM/dd/yyyy hh:mm a",
                Locale.getDefault())
                .format(new Date(millis));
    }

    /**
     * Converts a long to a string
     */
    public static String longToString(long number){
        return String.format(
                Locale.getDefault(),
                "%d",
                number);
    }
}
