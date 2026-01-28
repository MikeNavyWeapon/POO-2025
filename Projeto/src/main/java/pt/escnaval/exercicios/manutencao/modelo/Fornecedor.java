package pt.escnaval.exercicios.manutencao.modelo;

import java.util.Objects;

public class Fornecedor {
    private final String id;
    private String nome;
    private String contacto;
    private String email;
    private Integer slaHorasPadrao;

    public Fornecedor(String id, String nome, String contacto, String email, Integer slaHorasPadrao) {
        this.id = Objects.requireNonNull(id);
        this.nome = Objects.requireNonNull(nome);
        this.contacto = contacto;
        this.email = email;
        this.slaHorasPadrao = slaHorasPadrao;
    }

    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getContacto() { return contacto; }
    public String getEmail() { return email; }
    public Integer getSlaHorasPadrao() { return slaHorasPadrao; }

    public void setNome(String nome) { this.nome = Objects.requireNonNull(nome); }
    public void setContacto(String contacto) { this.contacto = contacto; }
    public void setEmail(String email) { this.email = email; }
    public void setSlaHorasPadrao(Integer slaHorasPadrao) { this.slaHorasPadrao = slaHorasPadrao; }

    @Override
    public String toString() {
        return String.format("%s - %s (SLA:%s)", id, nome, slaHorasPadrao == null ? "-" : slaHorasPadrao);
    }
}
