package pt.escnaval.exercicios;

public class DemoIdentidade {
    public static void main(String[] args) {
        System.out.println("=== Demonstração de Identidade vs. Valor ===\n");

        // Teste 1: Identidade de Cliente (por id)
        System.out.println("--- Teste 1: Identidade de Cliente ---");
        Endereco end1 = new Endereco("Rua A", "Lisboa", "1000-001");
        Endereco end2 = new Endereco("Rua B", "Porto", "4000-001");
        
    Cliente c1a = new Cliente(1, "Ana Silva", "ana@example.com", end1, "pass");
    Cliente c1b = new Cliente(1, "Ana Silva", "ana@example.com", end2, "pass"); // mesmo id, endereço diferente
    Cliente c2 = new Cliente(2, "Bruno Costa", "bruno@example.com", end1, "pass");
        
        System.out.println("c1a.equals(c1b)? " + c1a.equals(c1b) + " (esperado: true - mesmo id)");
        System.out.println("c1a.equals(c2)? " + c1a.equals(c2) + " (esperado: false - ids diferentes)");
        System.out.println("c1a.hashCode() == c1b.hashCode()? " + (c1a.hashCode() == c1b.hashCode()));

        // Teste 2: Valor de Endereco
        System.out.println("\n--- Teste 2: Valor de Endereco ---");
        Endereco endA1 = new Endereco("Rua das Flores", "Lisboa", "1000-001");
        Endereco endA2 = new Endereco("Rua das Flores", "Lisboa", "1000-001");
        Endereco endB = new Endereco("Rua das Rosas", "Lisboa", "1000-002");
        
        System.out.println("endA1.equals(endA2)? " + endA1.equals(endA2) + " (esperado: true - mesmo valor)");
        System.out.println("endA1.equals(endB)? " + endA1.equals(endB) + " (esperado: false - valores diferentes)");
        System.out.println("endA1.hashCode() == endA2.hashCode()? " + (endA1.hashCode() == endA2.hashCode()));

        // Teste 3: Identidade de Conta (por IBAN)
        System.out.println("\n--- Teste 3: Identidade de Conta ---");
        Iban iban1 = new Iban("PT50000000000000000000000");
        Iban iban2 = new Iban("PT60000000000000000000000");
        
        Conta conta1a = new Conta(iban1, c1a, 100.0);
        Conta conta1b = new Conta(iban1, c2, 200.0); // mesmo IBAN, titular e saldo diferentes
        Conta conta2 = new Conta(iban2, c1a, 100.0);
        
        System.out.println("conta1a.equals(conta1b)? " + conta1a.equals(conta1b) + " (esperado: true - mesmo IBAN)");
        System.out.println("conta1a.equals(conta2)? " + conta1a.equals(conta2) + " (esperado: false - IBANs diferentes)");
        System.out.println("conta1a.hashCode() == conta1b.hashCode()? " + (conta1a.hashCode() == conta1b.hashCode()));

        // Teste 4: Saldo não afeta equals
        System.out.println("\n--- Teste 4: Mudança de saldo não afeta identidade ---");
        double saldoAntes = conta1a.getSaldo();
        int hashAntes = conta1a.hashCode();
        conta1a.depositar(50.0);
        System.out.println("Saldo antes: " + saldoAntes + ", depois: " + conta1a.getSaldo());
        System.out.println("HashCode mudou? " + (hashAntes != conta1a.hashCode()) + " (esperado: false)");
        System.out.println("conta1a.equals(conta1b)? " + conta1a.equals(conta1b) + " (esperado: true - IBAN não mudou)");

        // Teste 5: Simulação de conjunto (array sem duplicados)
        System.out.println("\n--- Teste 5: Conjunto simulado com array ---");
        Conta[] conjunto = new Conta[10];
        int tamanho = 0;
        
        // Método auxiliar para adicionar sem duplicados
        boolean adicionado1 = adicionarSemDuplicados(conjunto, tamanho, conta1a);
        if (adicionado1) tamanho++;
        
        boolean adicionado2 = adicionarSemDuplicados(conjunto, tamanho, conta1b); // duplicado
        if (adicionado2) tamanho++;
        
        boolean adicionado3 = adicionarSemDuplicados(conjunto, tamanho, conta2);
        if (adicionado3) tamanho++;
        
        System.out.println("Tentou adicionar 3 contas (2 com mesmo IBAN)");
        System.out.println("Tamanho do conjunto: " + tamanho + " (esperado: 2)");
        System.out.println("Contas no conjunto:");
        for (int i = 0; i < tamanho; i++) {
            System.out.println("  " + conjunto[i].getIban());
        }
    }

    private static boolean adicionarSemDuplicados(Conta[] array, int tamanho, Conta novaConta) {
        // Verifica se já existe
        for (int i = 0; i < tamanho; i++) {
            if (array[i].equals(novaConta)) {
                System.out.println("  Rejeitado (duplicado): " + novaConta.getIban());
                return false;
            }
        }
        // Adiciona
        if (tamanho < array.length) {
            array[tamanho] = novaConta;
            System.out.println("  Adicionado: " + novaConta.getIban());
            return true;
        }
        return false;
    }
} 
