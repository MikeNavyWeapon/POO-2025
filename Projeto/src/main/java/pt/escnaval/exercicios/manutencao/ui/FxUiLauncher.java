package pt.escnaval.exercicios.manutencao.ui;

import java.lang.reflect.Method;

public class FxUiLauncher implements UiApp {
    private final String[] args;

    public FxUiLauncher(String[] args) {
        this.args = args == null ? new String[0] : args.clone();
    }

    @Override
    public void start() {
        try {
            Class<?> appClass = Class.forName("pt.escnaval.exercicios.manutencao.ui.FxUiApp");
            Class<?> applicationClass = Class.forName("javafx.application.Application");
            Method launch = applicationClass.getMethod("launch", Class.class, String[].class);
            launch.invoke(null, appClass, args);
        } catch (Throwable t) {
            System.out.println("JavaFX preparado, mas nao ativado.");
        }
    }
}
