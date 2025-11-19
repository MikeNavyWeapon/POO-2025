 package pt.escnaval.exercicios;
import java.util.Scanner;
public class MenuAlunos {
 private static final AlunoRepo repo = new AlunoRepo();
 public static void main(String[] args) {
 try (Scanner sc = new Scanner(System.in)) {
 int op;
 do {
 mostrarMenu();
 op = UtilsIO.lerOpcao(sc, 0, 6);
 switch (op) {
 case 1 -> repo.listarPorId();
 case 2 -> repo.listarPorNome();
 case 3 -> adicionarFluxo(sc);
 case 4 -> removerFluxo(sc);
 case 5 -> buscarFluxo(sc);
 case 6 -> renomearFluxo(sc);
 case 0 -> System.out.println("A terminar...");
 default -> System.out.println("Opção inválida.");
 }
 System.out.println();
 } while (op != 0);
 }
 }
 static void mostrarMenu() {
 System.out.println("=== MENU ALUNOS ===");
 System.out.println("1) Listar por ID");
 System.out.println("2) Listar por Nome");
 System.out.println("3) Adicionar");
 System.out.println("4) Remover por ID");
 System.out.println("5) Buscar por termo");
 System.out.println("6) Renomear por ID");
 System.out.println("0) Sair");
 System.out.print("Opção (0..6) → ");
 }
 static void adicionarFluxo(Scanner sc) {
 int id = UtilsIO.lerInt(sc, "ID (inteiro > 0): ");
 System.out.print("Nome: ");
 String nome = sc.nextLine();
 try {
    boolean ok = repo.adicionar(new Aluno(id, nome));
 System.out.println(ok ? "Adicionado." : "Falha: ID já existente.");
 } catch (IllegalArgumentException e) {
 System.out.println("Erro: " + e.getMessage());
 }
 }
 static void removerFluxo(Scanner sc) {
 int id = UtilsIO.lerInt(sc, "ID a remover: ");
 boolean ok = repo.removerPorId(id);
 System.out.println(ok ? "Removido." : "ID não encontrado.");
 }
 static void buscarFluxo(Scanner sc) {
   System.out.print("Termo (parte do nome): ");
   String termo = sc.nextLine();
   // Usa o método disponível no repositório
   repo.buscarPorNome(termo);
 }
 static void renomearFluxo(Scanner sc) {
   int id = UtilsIO.lerInt(sc, "ID a renomear: ");
   Aluno a = repo.findById(id);
   if (a == null) {
      System.out.println("ID não encontrado.");
      return;
   }
   System.out.print("Novo nome: ");
   String novo = sc.nextLine();
   a.setNome(novo);
   if (a.getMensagemErro() != null && !a.getMensagemErro().isEmpty()) {
      System.out.println("Erro: " + a.getMensagemErro());
   } else {
      System.out.println("Atualizado.");
   }
 }
}