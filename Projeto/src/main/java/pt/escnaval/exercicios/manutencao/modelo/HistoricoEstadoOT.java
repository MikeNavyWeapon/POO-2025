package pt.escnaval.exercicios.manutencao.modelo;

import java.time.Instant;
import java.util.Objects;

public class HistoricoEstadoOT {
    private final String id;
    private final String otId;
    private OrdemTrabalho.Estado estado;
    private Instant data;
    private String utilizadorId;

    public HistoricoEstadoOT(String id, String otId, OrdemTrabalho.Estado estado, Instant data, String utilizadorId) {
        this.id = Objects.requireNonNull(id);
        this.otId = Objects.requireNonNull(otId);
        this.estado = Objects.requireNonNull(estado);
        this.data = data;
        this.utilizadorId = utilizadorId;
    }

    public String getId() { return id; }
    public String getOtId() { return otId; }
    public OrdemTrabalho.Estado getEstado() { return estado; }
    public Instant getData() { return data; }
    public String getUtilizadorId() { return utilizadorId; }

    public void setEstado(OrdemTrabalho.Estado estado) { this.estado = Objects.requireNonNull(estado); }
    public void setData(Instant data) { this.data = data; }
    public void setUtilizadorId(String utilizadorId) { this.utilizadorId = utilizadorId; }

    @Override
    public String toString() {
        return String.format("%s %s %s", otId, estado, data);
    }
}
