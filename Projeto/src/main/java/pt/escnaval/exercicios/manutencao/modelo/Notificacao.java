package pt.escnaval.exercicios.manutencao.modelo;

import java.time.Instant;
import java.util.Objects;

public class Notificacao {
    public enum Estado { NOVA, ENVIADA }

    private final String id;
    private String tipo;
    private String mensagem;
    private Instant data;
    private Estado estado;

    public Notificacao(String id, String tipo, String mensagem, Instant data, Estado estado) {
        this.id = Objects.requireNonNull(id);
        this.tipo = Objects.requireNonNull(tipo);
        this.mensagem = Objects.requireNonNull(mensagem);
        this.data = data;
        this.estado = Objects.requireNonNull(estado);
    }

    public String getId() { return id; }
    public String getTipo() { return tipo; }
    public String getMensagem() { return mensagem; }
    public Instant getData() { return data; }
    public Estado getEstado() { return estado; }

    public void setTipo(String tipo) { this.tipo = Objects.requireNonNull(tipo); }
    public void setMensagem(String mensagem) { this.mensagem = Objects.requireNonNull(mensagem); }
    public void setData(Instant data) { this.data = data; }
    public void setEstado(Estado estado) { this.estado = Objects.requireNonNull(estado); }

    @Override
    public String toString() {
        return String.format("[%s] %s: %s", estado, tipo, mensagem);
    }
}
