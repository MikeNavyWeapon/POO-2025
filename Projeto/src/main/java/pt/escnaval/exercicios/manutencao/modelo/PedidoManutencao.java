package pt.escnaval.exercicios.manutencao.modelo;

import java.time.Instant;
import java.util.Objects;

public class PedidoManutencao {
    public enum Estado { SUBMETIDO, APROVADO, REJEITADO, CONVERTIDO }

    private final String id;
    private final String ativoCodigo;
    private String descricao;
    private Estado estado;
    private Instant data;
    private String solicitanteId;
    private String aprovadorId;

    public PedidoManutencao(String id, String ativoCodigo, String descricao, Estado estado, Instant data,
                            String solicitanteId, String aprovadorId) {
        this.id = Objects.requireNonNull(id);
        this.ativoCodigo = Objects.requireNonNull(ativoCodigo);
        this.descricao = Objects.requireNonNull(descricao);
        this.estado = Objects.requireNonNull(estado);
        this.data = data;
        this.solicitanteId = solicitanteId;
        this.aprovadorId = aprovadorId;
    }

    public String getId() { return id; }
    public String getAtivoCodigo() { return ativoCodigo; }
    public String getDescricao() { return descricao; }
    public Estado getEstado() { return estado; }
    public Instant getData() { return data; }
    public String getSolicitanteId() { return solicitanteId; }
    public String getAprovadorId() { return aprovadorId; }

    public void setDescricao(String descricao) { this.descricao = Objects.requireNonNull(descricao); }
    public void setEstado(Estado estado) { this.estado = Objects.requireNonNull(estado); }
    public void setData(Instant data) { this.data = data; }
    public void setSolicitanteId(String solicitanteId) { this.solicitanteId = solicitanteId; }
    public void setAprovadorId(String aprovadorId) { this.aprovadorId = aprovadorId; }

    @Override
    public String toString() {
        return String.format("Pedido %s ativo:%s [%s]", id, ativoCodigo, estado);
    }
}
