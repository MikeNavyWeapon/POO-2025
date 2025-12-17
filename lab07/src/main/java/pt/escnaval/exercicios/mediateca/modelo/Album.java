package pt.escnaval.exercicios.mediateca.modelo;

import java.util.ArrayList;
import java.util.Objects;

public class Album {
    private final String id;
    private final String titulo;
    private final String autor;
    private final ArrayList<Faixa> faixas = new ArrayList<>();

    public Album(String id, String titulo, String autor) {
        this.id = Objects.requireNonNull(id);
        this.titulo = Objects.requireNonNull(titulo);
        this.autor = Objects.requireNonNull(autor);
    }

    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public ArrayList<Faixa> getFaixas() { return faixas; }

    public void adicionarFaixa(Faixa f) { faixas.add(f); }

    @Override
    public String toString() {
        return String.format("%s — %s (%s) [%d faixas]", id, titulo, autor, faixas.size());
    }
}
