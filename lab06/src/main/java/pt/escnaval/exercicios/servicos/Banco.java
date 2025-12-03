package pt.escnaval.exercicios.servicos;

import java.util.Arrays;
import pt.escnaval.exercicios.modelo.ContaBancaria;

/**
 * Serviço de gestão de contas bancárias usando arrays.
 * Demonstra POLIMORFISMO com array heterogéneo.
 */
public class Banco {
    private ContaBancaria[] contas;
    private int numeroContas;
    private static final int CAPACIDADE_INICIAL = 10;
    
    public Banco() {
        this.contas = new ContaBancaria[CAPACIDADE_INICIAL];
        this.numeroContas = 0;
    }
    
    public void adicionarConta(ContaBancaria conta) {
        if (conta == null) {
            throw new IllegalArgumentException("conta não pode ser null");
        }
        
        // Redimensiona se necessário
        if (numeroContas == contas.length) {
            redimensionar();
        }
        
        contas[numeroContas] = conta;
        numeroContas++;
    }
    
    public boolean removerConta(String numero) {
        for (int i = 0; i < numeroContas; i++) {
            if (contas[i].getNumero().equals(numero)) {
                // Reorganiza array (move elementos à direita para esquerda)
                for (int j = i; j < numeroContas - 1; j++) {
                    contas[j] = contas[j + 1];
                }
                contas[numeroContas - 1] = null; // Limpa referência
                numeroContas--;
                return true;
            }
        }
        return false;
    }
    
    public ContaBancaria buscarContaPorNumero(String numero) {
        for (int i = 0; i < numeroContas; i++) {
            if (contas[i].getNumero().equals(numero)) {
                return contas[i];
            }
        }
        return null; // Não encontrada
    }
    
    public void listarContas() {
        if (numeroContas == 0) {
            System.out.println("(nenhuma conta registada)");
            return;
        }
        
        System.out.println("=== CONTAS REGISTADAS ===");
        for (int i = 0; i < numeroContas; i++) {
            System.out.printf("%d) %s%n", i + 1, contas[i]);
        }
    }
    
    public double saldoTotal() {
        double total = 0.0;
        for (int i = 0; i < numeroContas; i++) {
            total += contas[i].getSaldo();
        }
        return total;
    }
    
    public void listarContasComDescoberto() {
        boolean encontrou = false;
        
        System.out.println("=== CONTAS COM DESCOBERTO ===");
        for (int i = 0; i < numeroContas; i++) {
            if (contas[i].getSaldo() < 0) {
                System.out.println(contas[i]);
                encontrou = true;
            }
        }
        
        if (!encontrou) {
            System.out.println("(nenhuma conta com descoberto)");
        }
    }
    
    public int getNumeroContas() {
        return numeroContas;
    }
    
    private void redimensionar() {
        int novaCapacidade = contas.length * 2;
        contas = Arrays.copyOf(contas, novaCapacidade);
    }
} 
