package pt.escnaval.exercicios.manutencao.modelo;

import java.util.Objects;

public class Documento {
    private final String id;
    private final String entidade;
    private final String entidadeId;
    private String tipo;
    private String path;
    private String meta;

    public Documento(String id, String entidade, String entidadeId, String tipo, String path, String meta) {
        this.id = Objects.requireNonNull(id);
        this.entidade = Objects.requireNonNull(entidade);
        this.entidadeId = Objects.requireNonNull(entidadeId);
        this.tipo = Objects.requireNonNull(tipo);
        this.path = Objects.requireNonNull(path);
        this.meta = meta;
    }

    public String getId() { return id; }
    public String getEntidade() { return entidade; }
    public String getEntidadeId() { return entidadeId; }
    public String getTipo() { return tipo; }
    public String getPath() { return path; }
    public String getMeta() { return meta; }

    public void setTipo(String tipo) { this.tipo = Objects.requireNonNull(tipo); }
    public void setPath(String path) { this.path = Objects.requireNonNull(path); }
    public void setMeta(String meta) { this.meta = meta; }

    @Override
    public String toString() {
        return String.format("%s %s -> %s", entidade, entidadeId, path);
    }
}
