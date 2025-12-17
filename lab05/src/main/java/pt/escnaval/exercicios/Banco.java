package pt.escnaval.exercicios;
public class Banco {
 private Conta[] contas;
 private int numContas;
 public Banco(int capacidadeInicial) {
 this.contas = new Conta[capacidadeInicial > 0 ? capacidadeInicial : 10];
 this.numContas = 0;
 }
 public boolean abrirConta(Conta c) {
 if (c == null || !c.isValida()) return false;

 // Verificar se IBAN já existe
 if (findByIban(c.getIban()) != null) return false;

 // Verificar se há espaço
 if (numContas >= contas.length) {
 // Expandir array
 Conta[] novoArray = new Conta[contas.length * 2];
 for (int i = 0; i < numContas; i++) {
 novoArray[i] = contas[i];
 }
 contas = novoArray;
 }

 contas[numContas] = c;
 numContas++;
 return true;
 }
 public boolean fecharConta(Iban iban) {
 if (iban == null || !iban.isValid()) return false;

 for (int i = 0; i < numContas; i++) {
 if (contas[i].getIban().equals(iban)) {
 // Remover deslocando elementos
 for (int j = i; j < numContas - 1; j++) {
 contas[j] = contas[j + 1];
 }
 contas[numContas - 1] = null;
 numContas--;
 return true;
 }
 }
 return false;
 }
 public Conta findByIban(Iban iban) {
 if (iban == null || !iban.isValid()) return null;

 for (int i = 0; i < numContas; i++) {
 if (contas[i].getIban().equals(iban)) {
 return contas[i];
 }
 }
 return null;
 }
 public Conta[] getContas() {
 Conta[] copia = new Conta[numContas];
 for (int i = 0; i < numContas; i++) {
 copia[i] = contas[i];
 }
 return copia;
 }
 public int getNumContas() {
 return numContas;
 }
}
