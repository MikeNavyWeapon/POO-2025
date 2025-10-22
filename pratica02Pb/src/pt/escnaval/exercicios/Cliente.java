package pt.escnaval.exercicios;

public class Cliente {
    private int id;
    private String nome;
    private double desconto;

    public Cliente(int aId, String aNome, double aDesconto) {
        id = aId;
        nome = aNome;
        desconto = aDesconto;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public double getDesconto() { return desconto; }
    public void setDesconto(double desconto) { this.desconto = desconto; }
    public void mostrar() { System.out.println("Nome(" + id + ")(" + desconto + ")"); }
}
