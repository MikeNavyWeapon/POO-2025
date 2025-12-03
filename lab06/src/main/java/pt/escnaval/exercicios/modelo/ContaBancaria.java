package pt.escnaval.exercicios.modelo;

import pt.escnaval.exercicios.exceptions.ContaBancariaException;

/**
 * Interface que define o contrato para todas as contas bancárias.
 * Demonstra ABSTRAÇÃO e POLIMORFISMO.
 */
public interface ContaBancaria {
    String getNumero();
    String getTitular();
    double getSaldo();
    void depositar(double montante) throws ContaBancariaException;
    void levantar(double montante) throws ContaBancariaException;
    String getTipo();
} 
