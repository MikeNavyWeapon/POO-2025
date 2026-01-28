package pt.escnaval.exercicios.manutencao.modelo;

import java.util.Objects;

public class TarefaOT {
    private final String id;
    private final String otId;
    private String descricao;
    private int duracaoPlaneadaMin;

    public TarefaOT(String id, String otId, String descricao, int duracaoPlaneadaMin) {
        this.id = Objects.requireNonNull(id);
        this.otId = Objects.requireNonNull(otId);
        this.descricao = Objects.requireNonNull(descricao);
        this.duracaoPlaneadaMin = duracaoPlaneadaMin;
    }

    public String getId() { return id; }
    public String getOtId() { return otId; }
    public String getDescricao() { return descricao; }
    public int getDuracaoPlaneadaMin() { return duracaoPlaneadaMin; }

    public void setDescricao(String descricao) { this.descricao = Objects.requireNonNull(descricao); }
    public void setDuracaoPlaneadaMin(int duracaoPlaneadaMin) { this.duracaoPlaneadaMin = duracaoPlaneadaMin; }

    @Override
    public String toString() { return String.format("%s - %s (%d min)", id, descricao, duracaoPlaneadaMin); }
}
