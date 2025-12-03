package pt.escnaval.exercicios.exceptions;

/**
 * Exceção (unchecked) para dados inválidos de conta.
 * Indica erro de Programação/violação de invariante.
 */
public class ContaInvalidaException extends RuntimeException {
    public ContaInvalidaException(String message) {
        super(message);
    }
    
    public ContaInvalidaException(String message, Throwable cause) {
        super(message, cause);
    }
} 
