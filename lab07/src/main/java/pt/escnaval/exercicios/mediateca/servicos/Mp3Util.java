package pt.escnaval.exercicios.mediateca.servicos;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class Mp3Util {
    public static void copy(Path origem, Path destino) throws IOException {
        Files.createDirectories(destino.getParent());
        try (InputStream in = Files.newInputStream(origem);
             OutputStream out = Files.newOutputStream(destino)) {
            byte[] buf = new byte[8192];
            int n;
            while ((n = in.read(buf)) != -1) {
                out.write(buf, 0, n);
            }
        }
    }
} 
