package pt.escnaval.exercicios;
public class AlunoRepo {
 private Aluno[] dados;
 private int tamanho;
 private static final int CAPACIDADE_INICIAL = 10;
 public AlunoRepo() {
 this.dados = new Aluno[CAPACIDADE_INICIAL];
 this.tamanho = 0;
 }
 private void redimensionar() {
 Aluno[] novoArray = new Aluno[dados.length * 2];
 for (int i = 0; i < tamanho; i++) {
 novoArray[i] = dados[i];
 }
 dados = novoArray;
 }
 public boolean adicionar(Aluno a) {
 if (findById(a.getId()) != null) return false;

 if (tamanho == dados.length) {
 redimensionar();
 }
 dados[tamanho] = a;
 tamanho++;
 return true;
 }
 public boolean removerPorId(int id) {
 int indice = -1;
 for (int i = 0; i < tamanho; i++) {
 if (dados[i].getId() == id) {
 indice = i;
 break;
 }
 }

 if (indice == -1) return false;

 for (int i = indice; i < tamanho - 1; i++) {
 dados[i] = dados[i + 1];
 }
 tamanho--;
 return true;
 }
 public Aluno findById(int id) {
 for (int i = 0; i < tamanho; i++) {
 if (dados[i].getId() == id) {
 return dados[i];
 }
 }
 return null;
 }
 public void listarPorId() {
 Aluno[] copia = new Aluno[tamanho];
 for (int i = 0; i < tamanho; i++) {
 copia[i] = dados[i];
 }
 ordenarPorId(copia, tamanho);
 for (int i = 0; i < tamanho; i++) {
 System.out.println(copia[i]);
 }
 }
 public void listarPorNome() {
 Aluno[] copia = new Aluno[tamanho];
 for (int i = 0; i < tamanho; i++) {
 copia[i] = dados[i];
 }
 ordenarPorNome(copia, tamanho);
 for (int i = 0; i < tamanho; i++) {
 System.out.println(copia[i]);
 }
 }
 private void ordenarPorId(Aluno[] arr, int n) {
 for (int i = 0; i < n - 1; i++) {
 for (int j = 0; j < n - i - 1; j++) {
 if (arr[j].getId() > arr[j + 1].getId()) {
 Aluno temp = arr[j];
 arr[j] = arr[j + 1];
 arr[j + 1] = temp;
 }
 }
 }
 }
 private void ordenarPorNome(Aluno[] arr, int n) {
 for (int i = 0; i < n - 1; i++) {
 for (int j = 0; j < n - i - 1; j++) {
 if (arr[j].getNome().compareTo(arr[j + 1].getNome()) > 0) {
 Aluno temp = arr[j];
 arr[j] = arr[j + 1];
 arr[j + 1] = temp;
 }
 }
 }
 }
 public void buscarPorNome(String termo) {
 String t = termo == null ? "" : termo.toLowerCase();
 boolean algum = false;
 for (int i = 0; i < tamanho; i++) {
 if (dados[i].getNome().toLowerCase().contains(t)) {
 System.out.println(dados[i]);
 algum = true;
 }
 }
 if (!algum) System.out.println("(nenhum resultado)");
 }
}
