package pt.escnaval.exercicios;

public class DemoHerancaInterface {
    public static void main(String[] args) {
        System.out.println("=== Demonstração de Herança e Interfaces ===\n");

        // Preparar dados
        Endereco end1 = new Endereco("Rua A", "Lisboa", "1000-001");
        Cliente c1 = new Cliente(1, "Ana Silva", "ana@example.com", end1, "senha123");
        Cliente c2 = new Cliente(2, "Bruno Costa", "bruno@example.com", end1, "pass456");

        // Teste de Interface Autenticavel
        System.out.println("--- Teste 1: Interface Autenticavel ---");
        System.out.println("Cliente 1 identificador: " + c1.getIdentificador());
        System.out.println("Autenticar com senha correta: " + c1.autenticar("senha123"));
        System.out.println("Autenticar com senha errada: " + c1.autenticar("errada"));
        
        // Polimorfismo com interface
        Autenticavel auth = c2; // Cliente É-UM Autenticavel
        System.out.println("Via interface - identificador: " + auth.getIdentificador());
        System.out.println("Via interface - autenticar: " + auth.autenticar("pass456"));

        // Teste de Herança
        System.out.println("\n--- Teste 2: Herança - Polimorfismo ---");
        
        Iban iban1 = new Iban("PT50000000000000000000001");
        Iban iban2 = new Iban("PT50000000000000000000002");
        Iban iban3 = new Iban("PT50000000000000000000003");
        
        // Array polimórfico: Conta é tipo base
        Conta[] contas = new Conta[3];
        contas[0] = new Conta(iban1, c1, 100.0);
        contas[1] = new ContaOrdem(iban2, c2, 50.0, 500.0);
        contas[2] = new ContaPoupanca(iban3, c1, 1000.0, 3.5);
        
        System.out.println("Contas criadas (polimorfismo):");
        for (int i = 0; i < contas.length; i++) {
            System.out.println(contas[i]); // polimorfismo: toString() específico
        }
        
        // Operações comuns (interface comum)
        System.out.println("\n--- Teste 3: Operações comuns ---");
        System.out.println("Depositar 50 em todas:");
        for (Conta c : contas) {
            c.depositar(50.0);
            System.out.println("  " + c.getIban() + " -> saldo: " + c.getSaldo());
        }
        
        // Operações específicas (requer casting)
        System.out.println("\n--- Teste 4: Operações específicas (casting) ---");
        
        // ContaOrdem: testar descoberto
        if (contas[1] instanceof ContaOrdem) {
            ContaOrdem co = (ContaOrdem) contas[1];
            System.out.println("ContaOrdem - limite descoberto: " + co.getLimiteDescoberto());
            System.out.println("Tentar levantar 600 (saldo=100, limite=500):");
            boolean sucesso = co.levantar(600);
            System.out.println("  Sucesso? " + sucesso + " (esperado: true)");
            System.out.println("  Saldo após: " + co.getSaldo());
            
            System.out.println("Tentar levantar mais 100 (ultrapassaria limite):");
            sucesso = co.levantar(100);
            System.out.println("  Sucesso? " + sucesso + " (esperado: false)");
        }
        
        // ContaPoupanca: testar juros
        if (contas[2] instanceof ContaPoupanca) {
            ContaPoupanca cp = (ContaPoupanca) contas[2];
            System.out.println("\nContaPoupanca - taxa: " + cp.getTaxaJuroAnual() + "%");
            System.out.println("Saldo antes: " + cp.getSaldo());
            double juros = cp.calcularJuros();
            System.out.println("Juros calculados: " + juros);
            cp.aplicarJuros();
            System.out.println("Saldo após aplicar juros: " + cp.getSaldo());
        }
        
        // Verificar identidade (equals baseado em IBAN)
        System.out.println("\n--- Teste 5: Identidade preservada em hierarquia ---");
        Conta c1_ref = contas[0];
        Conta c1_novo = new Conta(iban1, c2, 999.0); // mesmo IBAN
        System.out.println("c1_ref.equals(c1_novo)? " + c1_ref.equals(c1_novo) + " (esperado: true)");
        
        ContaOrdem co_novo = new ContaOrdem(iban2, c1, 0, 1000);
        System.out.println("contas[1].equals(co_novo)? " + contas[1].equals(co_novo) + " (esperado: true)");
        
        System.out.println("\n--- Resumo ---");
        System.out.println("✓ Interfaces definem contratos (Autenticavel)");
        System.out.println("✓ Herança representa relação É-UM (ContaOrdem É-UMA Conta)");
        System.out.println("✓ Polimorfismo permite tratar subclasses como tipo base");
        System.out.println("✓ Casting permite acesso a métodos específicos");
        System.out.println("✓ equals/hashCode baseados na classe base funcionam em toda hierarquia");
    }
} 
