package pt.escnaval.exercicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class EstatisticasSimples {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        List<Double> valores = new ArrayList<>();

        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("=== Estatísticas Simples ===");
            System.out.println("Introduza valores (ENTER por linha). Linha vazia termina.");
            while (true) {
                String s = sc.nextLine();
                if (s.isBlank()) break;
                try {
                    valores.add(Double.parseDouble(s.trim()));
                } catch (NumberFormatException e) {
                    System.out.println("Ignorado (não é número).");
                }
            }
        }

        if (valores.isEmpty()) {
            System.out.println("(sem dados)");
            return;
        }

        double soma = 0, min = Double.POSITIVE_INFINITY, max = Double.NEGATIVE_INFINITY;
        for (double v : valores) {
            soma += v;
            if (v < min) min = v;
            if (v > max) max = v;
        }
        double media = soma / valores.size();

        System.out.printf("n=%d, soma=%.3f, média=%.3f, min=%.3f, max=%.3f%n",
                valores.size(), soma, media, min, max);
    }
}