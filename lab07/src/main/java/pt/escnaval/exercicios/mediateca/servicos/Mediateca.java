package pt.escnaval.exercicios.mediateca.servicos;

import pt.escnaval.exercicios.mediateca.modelo.Album;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Mediateca {
    private final ArrayList<Album> albuns = new ArrayList<>();

    public void adicionarAlbum(Album a) { albuns.add(a); }
    public boolean removerAlbum(String id) { return albuns.removeIf(a -> a.getId().equals(id)); }
    public Album buscarPorId(String id) { 
        return albuns.stream().filter(a -> a.getId().equals(id)).findFirst().orElse(null); 
    }
    public ArrayList<Album> buscarPorAutor(String autor) {
        return new ArrayList<>(albuns.stream().filter(a -> a.getAutor().equalsIgnoreCase(autor)).collect(Collectors.toList()));
    }
    public ArrayList<Album> listarAlbuns() { return new ArrayList<>(albuns); }
    public int tamanho() { return albuns.size(); }
} 
