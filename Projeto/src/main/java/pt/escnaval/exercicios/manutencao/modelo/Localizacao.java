package pt.escnaval.exercicios.manutencao.modelo;

import java.util.Objects;

public class Localizacao {
    private final String id;
    private String nome;
    private String idPai;

    public Localizacao(String id, String nome, String idPai) {
        this.id = Objects.requireNonNull(id);
        this.nome = Objects.requireNonNull(nome);
        this.idPai = idPai;
    }

    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getIdPai() { return idPai; }

    public void setNome(String nome) { this.nome = Objects.requireNonNull(nome); }
    public void setIdPai(String idPai) { this.idPai = idPai; }

    @Override
    public String toString() { return String.format("%s - %s", id, nome); }
}
