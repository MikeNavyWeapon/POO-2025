package pt.escnaval.exercicios;

import java.util.Locale;
import java.util.Scanner;

public class LeituraRobusta {
    public static void main(String[] args) {
        // Use Locale fixo para garantir ponto decimal (.) ao ler doubles
        Locale.setDefault(Locale.US);

        try (Scanner sc = new Scanner(System.in)) {
            System.out.print("Introduza um inteiro: ");
            int n = lerInt(sc);

            System.out.print("Introduza um número real (double): ");
            double x = lerDouble(sc);

            System.out.print("Introduza um texto (linha): ");
            String linha = sc.nextLine().trim();

            System.out.printf("OK: n=%d, x=%.3f, texto=\"%s\"%n", n, x, linha);
        }
    }

    // Ler inteiro de forma robusta (consome linha e tenta converter)
    static int lerInt(Scanner sc) {
        while (true) {
            String s = sc.nextLine();
            try {
                return Integer.parseInt(s.trim());
            } catch (NumberFormatException e) {
                System.out.print("Inteiro inválido. Tente novamente: ");
            }
        }
    }

    // Ler double de forma robusta
    static double lerDouble(Scanner sc) {
        while (true) {
            String s = sc.nextLine();
            try {
                return Double.parseDouble(s.trim());
            } catch (NumberFormatException e) {
                System.out.print("Double inválido. Tente novamente (ex.: 3.14): ");
            }
        }
    }
}