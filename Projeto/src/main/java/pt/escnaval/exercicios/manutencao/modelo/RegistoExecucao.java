package pt.escnaval.exercicios.manutencao.modelo;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public class RegistoExecucao {
    private final String id;
    private final String otId;
    private Instant inicio;
    private Instant fim;
    private String causa;
    private String acao;
    private String tecnicoId;
    private String observacoes;

    public RegistoExecucao(String id, String otId, Instant inicio, Instant fim, String causa, String acao,
                           String tecnicoId, String observacoes) {
        this.id = Objects.requireNonNull(id);
        this.otId = Objects.requireNonNull(otId);
        this.inicio = inicio;
        this.fim = fim;
        this.causa = causa;
        this.acao = acao;
        this.tecnicoId = tecnicoId;
        this.observacoes = observacoes;
    }

    public String getId() { return id; }
    public String getOtId() { return otId; }
    public Instant getInicio() { return inicio; }
    public Instant getFim() { return fim; }
    public String getCausa() { return causa; }
    public String getAcao() { return acao; }
    public String getTecnicoId() { return tecnicoId; }
    public String getObservacoes() { return observacoes; }

    public void setInicio(Instant inicio) { this.inicio = inicio; }
    public void setFim(Instant fim) { this.fim = fim; }
    public void setCausa(String causa) { this.causa = causa; }
    public void setAcao(String acao) { this.acao = acao; }
    public void setTecnicoId(String tecnicoId) { this.tecnicoId = tecnicoId; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public long getDuracaoMin() {
        if (inicio == null || fim == null) return 0;
        return Duration.between(inicio, fim).toMinutes();
    }

    @Override
    public String toString() {
        return String.format("Exec %s %s->%s tecnico:%s", id, inicio, fim, tecnicoId);
    }
}
