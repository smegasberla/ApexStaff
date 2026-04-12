package me.smegasberla.apexStaff.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for time-related operations
 */
public class TimeUtils {

    public static final long SECOND = 1;
    public static final long MINUTE = 60 * SECOND;
    public static final long HOUR = 60 * MINUTE;
    public static final long DAY = 24 * HOUR;
    public static final long WEEK = 7 * DAY;
    public static final long MONTH = 30 * DAY;
    public static final long YEAR = 365 * DAY;

    public static String formatDuration(long seconds) {
        if (seconds <= 0) return "0s";

        long years = seconds / YEAR;
        seconds %= YEAR;
        long months = seconds / MONTH;
        seconds %= MONTH;
        long weeks = seconds / WEEK;
        seconds %= WEEK;
        long days = seconds / DAY;
        seconds %= DAY;
        long hours = seconds / HOUR;
        seconds %= HOUR;
        long minutes = seconds / MINUTE;
        long secs = seconds % MINUTE;

        StringBuilder builder = new StringBuilder();

        if (years > 0) builder.append(years).append("y ");
        if (months > 0) builder.append(months).append("mo ");
        if (weeks > 0) builder.append(weeks).append("w ");
        if (days > 0) builder.append(days).append("d ");
        if (hours > 0) builder.append(hours).append("h ");
        if (minutes > 0) builder.append(minutes).append("m ");
        if (secs > 0 || builder.length() == 0) builder.append(secs).append("s");

        return builder.toString().trim();
    }

    public static String formatDurationMillis(long millis) {
        return formatDuration(millis / 1000);
    }

    public static String getRemainingTimeFormatted(long expiryTimestampMillis) {
        if (expiryTimestampMillis <= 0) return "Permanent";

        long remainingMillis = expiryTimestampMillis - System.currentTimeMillis();
        if (remainingMillis <= 0) return "Expired";

        return formatDurationMillis(remainingMillis);
    }

    public static long parseTimeString(String timeString) {
        if (timeString == null || timeString.trim().isEmpty()) return -1;

        long totalSeconds = 0;
        StringBuilder number = new StringBuilder();

        // Normalize string to lowercase and remove spaces
        timeString = timeString.toLowerCase().replace(" ", "");

        for (int i = 0; i < timeString.length(); i++) {
            char c = timeString.charAt(i);

            if (Character.isDigit(c)) {
                number.append(c);
            } else if (Character.isLetter(c)) {
                if (number.length() == 0) continue;

                try {
                    long value = Long.parseLong(number.toString());
                    number.setLength(0);

                    if (c == 'y') totalSeconds += value * YEAR;
                    else if (c == 'w') totalSeconds += value * WEEK;
                    else if (c == 'd') totalSeconds += value * DAY;
                    else if (c == 'h') totalSeconds += value * HOUR;
                    else if (c == 'm') {
                        if (i + 1 < timeString.length() && timeString.charAt(i + 1) == 'o') {
                            totalSeconds += value * MONTH;
                            i++;
                        } else {
                            totalSeconds += value * MINUTE;
                        }
                    }
                    else if (c == 's') totalSeconds += value;
                } catch (NumberFormatException e) {
                    return -1;
                }
            }
        }

        if (number.length() > 0) {
            try {
                totalSeconds += Long.parseLong(number.toString());
            } catch (NumberFormatException e) {
                return -1;
            }
        }

        return totalSeconds > 0 ? totalSeconds : -1;
    }

    public static long parseTimeToMillis(String timeString) {
        long seconds = parseTimeString(timeString);
        return seconds == -1 ? -1 : seconds * 1000;
    }

    public static long calculateExpiry(String durationString) {
        if (durationString == null || durationString.trim().isEmpty()) return 0;

        durationString = durationString.trim();
        if (durationString.equalsIgnoreCase("permanent") ||
                durationString.equalsIgnoreCase("perm") ||
                durationString.equals("-1")) {
            return -1;
        }

        long seconds = parseTimeString(durationString);

        if (seconds <= 0) return 0;

        return System.currentTimeMillis() + (seconds * 1000);
    }

    public static String formatTimestamp(long timestamp) {
        if (timestamp <= 0) return "Permanent";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(timestamp));
    }
}