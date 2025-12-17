package pt.escnaval.exercicios.mediateca.modelo;

import java.util.Objects;

public class Faixa {
    private final int numero;
    private final String titulo;
    private final int duracaoSegundos;
    private String ficheiroMp3Path; // relativo (ex: media/..)

    public Faixa(int numero, String titulo, int duracaoSegundos) {
        this.numero = numero;
        this.titulo = Objects.requireNonNull(titulo);
        this.duracaoSegundos = duracaoSegundos;
    }

    public Faixa(int numero, String titulo, int duracaoSegundos, String ficheiroMp3Path) {
        this.numero = numero;
        this.titulo = Objects.requireNonNull(titulo);
        this.duracaoSegundos = duracaoSegundos;
        this.ficheiroMp3Path = ficheiroMp3Path;
    }

    public int getNumero() { return numero; }
    public String getTitulo() { return titulo; }
    public int getDuracaoSegundos() { return duracaoSegundos; }
    public String getFicheiroMp3Path() { return ficheiroMp3Path; }
    public void setFicheiroMp3Path(String p) { this.ficheiroMp3Path = p; }

    @Override
    public String toString() {
        return String.format("%02d. %s (%ds) %s", numero, titulo, duracaoSegundos,
            ficheiroMp3Path == null ? "" : "[" + ficheiroMp3Path + "]");
    }
}
