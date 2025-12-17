package pt.escnaval.exercicios;

/**
 * Interface que define o contrato para objetos que podem ser autenticados.
 * Qualquer classe que implemente esta interface deve fornecer um mecanismo
 * de autenticação e um identificador único.
 */
public interface Autenticavel {
    /**
     * Autentica o objeto com a senha fornecida.
     * @param senha a senha a validar
     * @return true se autenticação bem-sucedida, false caso contrário
     */
    boolean autenticar(String senha);
    
    /**
     * Obtém o identificador único do objeto autenticável.
     * @return identificador (ex: email, username)
     */
    String getIdentificador();
}
