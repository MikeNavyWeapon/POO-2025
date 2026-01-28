package pt.escnaval.exercicios.manutencao.modelo;

import java.time.Instant;
import java.util.Objects;

public class Auditoria {
    private final String id;
    private final String entidade;
    private final String idEntidade;
    private final String campo;
    private final String valorAntigo;
    private final String valorNovo;
    private final Instant timestamp;
    private final String utilizadorId;

    public Auditoria(String id, String entidade, String idEntidade, String campo, String valorAntigo,
                     String valorNovo, Instant timestamp, String utilizadorId) {
        this.id = Objects.requireNonNull(id);
        this.entidade = Objects.requireNonNull(entidade);
        this.idEntidade = Objects.requireNonNull(idEntidade);
        this.campo = Objects.requireNonNull(campo);
        this.valorAntigo = valorAntigo;
        this.valorNovo = valorNovo;
        this.timestamp = Objects.requireNonNull(timestamp);
        this.utilizadorId = Objects.requireNonNull(utilizadorId);
    }

    public String getId() { return id; }
    public String getEntidade() { return entidade; }
    public String getIdEntidade() { return idEntidade; }
    public String getCampo() { return campo; }
    public String getValorAntigo() { return valorAntigo; }
    public String getValorNovo() { return valorNovo; }
    public Instant getTimestamp() { return timestamp; }
    public String getUtilizadorId() { return utilizadorId; }

    @Override
    public String toString() {
        return String.format("[%s] %s %s %s: %s -> %s",
                timestamp, entidade, idEntidade, campo, valorAntigo, valorNovo);
    }
}
