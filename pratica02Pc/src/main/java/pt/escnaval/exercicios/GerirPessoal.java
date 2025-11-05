package pt.escnaval.exercicios;

public class GerirPessoal {
   public static void main(String[] args) {
      // construir um objeto Gestor
      Gestor gestor = new Gestor("António Matos", 50, 8000, 2018, 12, 15);
      gestor.setBonus(5000);

      Colaborador[] pessoal = new Colaborador[3];

      // preencher a tabela de pessoal com os objetos de Gestor e Colaborador

      pessoal[0] = gestor;
      pessoal[1] = new Colaborador("Maria Santos", 30, 5000, 2022, 10, 1);
      pessoal[2] = new Colaborador("Pedro Silva", 25, 4000, 2021, 3, 15);

      gestor.aumentarEquipa(pessoal[1]);
      gestor.aumentarEquipa(pessoal[2]);
      System.out.println("Equipa do gestor:");
      gestor.listarEquipa();
      System.out.println(" -- -- --");

      // mostrar informação de todos os objetos Colaborador

      System.out.println("\nColaboradores:");
      for (Colaborador c : pessoal)
         System.out.println(c);

      for (Colaborador c : pessoal)
         c.aumentarSalario(3.0);

      System.out.println("\nApós aumento:");
      for (Colaborador c : pessoal)
         System.out.println(c);

   }
}
