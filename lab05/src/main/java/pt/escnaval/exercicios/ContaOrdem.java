package pt.escnaval.exercicios;

import java.util.Locale;

public class ContaOrdem extends Conta {
    private final double limiteDescoberto;

    public ContaOrdem(Iban iban, Cliente titular, double saldoInicial, double limiteDescoberto) {
        super(iban, titular, saldoInicial);
        this.limiteDescoberto = limiteDescoberto >= 0 ? limiteDescoberto : 0;
    }

    public double getLimiteDescoberto() {
        return limiteDescoberto;
    }

    @Override
    public boolean levantar(double valor) {
        if (!isValida() || valor <= 0) return false;
        
        // Permite saldo negativo até -limiteDescoberto
        if (saldo - valor < -limiteDescoberto) {
            return false;
        }
        
        saldo -= valor;
        return true;
    }

    @Override
    public String toString() {
        if (!isValida()) return "[ContaOrdem inválida]";
        return String.format(Locale.US, "ContaOrdem %s\t%-18s saldo=%.2f (limite desc.: %.2f)",
                getIban(), getTitular().getNome(), saldo, limiteDescoberto);
    }
}