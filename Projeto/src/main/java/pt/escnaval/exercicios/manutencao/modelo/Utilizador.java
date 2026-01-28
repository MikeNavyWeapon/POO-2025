package pt.escnaval.exercicios.manutencao.modelo;

import java.util.Objects;

public class Utilizador {
    public enum Perfil { TECNICO, PLANEADOR, GESTOR, SOLICITANTE }

    private final String id;
    private String nome;
    private String email;
    private Perfil perfil;
    private String passwordHash;
    private EstadoUtilizador estado;
    private String equipa;
    private Integer turnoInicio;
    private Integer turnoFim;

    public Utilizador(String id, String nome, String email, Perfil perfil, String passwordHash) {
        this(id, nome, email, perfil, passwordHash, EstadoUtilizador.ATIVO, null, null, null);
    }

    public Utilizador(String id, String nome, String email, Perfil perfil, String passwordHash,
                      EstadoUtilizador estado, String equipa, Integer turnoInicio, Integer turnoFim) {
        this.id = Objects.requireNonNull(id);
        this.nome = Objects.requireNonNull(nome);
        this.email = Objects.requireNonNull(email);
        this.perfil = Objects.requireNonNull(perfil);
        this.passwordHash = Objects.requireNonNull(passwordHash);
        this.estado = Objects.requireNonNull(estado);
        this.equipa = equipa;
        this.turnoInicio = turnoInicio;
        this.turnoFim = turnoFim;
    }

    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public Perfil getPerfil() { return perfil; }
    public String getPasswordHash() { return passwordHash; }
    public EstadoUtilizador getEstado() { return estado; }
    public String getEquipa() { return equipa; }
    public Integer getTurnoInicio() { return turnoInicio; }
    public Integer getTurnoFim() { return turnoFim; }

    public void setNome(String nome) { this.nome = Objects.requireNonNull(nome); }
    public void setEmail(String email) { this.email = Objects.requireNonNull(email); }
    public void setPerfil(Perfil perfil) { this.perfil = Objects.requireNonNull(perfil); }
    public void setPasswordHash(String passwordHash) { this.passwordHash = Objects.requireNonNull(passwordHash); }
    public void setEstado(EstadoUtilizador estado) { this.estado = Objects.requireNonNull(estado); }
    public void setEquipa(String equipa) { this.equipa = equipa; }
    public void setTurnoInicio(Integer turnoInicio) { this.turnoInicio = turnoInicio; }
    public void setTurnoFim(Integer turnoFim) { this.turnoFim = turnoFim; }

    @Override
    public String toString() {
        return String.format("%s (%s) [%s/%s]", nome, email, perfil, estado);
    }
}
