package pt.escnaval.exercicios.manutencao.modelo;

import java.util.Objects;

public class Stock {
    private final String id;
    private final String sku;
    private String deposito;
    private int quantidade;

    public Stock(String id, String sku, String deposito, int quantidade) {
        this.id = Objects.requireNonNull(id);
        this.sku = Objects.requireNonNull(sku);
        this.deposito = Objects.requireNonNull(deposito);
        this.quantidade = quantidade;
    }

    public String getId() { return id; }
    public String getSku() { return sku; }
    public String getDeposito() { return deposito; }
    public int getQuantidade() { return quantidade; }

    public void setDeposito(String deposito) { this.deposito = Objects.requireNonNull(deposito); }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    @Override
    public String toString() {
        return String.format("%s %s=%d", sku, deposito, quantidade);
    }
}
