package pt.escnaval.exercicios;

import java.util.Scanner;

public class MediaPonderada {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nota avaliação periódica (0-20): ");
        if (!sc.hasNextDouble()) { System.out.println("Entrada inválida"); return; }
        double perio = sc.nextDouble();
        System.out.print("Nota avaliação final (0-20): ");
        if (!sc.hasNextDouble()) { System.out.println("Entrada inválida"); return; }
        double finalNote = sc.nextDouble();

        double media = 0.3 * perio + 0.7 * finalNote;
        System.out.printf("Nota final: %.2f\n", media);
        if (perio < 6.0 || finalNote < 6.0) {
            System.out.println("Reprovado por uma das notas ser inferior à mínima");
        }
    }
}
