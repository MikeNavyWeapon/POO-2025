 package pt.escnaval.exercicios;
import java.util.Scanner;
public final class UtilsIO {
 private UtilsIO() {}
 private static boolean ehInteiro(String s) {
 if (s == null || s.trim().isEmpty()) {
 return false;
 }
 String trimmed = s.trim();
 if (trimmed.startsWith("-")) {
 trimmed = trimmed.substring(1);
 }
 for (char c : trimmed.toCharArray()) {
 if (!Character.isDigit(c)) {
 return false;
 }
 }
 return true;
 }
 public static int lerInt(Scanner sc, String prompt) {
    System.out.print(prompt);
 while (true) {
 String s = sc.nextLine();
 if (ehInteiro(s)) {
 return Integer.parseInt(s.trim());
 }
 System.out.print("Inteiro inválido. Tente novamente: ");
 }
 }
 public static int lerOpcao(Scanner sc, int min, int max) {
 while (true) {
 String s = sc.nextLine();
 if (ehInteiro(s)) {
 int op = Integer.parseInt(s.trim());
            if (op < min || op > max) {
               System.out.printf("Opção fora do intervalo. (%d..%d) → ", min, max);
            } else {
 return op;
 }
 } else {
 System.out.print("Opção inválida. Introduza um número: ");
 }
 }
 }
}