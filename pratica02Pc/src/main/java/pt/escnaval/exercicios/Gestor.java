package pt.escnaval.exercicios;

public class Gestor extends Colaborador {
   private double bonus;
   private Colaborador[] membros;
   private int nrMembros = 0;
   private final int MAX_DEPENDENTES = 10;

   /**
    * @param nome    nome do colaborador
    * @param salário o salario
    * @param ano     ano de contratação
    * @param mes     mês de contratação
    * @param dia     dia de contratação
    */
   public Gestor(String nome, int idade, double salario, int ano, int mes, int dia) {
      super(nome, idade, salario, ano, mes, dia);
      membros = new Colaborador[MAX_DEPENDENTES];
      bonus = 0;
   }

   @Override
   public double getSalario() {
      double baseSalario = super.getSalario();
      return baseSalario + bonus;
   }

   public void setBonus(double b) {
      bonus = b;
   }

   public void aumentarEquipa(Colaborador membro) {
      if (nrMembros >= MAX_DEPENDENTES)
         return;
      membros[nrMembros++] = membro;

   }

   public void listarEquipa() {
      for (Colaborador membro : membros) {
         if (membro != null) {
            System.out.println(membro);
         } else {
            break;
         }
      }
   }

}
