package com.apptime.auth.util;

import java.time.Duration;

public class DurationUtil {
    public static String toString(Duration duration) {
        if (duration == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        if (duration.toDays() > 0) {
            long days = duration.toDays();
            sb.append(days).append(" ");
            if (days > 1) {
                sb.append("Days");
            } else {
                sb.append("Day");
            }
            duration = duration.minusDays(days);
        }

        if (duration.toHours() > 0) {
            if (sb.length() > 0) {
                sb.append(" ");
            }
            long hours = duration.toHours();
            sb.append(hours).append(" ");
            if (hours > 1) {
                sb.append("Hours");
            } else {
                sb.append("Hour");
            }
            duration = duration.minusHours(hours);
        }

        if (duration.toMinutes() > 0) {
            if (sb.length() > 0) {
                sb.append(" ");
            }
            long minutes = duration.toMinutes();
            sb.append(minutes).append(" ");
            if (minutes > 1) {
                sb.append("Minutes");
            } else {
                sb.append("Minute");
            }
        }

        return sb.toString();
    }
}
