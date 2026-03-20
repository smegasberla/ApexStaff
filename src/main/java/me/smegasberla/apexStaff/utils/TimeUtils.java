package me.smegasberla.apexStaff.utils;

/**
 * Utility class for time-related operations
 */
public class TimeUtils {

    /**
     * Formats a duration in seconds to a human-readable string
     *
     * @param seconds The duration in seconds
     * @return The formatted time string (e.g., "1h 30m 45s")
     */
    public static String formatDuration(long seconds) {
        if (seconds <= 0) {
            return "0s";
        }

        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;

        StringBuilder builder = new StringBuilder();

        if (hours > 0) {
            builder.append(hours).append("h ");
        }
        if (minutes > 0) {
            builder.append(minutes).append("m ");
        }
        if (secs > 0 || (hours == 0 && minutes == 0)) {
            builder.append(secs).append("s");
        }

        return builder.toString().trim();
    }

    /**
     * Parses a time string to seconds
     * Supports formats like: "1h", "30m", "45s", "1h30m45s"
     *
     * @param timeString The time string to parse
     * @return The duration in seconds, or -1 if invalid
     */
    public static long parseTimeToSeconds(String timeString) {
        if (timeString == null || timeString.isEmpty()) {
            return -1;
        }

        long totalSeconds = 0;
        StringBuilder number = new StringBuilder();

        for (int i = 0; i < timeString.length(); i++) {
            char c = timeString.charAt(i);

            if (Character.isDigit(c)) {
                number.append(c);
            } else if (c == 'h' || c == 'm' || c == 's') {
                if (number.length() == 0) {
                    return -1;
                }

                long value = Long.parseLong(number.toString());
                number = new StringBuilder();

                switch (c) {
                    case 'h':
                        totalSeconds += value * 3600;
                        break;
                    case 'm':
                        totalSeconds += value * 60;
                        break;
                    case 's':
                        totalSeconds += value;
                        break;
                }
            } else if (!Character.isWhitespace(c)) {
                return -1;
            }
        }

        return totalSeconds;
    }

    /**
     * Gets the current timestamp in milliseconds
     *
     * @return Current time in milliseconds
     */
    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * Gets the current timestamp in seconds
     *
     * @return Current time in seconds
     */
    public static long getCurrentTimeSeconds() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * Converts milliseconds to seconds
     *
     * @param millis The milliseconds to convert
     * @return The equivalent seconds
     */
    public static long millisToSeconds(long millis) {
        return millis / 1000;
    }

    /**
     * Converts seconds to milliseconds
     *
     * @param seconds The seconds to convert
     * @return The equivalent milliseconds
     */
    public static long secondsToMillis(long seconds) {
        return seconds * 1000;
    }
}
