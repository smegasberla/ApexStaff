package me.smegasberla.apexStaff.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for time-related operations
 */
public class TimeUtils {

    // Time constants in seconds
    public static final long SECOND = 1;
    public static final long MINUTE = 60 * SECOND;
    public static final long HOUR = 60 * MINUTE;
    public static final long DAY = 24 * HOUR;
    public static final long WEEK = 7 * DAY;
    public static final long MONTH = 30 * DAY;
    public static final long YEAR = 365 * DAY;

    /**
     * Formats a duration in seconds to a human-readable string
     */
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

    /**
     * Formats a duration in milliseconds to a human-readable string
     */
    public static String formatDurationMillis(long millis) {
        return formatDuration(millis / 1000);
    }

    /**
     * Calculates the remaining time from a future timestamp and formats it.
     * Use this in AsyncPreLoginListener for the kick message!
     * @param expiryTimestampMillis The timestamp when the ban ends
     * @return Formatted remaining time (e.g. "1h 30m" or "Permanent")
     */
    public static String getRemainingTimeFormatted(long expiryTimestampMillis) {
        // FIX: Handle permanent bans correctly so it doesn't say "0s"
        if (expiryTimestampMillis <= 0) return "Permanent";

        long remainingMillis = expiryTimestampMillis - System.currentTimeMillis();
        if (remainingMillis <= 0) return "Expired"; // Changed to Expired to avoid confusion with 0s

        return formatDurationMillis(remainingMillis);
    }

    /**
     * Parses a time string to seconds (Consolidated & Bug-Fixed)
     * Supports formats like: "1y", "2mo", "3w", "4d", "5h", "30m", "45s"
     */
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
                    // FIX: Catch huge numbers to prevent NumberFormatException crashes
                    long value = Long.parseLong(number.toString());
                    number.setLength(0); // Reset for the next number

                    if (c == 'y') totalSeconds += value * YEAR;
                    else if (c == 'w') totalSeconds += value * WEEK;
                    else if (c == 'd') totalSeconds += value * DAY;
                    else if (c == 'h') totalSeconds += value * HOUR;
                    else if (c == 'm') {
                        // Check if it's "mo" for months
                        if (i + 1 < timeString.length() && timeString.charAt(i + 1) == 'o') {
                            totalSeconds += value * MONTH;
                            i++; // Skip the 'o'
                        } else {
                            totalSeconds += value * MINUTE; // Just 'm' for minutes
                        }
                    }
                    else if (c == 's') totalSeconds += value;
                } catch (NumberFormatException e) {
                    return -1; // Number was too long, mark as invalid
                }
            }
        }

        // If a raw number was passed without a letter (e.g. "3600"), treat it as seconds
        if (number.length() > 0) {
            try {
                totalSeconds += Long.parseLong(number.toString());
            } catch (NumberFormatException e) {
                return -1;
            }
        }

        return totalSeconds > 0 ? totalSeconds : -1;
    }

    /**
     * Converts a time string to milliseconds
     */
    public static long parseTimeToMillis(String timeString) {
        long seconds = parseTimeString(timeString);
        return seconds == -1 ? -1 : seconds * 1000;
    }

    /**
     * Calculates the expiry timestamp from a duration string
     */
    public static long calculateExpiry(String durationString) {
        if (durationString == null || durationString.trim().isEmpty()) return 0; // 0 = Invalid Format

        durationString = durationString.trim();
        if (durationString.equalsIgnoreCase("permanent") ||
                durationString.equalsIgnoreCase("perm") ||
                durationString.equals("-1")) {
            return -1; // -1 = Permanent
        }

        long seconds = parseTimeString(durationString);

        // FIX: If parseTimeString fails (-1), return 0 (Invalid), NOT -1 (Permanent)
        // This prevents staff typos from causing accidental perm bans!
        if (seconds <= 0) return 0;

        return System.currentTimeMillis() + (seconds * 1000);
    }

    /**
     * Formats a timestamp to a readable date/time string
     */
    public static String formatTimestamp(long timestamp) {
        if (timestamp <= 0) return "Permanent";

        // Creating the SimpleDateFormat inside the method ensures it is thread-safe!
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(timestamp));
    }
}