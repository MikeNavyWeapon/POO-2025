package pt.escnaval.exercicios.manutencao.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class DateUtils {
    private static final DateTimeFormatter USER_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private DateUtils() {}

    public static Instant parseInstant(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return Instant.parse(value);
        } catch (Exception e) {
            return null;
        }
    }

    public static String formatInstant(Instant instant) {
        return instant == null ? "" : instant.toString();
    }

    public static Instant parseUserDateTime(String value) {
        LocalDateTime dt = LocalDateTime.parse(value, USER_FMT);
        return dt.atZone(ZoneId.systemDefault()).toInstant();
    }

    public static String formatUserDateTime(Instant instant) {
        if (instant == null) return "";
        return USER_FMT.format(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()));
    }
}
