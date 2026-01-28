package pt.escnaval.exercicios.manutencao.modelo;

import java.time.Instant;
import java.util.Objects;

public class OrdemTrabalho {
    public enum Estado { ABERTA, ATRIBUIDA, EM_ANDAMENTO, SUSPENSA, CONCLUIDA, CANCELADA }
    public enum Tipo { CORRETIVA, PREVENTIVA, EXTERNA }

    private final String id;
    private Tipo tipo;
    private int prioridade;
    private Estado estado;
    private String descricao;
    private String idAtivo;
    private Instant dataCriacao;
    private Instant dataInicio;
    private Instant dataFim;
    private Instant dataLimite;
    private double custoTotal;
    private double custoPrevisto;
    private String idTecnico;
    private Integer slaHoras;
    private String idPedido;
    private String categoriaFalha;
    private String centroCusto;
    private String fornecedorId;
    private String ordemExterna;
    private int tempoGastoTotalMin;

    public OrdemTrabalho(String id, Tipo tipo, int prioridade, String descricao, String idAtivo) {
        this.id = Objects.requireNonNull(id);
        this.tipo = Objects.requireNonNull(tipo);
        this.prioridade = prioridade;
        this.descricao = Objects.requireNonNull(descricao);
        this.idAtivo = Objects.requireNonNull(idAtivo);
        this.estado = Estado.ABERTA;
        this.dataCriacao = Instant.now();
        this.tempoGastoTotalMin = 0;
    }

    public OrdemTrabalho(String id, Tipo tipo, int prioridade, Estado estado, String descricao, String idAtivo,
                         Instant dataCriacao, Instant dataInicio, Instant dataFim, Instant dataLimite,
                         double custoTotal, double custoPrevisto, String idTecnico, Integer slaHoras, String idPedido,
                         String categoriaFalha, String centroCusto, String fornecedorId, String ordemExterna, int tempoGastoTotalMin) {
        this.id = Objects.requireNonNull(id);
        this.tipo = Objects.requireNonNull(tipo);
        this.prioridade = prioridade;
        this.estado = Objects.requireNonNull(estado);
        this.descricao = Objects.requireNonNull(descricao);
        this.idAtivo = Objects.requireNonNull(idAtivo);
        this.dataCriacao = dataCriacao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.dataLimite = dataLimite;
        this.custoTotal = custoTotal;
        this.custoPrevisto = custoPrevisto;
        this.idTecnico = idTecnico;
        this.slaHoras = slaHoras;
        this.idPedido = idPedido;
        this.categoriaFalha = categoriaFalha;
        this.centroCusto = centroCusto;
        this.fornecedorId = fornecedorId;
        this.ordemExterna = ordemExterna;
        this.tempoGastoTotalMin = tempoGastoTotalMin;
    }

    public String getId() { return id; }
    public Tipo getTipo() { return tipo; }
    public int getPrioridade() { return prioridade; }
    public Estado getEstado() { return estado; }
    public String getDescricao() { return descricao; }
    public String getIdAtivo() { return idAtivo; }
    public Instant getDataCriacao() { return dataCriacao; }
    public Instant getDataInicio() { return dataInicio; }
    public Instant getDataFim() { return dataFim; }
    public Instant getDataLimite() { return dataLimite; }
    public double getCustoTotal() { return custoTotal; }
    public double getCustoPrevisto() { return custoPrevisto; }
    public String getIdTecnico() { return idTecnico; }
    public Integer getSlaHoras() { return slaHoras; }
    public String getIdPedido() { return idPedido; }
    public String getCategoriaFalha() { return categoriaFalha; }
    public String getCentroCusto() { return centroCusto; }
    public String getFornecedorId() { return fornecedorId; }
    public String getOrdemExterna() { return ordemExterna; }
    public int getTempoGastoTotalMin() { return tempoGastoTotalMin; }

    public void setTipo(Tipo tipo) { this.tipo = Objects.requireNonNull(tipo); }
    public void setPrioridade(int prioridade) { this.prioridade = prioridade; }
    public void setEstado(Estado estado) { this.estado = Objects.requireNonNull(estado); }
    public void setDescricao(String descricao) { this.descricao = Objects.requireNonNull(descricao); }
    public void setIdAtivo(String idAtivo) { this.idAtivo = Objects.requireNonNull(idAtivo); }
    public void setDataCriacao(Instant dataCriacao) { this.dataCriacao = dataCriacao; }
    public void setDataInicio(Instant dataInicio) { this.dataInicio = dataInicio; }
    public void setDataFim(Instant dataFim) { this.dataFim = dataFim; }
    public void setDataLimite(Instant dataLimite) { this.dataLimite = dataLimite; }
    public void setCustoTotal(double custoTotal) { this.custoTotal = custoTotal; }
    public void setCustoPrevisto(double custoPrevisto) { this.custoPrevisto = custoPrevisto; }
    public void setIdTecnico(String idTecnico) { this.idTecnico = idTecnico; }
    public void setSlaHoras(Integer slaHoras) { this.slaHoras = slaHoras; }
    public void setIdPedido(String idPedido) { this.idPedido = idPedido; }
    public void setCategoriaFalha(String categoriaFalha) { this.categoriaFalha = categoriaFalha; }
    public void setCentroCusto(String centroCusto) { this.centroCusto = centroCusto; }
    public void setFornecedorId(String fornecedorId) { this.fornecedorId = fornecedorId; }
    public void setOrdemExterna(String ordemExterna) { this.ordemExterna = ordemExterna; }

    public void adicionarTempo(int minutos) {
        if (minutos > 0) tempoGastoTotalMin += minutos;
    }

    public void adicionarCusto(double valor) {
        if (valor > 0) custoTotal += valor;
    }

    @Override
    public String toString() {
        String extra = fornecedorId == null ? "" : " forn:" + fornecedorId;
        return String.format("OT %s - %s [%s/%s] (prio:%d) ativo:%s tempo:%dmin%s",
                id, descricao, estado, tipo, prioridade, idAtivo, tempoGastoTotalMin, extra);
    }
}
