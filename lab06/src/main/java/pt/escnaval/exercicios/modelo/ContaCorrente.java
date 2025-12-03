package pt.escnaval.exercicios.modelo;

import pt.escnaval.exercicios.exceptions.ContaBancariaException;
import pt.escnaval.exercicios.exceptions.SaldoInsuficienteException;

/**
 * Conta corrente com descoberto permitido.
 * Demonstra POLIMORFISMO.
 */
public class ContaCorrente extends ContaBancariaBase {
    private final double limiteDescoberto;
    
    public ContaCorrente(String numero, String titular, double saldoInicial, double limiteDescoberto) {
        super(numero, titular, saldoInicial);
        if (limiteDescoberto < 0) {
            throw new IllegalArgumentException("limite de descoberto não pode ser negativo");
        }
        this.limiteDescoberto = limiteDescoberto;
    }
    
    @Override
    public void levantar(double montante) throws ContaBancariaException {
        if (montante <= 0) {
            throw new ContaBancariaException("montante a levantar deve ser > 0: " + montante);
        }
        
        double saldoDisponivel = saldo + limiteDescoberto;
        if (montante > saldoDisponivel) {
            throw new SaldoInsuficienteException(saldo, montante);
        }
        
        saldo -= montante;
    }
    
    @Override
    public String getTipo() { return "Conta Corrente"; }
    
    public double getLimiteDescoberto() { return limiteDescoberto; }
} 
