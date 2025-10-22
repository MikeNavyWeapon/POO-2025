package pt.escnaval.exercicios;

import java.util.ArrayList;
import java.util.List;

public class Biblioteca {
    public static void main(String[] args) {
        // Criar pelo menos três instâncias separadas
        Livro l1 = new Livro("1984", "George Orwell", 1949, "Distopia", true);
        Livro l2 = new Livro("O Pequeno Príncipe", "Antoine de Saint-Exupéry", 1943, "Fábula", false);
        Livro l3 = new Livro("A Menina do Mar", "Sophia de Mello Breyner Andresen", 1958, "Infantil", true);

        System.out.println(l1.formatado(1));
        System.out.println();
        System.out.println(l2.formatado(2));
        System.out.println();
        System.out.println(l3.formatado(3));
        System.out.println();

        // Criar uma coleção de 10 instâncias (títulos/autor dummy)
        List<Livro> livros = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            livros.add(new Livro("Livro Dummy " + i, "Autor " + i, 2000 + i, "Género " + i, i % 2 == 0));
        }

        System.out.println("Lista de 10 livros (sumário):");
        int idx = 1;
        for (Livro l : livros) {
            System.out.printf("%d) %s — %s (%d) — %s\n", idx++, l.getTitulo(), l.getAutor(), l.getAnoPublicacao(), l.isDisponivel() ? "Disponível" : "Indisponível");
        }

        System.out.println();
        System.out.println("Exiba as informações de todos os livros utilizando o método formatado:");
        idx = 1;
        for (Livro l : livros) {
            System.out.println();
            System.out.println(l.formatado(idx));
            idx++;
        }
    }
}

class Livro {
    private String titulo;
    private String autor;
    private int anoPublicacao;
    private String genero;
    private boolean disponivel;

    // Construtor completo
    public Livro(String titulo, String autor, int anoPublicacao, String genero, boolean disponivel) {
        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacao = anoPublicacao;
        this.genero = genero;
        this.disponivel = disponivel;
    }

    // Construtor com apenas título e autor
    public Livro(String titulo, String autor) {
        this(titulo, autor, 0, "", true);
    }

    // Construtor sem parâmetros
    public Livro() {
        this("", "", 0, "", false);
    }

    // Getters
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public int getAnoPublicacao() { return anoPublicacao; }
    public String getGenero() { return genero; }
    public boolean isDisponivel() { return disponivel; }

    // Setters
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setAutor(String autor) { this.autor = autor; }
    public void setAnoPublicacao(int anoPublicacao) { this.anoPublicacao = anoPublicacao; }
    public void setGenero(String genero) { this.genero = genero; }
    public void setDisponivel(boolean disponivel) { this.disponivel = disponivel; }

    // Método adicional: retorna string formatada com as informações do livro
    public String formatado(int numero) {
        StringBuilder sb = new StringBuilder();
        sb.append("Livro ").append(numero).append(":\n");
        sb.append("Título: ").append(titulo == null || titulo.isEmpty() ? "N/A" : titulo).append("\n");
        sb.append("Autor: ").append(autor == null || autor.isEmpty() ? "N/A" : autor).append("\n");
        sb.append("Ano de Publicação: ").append(anoPublicacao <= 0 ? "N/A" : anoPublicacao).append("\n");
        sb.append("Género: ").append(genero == null || genero.isEmpty() ? "N/A" : genero).append("\n");
        sb.append("Disponibilidade: ").append(disponivel ? "Disponível" : "Indisponível");
        return sb.toString();
    }
}
