 package pt.escnaval.exercicios.exceptions;

/**
 * Exceção [verificada (checked)] lançada quando saldo é insuficiente.
 * Erro de negócio recuperável.
 */
public class SaldoInsuficienteException extends ContaBancariaException {
    private final double saldoAtual;
    private final double montanteSolicitado;
    
    public SaldoInsuficienteException(double saldoAtual, double montanteSolicitado) {
        super(String.format("Saldo insuficiente: saldo=%.2f€, solicitado=%.2f€", 
              saldoAtual, montanteSolicitado));
        this.saldoAtual = saldoAtual;
        this.montanteSolicitado = montanteSolicitado;
    }
    
    public double getSaldoAtual() { return saldoAtual; }
    public double getMontanteSolicitado() { return montanteSolicitado; }
}
