package pt.escnaval.exercicios.manutencao.modelo;

import java.time.Instant;
import java.util.Objects;

public class PlanoPreventivo {
    public enum Politica { TEMPO, USO, MISTA }

    private final String id;
    private final String ativoCodigo;
    private Politica politica;
    private int periodicidadeDias;
    private int janelaDias;
    private Instant ultimoDisparo;

    public PlanoPreventivo(String id, String ativoCodigo, Politica politica, int periodicidadeDias, int janelaDias, Instant ultimoDisparo) {
        this.id = Objects.requireNonNull(id);
        this.ativoCodigo = Objects.requireNonNull(ativoCodigo);
        this.politica = Objects.requireNonNull(politica);
        this.periodicidadeDias = periodicidadeDias;
        this.janelaDias = janelaDias;
        this.ultimoDisparo = ultimoDisparo;
    }

    public String getId() { return id; }
    public String getAtivoCodigo() { return ativoCodigo; }
    public Politica getPolitica() { return politica; }
    public int getPeriodicidadeDias() { return periodicidadeDias; }
    public int getJanelaDias() { return janelaDias; }
    public Instant getUltimoDisparo() { return ultimoDisparo; }

    public void setPolitica(Politica politica) { this.politica = Objects.requireNonNull(politica); }
    public void setPeriodicidadeDias(int periodicidadeDias) { this.periodicidadeDias = periodicidadeDias; }
    public void setJanelaDias(int janelaDias) { this.janelaDias = janelaDias; }
    public void setUltimoDisparo(Instant ultimoDisparo) { this.ultimoDisparo = ultimoDisparo; }

    @Override
    public String toString() {
        return String.format("Plano %s ativo:%s %s %dd", id, ativoCodigo, politica, periodicidadeDias);
    }
}
