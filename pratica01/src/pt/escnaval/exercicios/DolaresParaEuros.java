package pt.escnaval.exercicios;

import java.util.Scanner;

public class DolaresParaEuros {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Montante em dólares: ");
        if (!sc.hasNextDouble()) {
            System.out.println("Entrada inválida");
            return;
        }
        double dollars = sc.nextDouble();
        double rate = 1.2; // $1.2 = 1€
        double euros = dollars / rate;
        double commission = 2.0; // euros
        double result = euros - commission;
        if (result < 0) result = 0;
        System.out.printf("Valor em euros (após comissão de %.2f€): %.2f€\n", commission, result);
    }
}
