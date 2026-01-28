package pt.escnaval.exercicios.manutencao.modelo;

import java.util.Objects;

public class ConsumoPeca {
    private final String id;
    private final String otId;
    private final String sku;
    private int quantidade;
    private double custoUnit;

    public ConsumoPeca(String id, String otId, String sku, int quantidade, double custoUnit) {
        this.id = Objects.requireNonNull(id);
        this.otId = Objects.requireNonNull(otId);
        this.sku = Objects.requireNonNull(sku);
        this.quantidade = quantidade;
        this.custoUnit = custoUnit;
    }

    public String getId() { return id; }
    public String getOtId() { return otId; }
    public String getSku() { return sku; }
    public int getQuantidade() { return quantidade; }
    public double getCustoUnit() { return custoUnit; }
    public double getCustoTotal() { return custoUnit * quantidade; }

    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    public void setCustoUnit(double custoUnit) { this.custoUnit = custoUnit; }

    @Override
    public String toString() {
        return String.format("%s x%d (%.2f)", sku, quantidade, getCustoTotal());
    }
}
