package pt.escnaval.exercicios.manutencao.utils;

import java.util.ArrayList;
import java.util.List;

public final class CsvUtils {
    private CsvUtils() {}

    public static String escape(String value) {
        if (value == null) return "";
        StringBuilder sb = new StringBuilder(value.length());
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == '\\') sb.append("\\\\");
            else if (c == ';') sb.append("\\;");
            else if (c == '\n') sb.append("\\n");
            else if (c == '\r') sb.append("\\r");
            else sb.append(c);
        }
        return sb.toString();
    }

    public static String[] split(String line) {
        List<String> out = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean esc = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (esc) {
                if (c == 'n') cur.append('\n');
                else if (c == 'r') cur.append('\r');
                else cur.append(c);
                esc = false;
            } else if (c == '\\') {
                esc = true;
            } else if (c == ';') {
                out.add(cur.toString());
                cur.setLength(0);
            } else {
                cur.append(c);
            }
        }
        out.add(cur.toString());
        return out.toArray(new String[0]);
    }

    public static String get(String[] fields, int index) {
        return index < fields.length ? fields[index] : "";
    }

    public static int getInt(String[] fields, int index, int def) {
        try {
            String v = get(fields, index);
            if (v == null || v.isBlank()) return def;
            return Integer.parseInt(v);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public static double getDouble(String[] fields, int index, double def) {
        try {
            String v = get(fields, index);
            if (v == null || v.isBlank()) return def;
            return Double.parseDouble(v);
        } catch (NumberFormatException e) {
            return def;
        }
    }
}
