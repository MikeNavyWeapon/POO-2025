package pt.escnaval.exercicios;

import java.util.Locale;

public class ContaPoupanca extends Conta {
    private final double taxaJuroAnual; // percentagem (ex: 2.5 = 2.5%)

    public ContaPoupanca(Iban iban, Cliente titular, double saldoInicial, double taxaJuroAnual) {
        super(iban, titular, saldoInicial);
        this.taxaJuroAnual = taxaJuroAnual >= 0 ? taxaJuroAnual : 0;
    }

    public double getTaxaJuroAnual() {
        return taxaJuroAnual;
    }

    /**
     * Calcula juros baseado no saldo atual e taxa anual.
     * @return valor dos juros
     */
    public double calcularJuros() {
        return saldo * (taxaJuroAnual / 100.0);
    }

    /**
     * Aplica os juros ao saldo.
     * @return true se aplicado com sucesso
     */
    public boolean aplicarJuros() {
        if (!isValida() || saldo <= 0) return false;
        double juros = calcularJuros();
        saldo += juros;
        return true;
    }

    @Override
    public String toString() {
        if (!isValida()) return "[ContaPoupanca inválida]";
        return String.format(Locale.US, "ContaPoupanca %s\t%-18s saldo=%.2f (taxa: %.2f%%)",
                getIban(), getTitular().getNome(), saldo, taxaJuroAnual);
    }
} 
