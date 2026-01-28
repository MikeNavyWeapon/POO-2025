package pt.escnaval.exercicios.manutencao.modelo;

import java.util.Objects;

public class Ativo {
    private final String codigo;
    private String nome;
    private EstadoAtivo estado;
    private int criticidade;
    private String idAtivoPai;
    private String idLocalizacao;

    public Ativo(String codigo, String nome, int criticidade) {
        this(codigo, nome, EstadoAtivo.ATIVO, criticidade, null, null);
    }

    public Ativo(String codigo, String nome, EstadoAtivo estado, int criticidade, String idAtivoPai, String idLocalizacao) {
        this.codigo = Objects.requireNonNull(codigo);
        this.nome = Objects.requireNonNull(nome);
        this.estado = Objects.requireNonNull(estado);
        this.criticidade = criticidade;
        this.idAtivoPai = idAtivoPai;
        this.idLocalizacao = idLocalizacao;
    }

    public String getCodigo() { return codigo; }
    public String getNome() { return nome; }
    public EstadoAtivo getEstado() { return estado; }
    public int getCriticidade() { return criticidade; }
    public String getIdAtivoPai() { return idAtivoPai; }
    public String getIdLocalizacao() { return idLocalizacao; }

    public void setNome(String nome) { this.nome = Objects.requireNonNull(nome); }
    public void setEstado(EstadoAtivo estado) { this.estado = Objects.requireNonNull(estado); }
    public void setCriticidade(int criticidade) { this.criticidade = criticidade; }
    public void setIdAtivoPai(String idAtivoPai) { this.idAtivoPai = idAtivoPai; }
    public void setIdLocalizacao(String idLocalizacao) { this.idLocalizacao = idLocalizacao; }

    @Override
    public String toString() {
        return String.format("%s - %s (crit:%d, estado:%s)", codigo, nome, criticidade, estado);
    }
}
