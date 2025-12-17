package pt.escnaval.exercicios.mediateca.servicos;

import pt.escnaval.exercicios.mediateca.modelo.Album;
import pt.escnaval.exercicios.mediateca.modelo.Faixa;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class RepositorioTexto {
    private static final String SEP_CAMPO = ";";
    private static final String SEP_FAIXA = ",";
    private static final String SEP_INT = "|";

    public static ArrayList<Album> carregar(Path ficheiro) throws IOException {
        ArrayList<Album> resultado = new ArrayList<>();
        if (!Files.exists(ficheiro)) return resultado;
        try (var r = Files.newBufferedReader(ficheiro, StandardCharsets.UTF_8)) {
            String linha;
            while ((linha = r.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty()) continue;
                String[] campos = linha.split(SEP_CAMPO, -1);
                String id = campos[0];
                String titulo = campos.length > 1 ? campos[1] : "";
                String autor = campos.length > 2 ? campos[2] : "";
                Album a = new Album(id, titulo, autor);
                if (campos.length > 3 && !campos[3].isBlank()) {
                    String[] faixas = campos[3].split(SEP_FAIXA);
                    for (String f : faixas) {
                        String[] parts = f.split("\\|", -1);
                        int num = Integer.parseInt(parts[0]);
                        int dur = Integer.parseInt(parts[1]);
                        String path = parts.length > 2 ? parts[2] : null;
                        Faixa faixa = new Faixa(num, "[import]", dur, path);
                        a.adicionarFaixa(faixa);
                    }
                }
                resultado.add(a);
            }
        }
        return resultado;
    }

    public static void salvar(Path ficheiro, ArrayList<Album> albuns) throws IOException {
        Files.createDirectories(ficheiro.getParent());
        try (var w = Files.newBufferedWriter(ficheiro, StandardCharsets.UTF_8)) {
            for (Album a : albuns) {
                StringBuilder sb = new StringBuilder();
                sb.append(a.getId()).append(SEP_CAMPO)
                  .append(a.getTitulo()).append(SEP_CAMPO)
                  .append(a.getAutor()).append(SEP_CAMPO);
                boolean primeira = true;
                for (var f : a.getFaixas()) {
                    if (!primeira) sb.append(SEP_FAIXA);
                    sb.append(f.getNumero()).append(SEP_INT)
                      .append(f.getDuracaoSegundos()).append(SEP_INT)
                      .append(f.getFicheiroMp3Path() == null ? "" : f.getFicheiroMp3Path());
                    primeira = false;
                }
                w.write(sb.toString());
                w.newLine();
            }
        }
    }
} 
