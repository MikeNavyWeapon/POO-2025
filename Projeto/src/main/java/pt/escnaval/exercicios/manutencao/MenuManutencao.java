package pt.escnaval.exercicios.manutencao;

import pt.escnaval.exercicios.manutencao.ui.ConsoleUi;
import pt.escnaval.exercicios.manutencao.ui.FxUiLauncher;
import pt.escnaval.exercicios.manutencao.ui.UiApp;

public class MenuManutencao {
    public static void main(String[] args) {
        UiApp ui = usarJavaFx(args) ? new FxUiLauncher(args) : new ConsoleUi();
        ui.start();
    }

    private static boolean usarJavaFx(String[] args) {
        if (args == null) return false;
        for (String a : args) {
            if ("--fx".equalsIgnoreCase(a)) return true;
        }
        return false;
    }
}
