package pt.escnaval.exercicios.manutencao.modelo;

import java.util.Objects;

public class Parametro {
    public enum Tipo {
        PRIORIDADE,
        CATEGORIA_FALHA,
        CENTRO_CUSTO,
        SLA_PRIORIDADE,
        RETENCAO_AUDITORIA_DIAS,
        RETENCAO_EXECUCAO_DIAS
    }

    private final String id;
    private Tipo tipo;
    private String codigo;
    private String descricao;

    public Parametro(String id, Tipo tipo, String codigo, String descricao) {
        this.id = Objects.requireNonNull(id);
        this.tipo = Objects.requireNonNull(tipo);
        this.codigo = Objects.requireNonNull(codigo);
        this.descricao = Objects.requireNonNull(descricao);
    }

    public String getId() { return id; }
    public Tipo getTipo() { return tipo; }
    public String getCodigo() { return codigo; }
    public String getDescricao() { return descricao; }

    public void setTipo(Tipo tipo) { this.tipo = Objects.requireNonNull(tipo); }
    public void setCodigo(String codigo) { this.codigo = Objects.requireNonNull(codigo); }
    public void setDescricao(String descricao) { this.descricao = Objects.requireNonNull(descricao); }

    @Override
    public String toString() { return String.format("%s %s - %s", tipo, codigo, descricao); }
}
