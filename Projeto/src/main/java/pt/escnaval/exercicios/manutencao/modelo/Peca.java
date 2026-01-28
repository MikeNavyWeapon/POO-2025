package pt.escnaval.exercicios.manutencao.modelo;

import java.util.Objects;

public class Peca {
    private final String sku;
    private String designacao;
    private String unidade;
    private int pontoReposicao;
    private double custoUnit;

    public Peca(String sku, String designacao, String unidade, int pontoReposicao, double custoUnit) {
        this.sku = Objects.requireNonNull(sku);
        this.designacao = Objects.requireNonNull(designacao);
        this.unidade = Objects.requireNonNull(unidade);
        this.pontoReposicao = pontoReposicao;
        this.custoUnit = custoUnit;
    }

    public String getSku() { return sku; }
    public String getDesignacao() { return designacao; }
    public String getUnidade() { return unidade; }
    public int getPontoReposicao() { return pontoReposicao; }
    public double getCustoUnit() { return custoUnit; }

    public void setDesignacao(String designacao) { this.designacao = Objects.requireNonNull(designacao); }
    public void setUnidade(String unidade) { this.unidade = Objects.requireNonNull(unidade); }
    public void setPontoReposicao(int pontoReposicao) { this.pontoReposicao = pontoReposicao; }
    public void setCustoUnit(double custoUnit) { this.custoUnit = custoUnit; }

    @Override
    public String toString() {
        return String.format("%s - %s [%s] (repos:%d, custo:%.2f)", sku, designacao, unidade, pontoReposicao, custoUnit);
    }
}
