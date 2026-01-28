package pt.escnaval.exercicios.manutencao.utils;

import java.time.Instant;
import java.util.Locale;
import java.util.Scanner;

public final class UtilsIO {
    private UtilsIO() {}

    public static int lerInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String linha = sc.nextLine().trim();
            try { return Integer.parseInt(linha); }
            catch (NumberFormatException e) { System.out.println("Entrada invalida. Introduza um inteiro."); }
        }
    }

    public static int lerInt(Scanner sc, String prompt, int min, int max) {
        while (true) {
            int v = lerInt(sc, prompt);
            if (v >= min && v <= max) return v;
            System.out.println("Valor fora de intervalo (" + min + "-" + max + ").");
        }
    }

    public static double lerDouble(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String linha = sc.nextLine().trim().replace(',', '.');
            try { return Double.parseDouble(linha); }
            catch (NumberFormatException e) { System.out.println("Entrada invalida. Introduza um numero."); }
        }
    }

    public static String lerStringNaoVazia(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String linha = sc.nextLine().trim();
            if (!linha.isBlank()) return linha;
            System.out.println("Entrada vazia. Tente novamente.");
        }
    }

    public static String lerStringOpcional(Scanner sc, String prompt) {
        System.out.print(prompt);
        String linha = sc.nextLine().trim();
        return linha.isBlank() ? null : linha;
    }

    public static boolean lerSimNao(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt + " (s/n): ");
            String linha = sc.nextLine().trim().toLowerCase(Locale.ROOT);
            if (linha.equals("s") || linha.equals("sim")) return true;
            if (linha.equals("n") || linha.equals("nao")) return false;
            System.out.println("Responda com s ou n.");
        }
    }

    public static <T extends Enum<T>> T lerEnum(Scanner sc, String prompt, Class<T> enumClass) {
        while (true) {
            System.out.print(prompt + " " + java.util.Arrays.toString(enumClass.getEnumConstants()) + ": ");
            String linha = sc.nextLine().trim().toUpperCase(Locale.ROOT);
            try { return Enum.valueOf(enumClass, linha); }
            catch (IllegalArgumentException e) { System.out.println("Valor invalido."); }
        }
    }

    public static Instant lerDataHoraOpcional(Scanner sc, String prompt) {
        System.out.print(prompt + " (yyyy-MM-dd HH:mm, vazio para ignorar): ");
        String linha = sc.nextLine().trim();
        if (linha.isBlank()) return null;
        return DateUtils.parseUserDateTime(linha);
    }
}
