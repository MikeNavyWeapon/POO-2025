package pt.escnaval.exercicios.manutencao.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public final class AppLogger {
    private static final Logger LOGGER = Logger.getLogger("Manutencao");

    static {
        try {
            Path logsDir = Path.of("logs");
            Files.createDirectories(logsDir);
            FileHandler fh = new FileHandler(logsDir.resolve("app.log").toString(), true);
            fh.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fh);
            LOGGER.setUseParentHandlers(false);
            LOGGER.setLevel(Level.INFO);
        } catch (IOException e) {
            // Avoid crashing on logger setup; fallback to default.
            LOGGER.log(Level.WARNING, "Falha ao iniciar logger", e);
        }
    }

    private AppLogger() {}

    public static Logger get() { return LOGGER; }
}
