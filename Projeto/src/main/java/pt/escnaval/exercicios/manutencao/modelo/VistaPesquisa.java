package pt.escnaval.exercicios.manutencao.modelo;

import java.util.Objects;

public class VistaPesquisa {
    private final String id;
    private String utilizadorId;
    private String entidade;
    private String filtro;
    private String ordenacao;
    private Integer pageSize;

    public VistaPesquisa(String id, String utilizadorId, String entidade, String filtro, String ordenacao, Integer pageSize) {
        this.id = Objects.requireNonNull(id);
        this.utilizadorId = Objects.requireNonNull(utilizadorId);
        this.entidade = Objects.requireNonNull(entidade);
        this.filtro = filtro;
        this.ordenacao = ordenacao;
        this.pageSize = pageSize;
    }

    public String getId() { return id; }
    public String getUtilizadorId() { return utilizadorId; }
    public String getEntidade() { return entidade; }
    public String getFiltro() { return filtro; }
    public String getOrdenacao() { return ordenacao; }
    public Integer getPageSize() { return pageSize; }

    public void setUtilizadorId(String utilizadorId) { this.utilizadorId = Objects.requireNonNull(utilizadorId); }
    public void setEntidade(String entidade) { this.entidade = Objects.requireNonNull(entidade); }
    public void setFiltro(String filtro) { this.filtro = filtro; }
    public void setOrdenacao(String ordenacao) { this.ordenacao = ordenacao; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }

    @Override
    public String toString() {
        return String.format("%s %s filtro=%s ordenacao=%s", utilizadorId, entidade, filtro, ordenacao);
    }
}
