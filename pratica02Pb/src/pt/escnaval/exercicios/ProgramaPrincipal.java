package pt.escnaval.exercicios;

public class ProgramaPrincipal {
    public static void main(String[] args) {
        Conta[] contas = new Conta[10];
        for (int i = 0; i < 10; i++) {
            int j = i + 1;
            Cliente c = new Cliente(j, "Nome " + j, j / 100.0);
            Endereco e = new Endereco("Rua " + j, j + 10, j * 1000 + j + " Localidade " + j);
            contas[i] = new Conta(j, c, j * 1000.0, e);
        }
        for (Conta c : contas) {
            c.mostrar();
            c.getCliente().mostrar();
            c.mostrarEndereco();
            System.out.println();
        }
    }
}
