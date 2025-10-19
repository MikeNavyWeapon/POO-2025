package pt.escnaval.exercicios;

import java.util.Scanner;

public class ConversorTemperatura {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Temperatura: ");
        if (!sc.hasNextDouble()) { System.out.println("Entrada inválida"); return; }
        double val = sc.nextDouble();
        System.out.print("Tipo (C/F): ");
        String t = sc.next();
        if (t.length() == 0) { System.out.println("Entrada inválida"); return; }
        char tipo = Character.toUpperCase(t.charAt(0));
        if (tipo == 'C') {
            double f = 1.8 * val + 32;
            System.out.printf("%.2f C equivale a %.2f F\n", val, f);
        } else if (tipo == 'F') {
            double c = (val - 32) / 1.8;
            System.out.printf("%.2f F equivale a %.2f C\n", val, c);
        } else {
            System.out.println("Tipo inválido (use C ou F)");
        }
    }
}
