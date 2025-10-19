package pt.escnaval.exercicios;

import java.util.Scanner;

public class WorkTimeCalculator {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Indique hora e minuto (4 registos): chegada manhã, saída almoço, entrada após almoço, saída final");
        try {
            System.out.print("Chegada (HH MM): "); int h1 = sc.nextInt(), m1 = sc.nextInt();
            System.out.print("Saída almoço (HH MM): "); int h2 = sc.nextInt(), m2 = sc.nextInt();
            System.out.print("Entrada após almoço (HH MM): "); int h3 = sc.nextInt(), m3 = sc.nextInt();
            System.out.print("Saída final (HH MM): "); int h4 = sc.nextInt(), m4 = sc.nextInt();

            int start1 = h1 * 60 + m1;
            int end1 = h2 * 60 + m2;
            int start2 = h3 * 60 + m3;
            int end2 = h4 * 60 + m4;

            int worked = (end1 - start1) + (end2 - start2);
            if (worked < 0) { System.out.println("Tempos inválidos"); return; }
            int hours = worked / 60;
            int minutes = worked % 60;
            System.out.printf("Trabalhou %d horas e %d minutos\n", hours, minutes);
        } catch (Exception e) {
            System.out.println("Entrada inválida");
        }
    }
}
