package pt.escnaval.exercicios.modelo;

import java.util.Locale;
import java.util.Objects;
import pt.escnaval.exercicios.exceptions.ContaInvalidaException;
import pt.escnaval.exercicios.exceptions.ContaBancariaException;

/**
 * Classe abstrata com comportamento comum a todas as contas.
 * Demonstra HERANÇA e REUTILIZAÇÃO.
 */
public abstract class ContaBancariaBase implements ContaBancaria {
    private final String numero;
    private final String titular;
    protected double saldo;
    
    protected ContaBancariaBase(String numero, String titular, double saldoInicial) {
        Objects.requireNonNull(numero, "número da conta não pode ser null");
        Objects.requireNonNull(titular, "titular não pode ser null");
        
        String num = numero.trim();
        String tit = titular.trim();
        
        if (num.isBlank()) {
            throw new ContaInvalidaException("número da conta vazio");
        }
        if (!validarFormatoNumero(num)) {
            throw new ContaInvalidaException("formato de número inválido: " + num);
        }
        if (tit.isBlank()) {
            throw new ContaInvalidaException("titular vazio");
        }
        if (saldoInicial < 0) {
            throw new ContaInvalidaException("saldo inicial não pode ser negativo: " + saldoInicial);
        }
        
        this.numero = num;
        this.titular = tit;
        this.saldo = saldoInicial;
    }
    
    private boolean validarFormatoNumero(String numero) {
        return numero.matches("PT\\d{2}-\\d{4}-\\d{8}");
    }
    
    @Override
    public final String getNumero() { return numero; }
    
    @Override
    public final String getTitular() { return titular; }
    
    @Override
    public final double getSaldo() { return saldo; }
    
    @Override
    public void depositar(double montante) throws ContaBancariaException {
        if (montante <= 0) {
            throw new ContaBancariaException("montante a depositar deve ser > 0: " + montante);
        }
        saldo += montante;
    }
    
    @Override
    public abstract void levantar(double montante) throws ContaBancariaException;
    
    @Override
    public String toString() {
        Locale ptPT = Locale.forLanguageTag("pt-PT");
        return String.format(ptPT, "%s [%s] %s saldo=%.2f€", 
            getTipo(), numero, titular, saldo);
    }
} 
