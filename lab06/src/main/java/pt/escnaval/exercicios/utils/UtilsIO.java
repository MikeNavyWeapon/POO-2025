package pt.escnaval.exercicios.utils;

import java.util.Scanner;

/**
 * Utilitários para leitura robusta com tratamento de Exceções.
 */
public final class UtilsIO {
    private UtilsIO() {}
    
    public static int lerInt(Scanner sc, String prompt) {
        System.out.print(prompt);
        while (true) {
            String linha = sc.nextLine().trim();
            try {
                return Integer.parseInt(linha);
            } catch (NumberFormatException e) {
                System.out.print("Entrada inválida. Introduza um número inteiro: ");
            }
        }
    }
    
    public static double lerDouble(Scanner sc, String prompt) {
        System.out.print(prompt);
        while (true) {
            String linha = sc.nextLine().trim();
            try {
                return Double.parseDouble(linha);
            } catch (NumberFormatException e) {
                System.out.print("Entrada inválida. Introduza um número: ");
            }
        }
    }
    
    public static int lerOpcao(Scanner sc, int min, int max) {
        while (true) {
            String linha = sc.nextLine().trim();
            try {
                int op = Integer.parseInt(linha);
                if (op < min || op > max) {
                    System.out.printf("Opção inválida. Escolha entre %d e %d: ", min, max);
                } else {
                    return op;
                }
            } catch (NumberFormatException e) {
                System.out.print("Entrada inválida. Introduza um número: ");
            }
        }
    }
    
    public static String lerStringNaoVazia(Scanner sc, String prompt) {
        System.out.print(prompt);
        while (true) {
            String linha = sc.nextLine().trim();
            if (!linha.isBlank()) {
                return linha;
            }
            System.out.print("Entrada vazia. Tente novamente: ");
        }
    }
} 
