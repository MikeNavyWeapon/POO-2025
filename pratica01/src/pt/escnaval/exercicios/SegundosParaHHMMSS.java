package pt.escnaval.exercicios;

import java.util.Scanner;

public class SegundosParaHHMMSS {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Segundos totais: ");
        if (!sc.hasNextLong()) { System.out.println("Entrada inválida"); return; }
        long seconds = sc.nextLong();
        if (seconds < 0) { System.out.println("Entrada inválida"); return; }
        long hh = seconds / 3600;
        long mm = (seconds % 3600) / 60;
        long ss = seconds % 60;
        System.out.printf("%02d:%02d:%02d\n", hh, mm, ss);
    }
}
