package pt.escnaval.exercicios;
import java.util.Locale;
public class Conta {
 private final Iban iban; // identidade estável
 private final Cliente titular; // composição
	protected double saldo; // invariante: >= 0
 private final boolean valida;
 public Conta(Iban iban, Cliente titular, double saldoInicial) {
 this.iban = iban;
 this.titular = titular;
 // Validação sem exceções
 if (iban == null || !iban.isValid() ||
 titular == null || !titular.isValido() ||
 saldoInicial < 0) {
 this.saldo = 0;
 this.valida = false;
 } else {
 this.saldo = saldoInicial;
 this.valida = true;
 }
 }
 public boolean isValida() { return valida; }
 public Iban getIban() { return iban; }
 public Cliente getTitular() { return titular; }
 public double getSaldo() { return saldo; }
 public boolean depositar(double valor) {
 if (!valida || valor <= 0) return false;
 saldo += valor;
 return true;
 }
 public boolean levantar(double valor) {
 if (!valida || valor <= 0 || valor > saldo) return false;
 saldo -= valor;
 return true;
 }
 public boolean transferirPara(Conta destino, double valor) {
 if (!valida || destino == null || !destino.isValida() || this == destino) {
 return false;
 }
 if (!this.levantar(valor)) return false;
 if (!destino.depositar(valor)) {
 // Reverter levantamento
 this.saldo += valor;
 return false;
 }
 return true;
 }
 @Override
 public String toString() {
 if (!valida) return "[Conta inválida]";
 return String.format(Locale.US, "%s\t%-18s saldo=%.2f",
 iban, titular.getNome(), saldo);
 }
 @Override
 public boolean equals(Object o) {
 if (this == o) return true;
 if (!(o instanceof Conta)) return false;
 Conta other = (Conta) o;
 return iban.equals(other.iban);
 }
 @Override
 public int hashCode() {
 return iban.hashCode();
 }
}