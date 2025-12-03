package pt.escnaval.exercicios.exceptions;

/**
 * Exceção base [verificada (checked)] para erros de negócio em operações bancárias.
 * Deve ser capturada e tratada pelo chamador.
 */
public class ContaBancariaException extends Exception {
    public ContaBancariaException(String message) {
        super(message);
    }
    
    public ContaBancariaException(String message, Throwable cause) {
        super(message, cause);
    }
}

