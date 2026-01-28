package pt.escnaval.exercicios.manutencao.modelo;

import java.util.Objects;

public class DashboardConfig {
    private final String id;
    private Utilizador.Perfil perfil;
    private String widgets;
    private Integer pageSize;

    public DashboardConfig(String id, Utilizador.Perfil perfil, String widgets, Integer pageSize) {
        this.id = Objects.requireNonNull(id);
        this.perfil = Objects.requireNonNull(perfil);
        this.widgets = Objects.requireNonNull(widgets);
        this.pageSize = pageSize;
    }

    public String getId() { return id; }
    public Utilizador.Perfil getPerfil() { return perfil; }
    public String getWidgets() { return widgets; }
    public Integer getPageSize() { return pageSize; }

    public void setPerfil(Utilizador.Perfil perfil) { this.perfil = Objects.requireNonNull(perfil); }
    public void setWidgets(String widgets) { this.widgets = Objects.requireNonNull(widgets); }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }

    @Override
    public String toString() {
        return String.format("%s widgets=%s", perfil, widgets);
    }
}
