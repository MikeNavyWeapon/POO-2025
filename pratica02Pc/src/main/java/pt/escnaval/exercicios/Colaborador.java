package pt.escnaval.exercicios;

import java.time.*;

public class Colaborador extends Pessoa {
   private double salario;
   private LocalDate dataContratacao;

   public Colaborador(String nome, int idade, double salario, int ano, int mes, int dia) {
      super(nome, idade);
      this.salario = salario;
      dataContratacao = LocalDate.of(ano, mes, dia);
   }

   public double getSalario() {
      return salario;
   }

   public LocalDate getDataContratacao() {
      return dataContratacao;
   }

   public String getDescricao() {
      return String.format("um colaborador com o salário de $%.2f", salario);
   }

   public void aumentarSalario(double percentagem) {
      double aumento = salario * percentagem / 100;
      salario += aumento;
   }

   @Override
   public String toString() {
      return super.toString() + " salario: " + getSalario() + " data de contratação: " + dataContratacao;
   }
}
