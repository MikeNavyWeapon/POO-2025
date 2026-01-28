package pt.escnaval.exercicios.manutencao.modelo;

import java.util.Objects;

public class RegraGatilho {
    public enum Tipo { TEMPO, USO }

    private final String id;
    private final String planoId;
    private Tipo tipo;
    private double valor;
    private String unidade;

    public RegraGatilho(String id, String planoId, Tipo tipo, double valor, String unidade) {
        this.id = Objects.requireNonNull(id);
        this.planoId = Objects.requireNonNull(planoId);
        this.tipo = Objects.requireNonNull(tipo);
        this.valor = valor;
        this.unidade = Objects.requireNonNull(unidade);
    }

    public String getId() { return id; }
    public String getPlanoId() { return planoId; }
    public Tipo getTipo() { return tipo; }
    public double getValor() { return valor; }
    public String getUnidade() { return unidade; }

    public void setTipo(Tipo tipo) { this.tipo = Objects.requireNonNull(tipo); }
    public void setValor(double valor) { this.valor = valor; }
    public void setUnidade(String unidade) { this.unidade = Objects.requireNonNull(unidade); }

    @Override
    public String toString() {
        return String.format("Gatilho %s %s %.2f %s", id, tipo, valor, unidade);
    }
}
