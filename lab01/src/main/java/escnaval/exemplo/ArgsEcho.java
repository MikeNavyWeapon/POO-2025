package escnaval.exemplo;

public class ArgsEcho {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Uso: java escnaval.exemplo.ArgsEcho <palavras...>");
            return;
        }
        System.out.println("Recebi " + args.length + " argumento(s):");
        for (int i = 0; i < args.length; i++) {
            System.out.println("  [" + i + "] " + args[i]);
        }
    }
}