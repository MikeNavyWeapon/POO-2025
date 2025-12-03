package pt.escnaval.exercicios.exceptions;

/**
 * Exceção (unchecked) para operações não permitidas.
 * Indica uso incorreto da API.
 */
public class OperacaoNaoPermitidaException extends RuntimeException {
    public OperacaoNaoPermitidaException(String message) {
        super(message);
    }
    
    public OperacaoNaoPermitidaException(String message, Throwable cause) {
        super(message, cause);
    }
} 
