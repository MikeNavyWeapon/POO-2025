package pt.escnaval.exercicios;

import java.util.Scanner;

public class CalculadoraEmprestimo {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Valor do empréstimo (euros): ");
        if (!sc.hasNextDouble()) { System.out.println("Entrada inválida"); return; }
        double valor = sc.nextDouble();

        System.out.print("Duração (anos): ");
        if (!sc.hasNextInt()) { System.out.println("Entrada inválida"); return; }
        int anos = sc.nextInt();

        System.out.print("Taxa de juro anual (ex.: 0.05 para 5%): ");
        if (!sc.hasNextDouble()) { System.out.println("Entrada inválida"); return; }
        double taxaAnual = sc.nextDouble();

        int numMeses = anos * 12;
        double juroMes = taxaAnual / 12.0;
        double baseMes = 1.0 + juroMes;

        if (juroMes == 0) {
            double mensalidade = valor / numMeses;
            double total = mensalidade * numMeses;
            System.out.printf("Mensalidade: %.2f EUR\nTotal a pagar: %.2f EUR\n", mensalidade, total);
            return;
        }

        double pow = Math.pow(baseMes, numMeses);
        double mensalidade = (juroMes * pow * valor) / (pow - 1.0);
        double total = mensalidade * numMeses;

        System.out.printf("Mensalidade: %.2f EUR\nTotal a pagar: %.2f EUR\n", mensalidade, total);
    }
}
