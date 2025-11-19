 package pt.escnaval.exercicios;
public class Aluno {
 private final int id;
 private String nome;
 private String mensagemErro;
 public Aluno(int id, String nome) {
 this.mensagemErro = "";

 if (id <= 0) {
 this.mensagemErro = "Erro: id deve ser > 0";
 this.id = -1;
 this.nome = "";
 return;
 }
 this.id = id;
 setNome(nome); // reutiliza validação
 }
 public int getId() { return id; }
 public String getNome() { return nome; }
 public String getMensagemErro() { return mensagemErro; }
 public void setNome(String nome) {
 this.mensagemErro = "";

 if (nome == null) {
 this.mensagemErro = "Erro: nome não pode ser null";
 return;
 }

 String n = nome.trim();
 if (n.isEmpty()) {
 this.mensagemErro = "Erro: nome não pode ser vazio";
 return;
 }
 this.nome = n;
 }
 @Override public String toString() {
 return String.format("%d\t%s", id, nome);
 }
 @Override public boolean equals(Object o) {
 if (this == o) return true;
 if (!(o instanceof Aluno)) return false;
 Aluno other = (Aluno) o;
 return id == other.id; // identidade pelo id
 }
 @Override public int hashCode() {
 return Integer.hashCode(id);
 }
}
