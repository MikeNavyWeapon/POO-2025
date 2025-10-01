package pt.escnaval.exercicios;

import java.util.Locale;
import java.util.Scanner;

public class ConversorUnidades {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("=== Conversor ===");
            System.out.print("Escolha (1) km→mi, (2) mi→km: ");
            int modo = lerOpcao(sc, 1, 2);
            double v = lerDouble(sc, "valor");

            double out = (modo == 1) ? kmParaMilhas(v) : milhasParaKm(v);
            System.out.printf("Resultado: %.3f%n", out);
        }
    }

    static int lerOpcao(Scanner sc, int min, int max) {
        while (true) {
            String s = sc.nextLine();
            try {
                int op = Integer.parseInt(s.trim());
                if (op < min || op > max) throw new IllegalArgumentException();
                return op;
            } catch (Exception e) {
                System.out.print("Opção inválida. Tente novamente: ");
            }
        }
    }

    static double lerDouble(Scanner sc, String nome) {
        System.out.print(nome + ": ");
        while (true) {
            String s = sc.nextLine();
            try { return Double.parseDouble(s.trim()); }
            catch (NumberFormatException e) { System.out.print("Número inválido. Tente novamente: "); }
        }
    }

    // Métodos "puros": não leem nem escrevem no terminal
    static double kmParaMilhas(double km)    { return km * 0.621371; }
    static double milhasParaKm(double mi)   { return mi / 0.621371; }
}