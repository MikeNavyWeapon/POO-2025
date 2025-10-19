package pt.escnaval.exercicios;

import java.util.Scanner;

public class CalculaDesconto {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Número de peças: ");
        if (!sc.hasNextInt()) { System.out.println("Entrada inválida"); return; }
        int n = sc.nextInt();
        System.out.print("Preço unitário (€): ");
        if (!sc.hasNextDouble()) { System.out.println("Entrada inválida"); return; }
        double p = sc.nextDouble();

        double total = n * p;
        double desconto;
        if (total < 2000) desconto = 0.02;
        else if (total < 5000) desconto = 0.04;
        else desconto = 0.075;

        double valorDesconto = total * desconto;
        double aPagar = total - valorDesconto;

        System.out.printf("Total sem desconto: %.2f€\nDesconto: %.2f%% (%.2f€)\nValor a pagar: %.2f€\n", total, desconto*100, valorDesconto, aPagar);
    }
}
