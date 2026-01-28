package pt.escnaval.exercicios.manutencao.modelo;

import java.time.Instant;
import java.util.Objects;

public class InventarioCiclico {
    private final String id;
    private final String sku;
    private String deposito;
    private int quantidadeContada;
    private Instant data;
    private String utilizadorId;

    public InventarioCiclico(String id, String sku, String deposito, int quantidadeContada, Instant data, String utilizadorId) {
        this.id = Objects.requireNonNull(id);
        this.sku = Objects.requireNonNull(sku);
        this.deposito = Objects.requireNonNull(deposito);
        this.quantidadeContada = quantidadeContada;
        this.data = data;
        this.utilizadorId = utilizadorId;
    }

    public String getId() { return id; }
    public String getSku() { return sku; }
    public String getDeposito() { return deposito; }
    public int getQuantidadeContada() { return quantidadeContada; }
    public Instant getData() { return data; }
    public String getUtilizadorId() { return utilizadorId; }

    public void setDeposito(String deposito) { this.deposito = Objects.requireNonNull(deposito); }
    public void setQuantidadeContada(int quantidadeContada) { this.quantidadeContada = quantidadeContada; }
    public void setData(Instant data) { this.data = data; }
    public void setUtilizadorId(String utilizadorId) { this.utilizadorId = utilizadorId; }

    @Override
    public String toString() {
        return String.format("%s %s=%d (%s)", sku, deposito, quantidadeContada, data);
    }
}
