package pt.escnaval.exercicios;

import java.util.Scanner;

public class EstatisticasProducao {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Introduza quantidades diárias (inteiro). Introduza -1 para terminar.");
        int count = 0;
        long sum = 0;
        int below250 = 0;
        while (true) {
            if (!sc.hasNextInt()) { sc.next(); continue; }
            int v = sc.nextInt();
            if (v == -1) break;
            count++;
            sum += v;
            if (v < 250) below250++;
        }
        System.out.printf("Total de peças produzidas: %d\n", sum);
        if (count > 0) {
            double avg = (double) sum / count;
            System.out.printf("Média de peças por dia: %.2f\n", avg);
        } else {
            System.out.println("Média de peças por dia: N/A (sem dados)");
        }
        System.out.printf("Número de dias com produção inferior a 250 peças: %d\n", below250);
    }
}
