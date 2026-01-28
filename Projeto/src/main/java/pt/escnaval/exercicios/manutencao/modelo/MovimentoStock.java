package pt.escnaval.exercicios.manutencao.modelo;

import java.time.Instant;
import java.util.Objects;

public class MovimentoStock {
    public enum Tipo { ENTRADA, SAIDA, AJUSTE }

    private final String id;
    private final String sku;
    private String deposito;
    private Tipo tipo;
    private Instant data;
    private int quantidade;
    private String otId;

    public MovimentoStock(String id, String sku, String deposito, Tipo tipo, Instant data, int quantidade, String otId) {
        this.id = Objects.requireNonNull(id);
        this.sku = Objects.requireNonNull(sku);
        this.deposito = Objects.requireNonNull(deposito);
        this.tipo = Objects.requireNonNull(tipo);
        this.data = data;
        this.quantidade = quantidade;
        this.otId = otId;
    }

    public String getId() { return id; }
    public String getSku() { return sku; }
    public String getDeposito() { return deposito; }
    public Tipo getTipo() { return tipo; }
    public Instant getData() { return data; }
    public int getQuantidade() { return quantidade; }
    public String getOtId() { return otId; }

    public void setDeposito(String deposito) { this.deposito = Objects.requireNonNull(deposito); }
    public void setTipo(Tipo tipo) { this.tipo = Objects.requireNonNull(tipo); }
    public void setData(Instant data) { this.data = data; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    public void setOtId(String otId) { this.otId = otId; }

    @Override
    public String toString() {
        return String.format("%s %s %d (%s)", sku, tipo, quantidade, deposito);
    }
}
