package pt.escnaval.exercicios.modelo;

import pt.escnaval.exercicios.exceptions.ContaBancariaException;
import pt.escnaval.exercicios.exceptions.SaldoInsuficienteException;
import pt.escnaval.exercicios.exceptions.OperacaoNaoPermitidaException;

/**
 * Conta poupança sem descoberto e com limite diário.
 * Demonstra POLIMORFISMO com regras diferentes.
 */
public class ContaPoupanca extends ContaBancariaBase {
    private static final double LIMITE_LEVANTAMENTO_DIARIO = 500.0;
    
    public ContaPoupanca(String numero, String titular, double saldoInicial) {
        super(numero, titular, saldoInicial);
    }
    
    @Override
    public void levantar(double montante) throws ContaBancariaException {
        if (montante <= 0) {
            throw new ContaBancariaException("montante a levantar deve ser > 0: " + montante);
        }
        
        if (montante > LIMITE_LEVANTAMENTO_DIARIO) {
            throw new OperacaoNaoPermitidaException(
                String.format("Levantamento excede limite diário (%.2f€ > %.2f€)", 
                montante, LIMITE_LEVANTAMENTO_DIARIO));
        }
        
        if (montante > saldo) {
            throw new SaldoInsuficienteException(saldo, montante);
        }
        
        saldo -= montante;
    }
    
    @Override
    public String getTipo() { return "Conta Poupança"; }
    
    public static double getLimiteLevantamentoDiario() {
        return LIMITE_LEVANTAMENTO_DIARIO;
    }
} 
