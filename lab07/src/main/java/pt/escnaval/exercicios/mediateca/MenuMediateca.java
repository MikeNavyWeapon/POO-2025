package pt.escnaval.exercicios.mediateca;

import pt.escnaval.exercicios.mediateca.modelo.Album;
import pt.escnaval.exercicios.mediateca.modelo.Faixa;
import pt.escnaval.exercicios.mediateca.servicos.Mediateca;
import pt.escnaval.exercicios.mediateca.servicos.RepositorioTexto;
import pt.escnaval.exercicios.mediateca.servicos.Mp3Util;
import pt.escnaval.exercicios.mediateca.utils.UtilsIO;

import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuMediateca {
    private static final Mediateca mediateca = new Mediateca();
    private static final Path DATA = Paths.get("data", "mediateca.csv");
    private static final Path MEDIA_DIR = Paths.get("media");

    public static void main(String[] args) {
        // carregar
        try {
            ArrayList<Album> carregados = RepositorioTexto.carregar(DATA);
            carregados.forEach(mediateca::adicionarAlbum);
            System.out.println("Mediateca: " + mediateca.tamanho() + " álbum(es) carregado(s)");
        } catch (Exception e) {
            System.out.println("Aviso: não foi possível carregar mediateca: " + e.getMessage());
        }

        try (Scanner sc = new Scanner(System.in)) {
            int op;
            do {
                mostrarMenu();
                System.out.print("Opção -> ");
                String line = sc.nextLine().trim();
                if (line.isEmpty()) { continue; }
                try { op = Integer.parseInt(line); }
                catch (NumberFormatException ex) { System.out.println("Opção inválida"); continue; }

                try {
                    switch (op) {
                        case 1 -> listarAlbuns();
                        case 2 -> adicionarAlbum(sc);
                        case 3 -> adicionarFaixa(sc);
                        case 4 -> removerAlbum(sc);
                        case 5 -> procurarPorAutor(sc);
                        case 6 -> copiarMp3(sc);
                        case 7 -> salvar();
                        case 0 -> { System.out.println("A terminar..."); return; }
                        default -> System.out.println("Opção inválida");
                    }
                } catch (Exception e) { System.out.println("Erro: " + e.getMessage()); }
            } while (true);
        }
    }

    static void mostrarMenu() {
        System.out.println("========== MEDIATECA ==========");
        System.out.println("1) Listar álbuns");
        System.out.println("2) Adicionar álbum");
        System.out.println("3) Adicionar faixa a álbum");
        System.out.println("4) Remover álbum");
        System.out.println("5) Procurar por autor");
        System.out.println("6) Copiar ficheiro mp3 para repositório local");
        System.out.println("7) Guardar mediateca");
        System.out.println("0) Sair");
    }

    static void listarAlbuns() {
        ArrayList<Album> l = mediateca.listarAlbuns();
        if (l.isEmpty()) { System.out.println("(nenhum álbum registado)"); return; }
        System.out.println("=== ÁLBUNS ===");
        int i = 1;
        for (Album a : l) {
            System.out.printf("%d) %s%n", i++, a);
            a.getFaixas().forEach(f -> System.out.println("   " + f));
        }
    }

    static void adicionarAlbum(Scanner sc) {
        System.out.println("--- Adicionar álbum ---");
        System.out.print("ID: ");
        String id = sc.nextLine().trim();
        System.out.print("Título: ");
        String titulo = sc.nextLine().trim();
        System.out.print("Autor: ");
        String autor = sc.nextLine().trim();
        if (id.isEmpty() || titulo.isEmpty() || autor.isEmpty()) { System.out.println("Campos obrigatórios em falta"); return; }
        Album a = new Album(id, titulo, autor);
        mediateca.adicionarAlbum(a);
        System.out.println("Álbum adicionado: " + a.getTitulo());
    }

    static void adicionarFaixa(Scanner sc) {
        System.out.println("--- Adicionar faixa ---");
        System.out.print("ID do álbum: ");
        String id = sc.nextLine().trim();
        Album a = mediateca.listarAlbuns().stream().filter(x -> x.getId().equals(id)).findFirst().orElse(null);
        if (a == null) { System.out.println("Álbum não encontrado"); return; }
        try {
            System.out.print("Número da faixa: ");
            int num = Integer.parseInt(sc.nextLine().trim());
            System.out.print("Título da faixa: ");
            String titulo = sc.nextLine().trim();
            System.out.print("Duração (s): ");
            int dur = Integer.parseInt(sc.nextLine().trim());
            System.out.print("Caminho mp3 (opcional): ");
            String path = sc.nextLine().trim();
            if (path.isEmpty()) path = null;
            Faixa f = new Faixa(num, titulo, dur, path);
            a.adicionarFaixa(f);
            System.out.println("Faixa adicionada ao álbum " + a.getTitulo());
        } catch (NumberFormatException ex) { System.out.println("Número/duração inválido"); }
        catch (IllegalArgumentException ex) { System.out.println("Erro: " + ex.getMessage()); }
    }

    static void removerAlbum(Scanner sc) {
        System.out.println("--- Remover álbum ---");
        System.out.print("ID do álbum: ");
        String id = sc.nextLine().trim();
        boolean rem = mediateca.removerAlbum(id);
        System.out.println(rem ? "Álbum removido" : "Álbum não encontrado");
    }

    static void procurarPorAutor(Scanner sc) {
        System.out.print("Autor: ");
        String autor = sc.nextLine().trim();
        ArrayList<Album> r = mediateca.buscarPorAutor(autor);
        if (r.isEmpty()) { System.out.println("Nenhum álbum encontrado para autor: " + autor); return; }
        r.forEach(a -> System.out.println(a));
    }

    static void copiarMp3(Scanner sc) {
        System.out.print("Caminho origem do mp3: ");
        String origem = sc.nextLine().trim();
        System.out.print("Nome destino (ex: exemplo.mp3): ");
        String destino = sc.nextLine().trim();
        if (origem.isEmpty() || destino.isEmpty()) { System.out.println("Parâmetros inválidos"); return; }
        try {
            Path o = Paths.get(origem);
            Path d = MEDIA_DIR.resolve(destino);
            Mp3Util.copy(o, d);
            System.out.println("Ficheiro copiado para: " + d.toAbsolutePath());
        } catch (Exception e) { System.out.println("Erro ao copiar mp3: " + e.getMessage()); }
    }

    static void salvar() {
        try {
            RepositorioTexto.salvar(DATA, mediateca.listarAlbuns());
            System.out.println("Mediateca guardada em: " + DATA.toAbsolutePath());
        } catch (Exception e) { System.out.println("Erro ao guardar: " + e.getMessage()); }
    }
} 
