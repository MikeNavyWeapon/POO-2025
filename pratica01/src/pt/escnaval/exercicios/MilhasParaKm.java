package pt.escnaval.exercicios;

import java.util.Scanner;

public class MilhasParaKm {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Distância em milhas: ");
        if (!sc.hasNextDouble()) {
            System.out.println("Entrada inválida");
            return;
        }
        double miles = sc.nextDouble();
        double km = miles * 1.609;
        System.out.printf("%.3f milhas = %.3f km\n", miles, km);
    }
}
