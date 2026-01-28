package pt.escnaval.exercicios.manutencao.modelo;

import java.time.Instant;
import java.util.Objects;

public class Contador {
    public enum Tipo { TEMPO, USO }

    private final String id;
    private final String ativoCodigo;
    private Tipo tipo;
    private double leituraAtual;
    private String unidade;
    private Instant atualizadoEm;

    public Contador(String id, String ativoCodigo, Tipo tipo, double leituraAtual, String unidade, Instant atualizadoEm) {
        this.id = Objects.requireNonNull(id);
        this.ativoCodigo = Objects.requireNonNull(ativoCodigo);
        this.tipo = Objects.requireNonNull(tipo);
        this.leituraAtual = leituraAtual;
        this.unidade = Objects.requireNonNull(unidade);
        this.atualizadoEm = atualizadoEm;
    }

    public String getId() { return id; }
    public String getAtivoCodigo() { return ativoCodigo; }
    public Tipo getTipo() { return tipo; }
    public double getLeituraAtual() { return leituraAtual; }
    public String getUnidade() { return unidade; }
    public Instant getAtualizadoEm() { return atualizadoEm; }

    public void setTipo(Tipo tipo) { this.tipo = Objects.requireNonNull(tipo); }
    public void setLeituraAtual(double leituraAtual) { this.leituraAtual = leituraAtual; }
    public void setUnidade(String unidade) { this.unidade = Objects.requireNonNull(unidade); }
    public void setAtualizadoEm(Instant atualizadoEm) { this.atualizadoEm = atualizadoEm; }

    @Override
    public String toString() {
        return String.format("%s ativo:%s %s=%.2f (%s)", id, ativoCodigo, tipo, leituraAtual, unidade);
    }
}
