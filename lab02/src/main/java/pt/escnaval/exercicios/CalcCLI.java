package pt.escnaval.exercicios;

import java.util.Locale;
import java.util.Scanner;

public class CalcCLI {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("=== CALC CLI ===");
            double a = lerDouble(sc, "a");
            double b = lerDouble(sc, "b");
            char op = lerOperador(sc, "+ - * / %");

            double res = calcular(a, b, op);
            System.out.printf("Resultado: %.4f%n", res);
        }
    }

    static double lerDouble(Scanner sc, String nome) {
        System.out.print("Introduza " + nome + ": ");
        while (true) {
            String s = sc.nextLine();
            try { return Double.parseDouble(s.trim()); }
            catch (NumberFormatException e) { System.out.print("Número inválido. Tente novamente: "); }
        }
    }

    static char lerOperador(Scanner sc, String menu) {
        System.out.print("Operador (" + menu + "): ");
        while (true) {
            String s = sc.nextLine().trim();
            if (s.length() == 1 && "+-*/%".indexOf(s.charAt(0)) >= 0) return s.charAt(0);
            System.out.print("Operador inválido. Tente: " + menu + " → ");
        }
    }

    static double calcular(double a, double b, char op) {
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/':
                if (b == 0.0) throw new IllegalArgumentException("Divisão por zero");
                return a / b;
            case '%':
                if (b == 0.0) throw new IllegalArgumentException("Módulo por zero");
                return a % b;
            default: throw new IllegalArgumentException("Operador desconhecido: " + op);
        }
    }
}