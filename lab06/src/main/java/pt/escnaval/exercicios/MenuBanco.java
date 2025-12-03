package pt.escnaval.exercicios;

public class MenuBanco {
    private static final Banco banco = new Banco();

    /**
     * Método principal - ponto de entrada do programa.
     * 
     * CONCEITO: TRATAMENTO DE EXCEÇÕES EM CAMADAS
     * - Exceções checked capturadas no switch
     * - Exceções unchecked capturadas no loop externo
     * - Feedback diferenciado por tipo
     */
    public static void main(String[] args) {
        // Garante ponto decimal (não vírgula) em printf
        Locale ptPT = Locale.forLanguageTag("pt-PT");
        Locale.setDefault(ptPT);

        // try-with-resources: Scanner fechado automaticamente
        try (Scanner sc = new Scanner(System.in)) {
            int op;
            do {
                mostrarMenu();
                op = UtilsIO.lerOpcao(sc, 0, 8);

                try {
                    // Cada operação pode lançar exceções
                    switch (op) {
                        case 1 -> listarContas();
                        case 2 -> criarContaCorrente(sc);
                        case 3 -> criarContaPoupanca(sc);
                        case 4 -> depositar(sc);
                        case 5 -> levantar(sc);
                        case 6 -> consultarSaldo(sc);
                        case 7 -> removerConta(sc);
                        case 8 -> mostrarEstatisticas();
                        case 0 -> System.out.println("A terminar...");
                    }
                } catch (SaldoInsuficienteException e) {
                    // TRATAMENTO ESPECÍFICO: saldo insuficiente
                    // Mostra informação adicional (saldo disponível)
                    System.err.println("ERRO: " + e.getMessage());
                    System.err.printf("  (saldo disponível: %.2f€)%n", e.getSaldoAtual());
                } catch (ContaBancariaException e) {
                    // TRATAMENTO GENÉRICO: outras exceções checked
                    System.err.println("ERRO: " + e.getMessage());
                } catch (RuntimeException e) {
                    // TRATAMENTO INESPERADO: exceções unchecked
                    // Normalmente indica bug no código
                    System.err.println("ERRO INESPERADO: " + e.getMessage());
                    e.printStackTrace(); // Debug: mostra stack trace
                }

                System.out.println();
            } while (op != 0);
        }
    }

    /**
     * Mostra o menu de opções.
     */
    static void mostrarMenu() {
        System.out.println("═══════════════════════════════════");
        System.out.println("      SISTEMA BANCÁRIO - MENU      ");
        System.out.println("═══════════════════════════════════");
        System.out.println("1) Listar contas");
        System.out.println("2) Criar Conta Corrente");
        System.out.println("3) Criar Conta Poupança");
        System.out.println("4) Depositar");
        System.out.println("5) Levantar");
        System.out.println("6) Consultar saldo");
        System.out.println("7) Remover conta");
        System.out.println("8) Estatísticas");
        System.out.println("0) Sair");
        System.out.println("═══════════════════════════════════");
        System.out.print("Opção → ");
    }

    /**
     * Lista todas as contas (polimorfismo).
     */
    static void listarContas() {
        banco.listarContas();
    }

    /**
     * Cria uma conta corrente.
     * 
     * CONCEITO: EXCEÇÕES UNCHECKED
     * - Pode lançar ContaInvalidaException (construtor)
     * - Não precisa try-catch aqui (propagada para main)
     * - Indica erro de validação (número inválido, etc.)
     */
    static void criarContaCorrente(Scanner sc) {
        System.out.println("\n--- CRIAR CONTA CORRENTE ---");
        String numero = UtilsIO.lerStringNaoVazia(sc, "Número da conta (PTXX-YYYY-ZZZZZZZZ): ");
        String titular = UtilsIO.lerStringNaoVazia(sc, "Titular: ");
        double saldoInicial = UtilsIO.lerDouble(sc, "Saldo inicial: ");
        double limiteDescoberto = UtilsIO.lerDouble(sc, "Limite de descoberto: ");

        // Pode lançar ContaInvalidaException (unchecked) - propagada automaticamente
        ContaCorrente conta = new ContaCorrente(numero, titular, saldoInicial, limiteDescoberto);
        banco.adicionarConta(conta);
        System.out.println("✓ Conta corrente criada com sucesso!");
    }

    /**
     * Cria uma conta poupança.
     * 
     * CONCEITO: POLIMORFISMO
     * - Cria ContaPoupanca mas guarda como ContaBancaria
     * - Tipo específico só interessa no momento da criação
     */
    static void criarContaPoupanca(Scanner sc) {
        System.out.println("\n--- CRIAR CONTA POUPANÇA ---");
        String numero = UtilsIO.lerStringNaoVazia(sc, "Número da conta (PTXX-YYYY-ZZZZZZZZ): ");
        String titular = UtilsIO.lerStringNaoVazia(sc, "Titular: ");
        double saldoInicial = UtilsIO.lerDouble(sc, "Saldo inicial: ");

        ContaPoupanca conta = new ContaPoupanca(numero, titular, saldoInicial);
        banco.adicionarConta(conta);
        System.out.println("✓ Conta poupança criada com sucesso!");
    }

    /**
     * Realiza um depósito.
     * 
     * CONCEITO: EXCEÇÕES CHECKED
     * - depositar() declara throws ContaBancariaException
     * - DEVEMOS declarar throws ou capturar
     * - Aqui propagamos para o main
     * 
     * CONCEITO: VERIFICAÇÃO DE NULL
     * - buscarContaPorNumero retorna null se não encontrar
     * - Verificamos null antes de usar
     */
    static void depositar(Scanner sc) throws ContaBancariaException {
        System.out.println("\n--- DEPOSITAR ---");
        String numero = UtilsIO.lerStringNaoVazia(sc, "Número da conta: ");
        ContaBancaria conta = banco.buscarContaPorNumero(numero);

        if (conta == null) {
            System.out.println("✗ Conta não encontrada.");
            return;
        }

        double montante = UtilsIO.lerDouble(sc, "Montante a depositar: ");

        // depositar() pode lançar ContaBancariaException (checked) - propagamos
        conta.depositar(montante);
        System.out.printf("✓ Depósito de %.2f€ realizado. Novo saldo: %.2f€%n",
                montante, conta.getSaldo());
    }

    /**
     * Realiza um levantamento.
     * 
     * CONCEITO: POLIMORFISMO
     * - levantar() comporta-se diferente conforme o tipo:
     * * ContaCorrente: permite descoberto
     * * ContaPoupanca: sem descoberto, com limite
     * - Método main captura exceções específicas
     * 
     * CONCEITO: EXCEÇÕES CHECKED
     * - levantar() pode lançar várias exceções checked
     * - SaldoInsuficienteException, ContaBancariaException
     * - Propagamos para tratamento específico no main
     */
    static void levantar(Scanner sc) throws ContaBancariaException {
        System.out.println("\n--- LEVANTAR ---");
        String numero = UtilsIO.lerStringNaoVazia(sc, "Número da conta: ");
        ContaBancaria conta = banco.buscarContaPorNumero(numero);

        if (conta == null) {
            System.out.println("✗ Conta não encontrada.");
            return;
        }

        double montante = UtilsIO.lerDouble(sc, "Montante a levantar: ");

        // levantar() pode lançar SaldoInsuficienteException,
        // OperacaoNaoPermitidaException
        // Todas são capturadas no main com tratamento diferenciado
        conta.levantar(montante);
        System.out.printf("✓ Levantamento de %.2f€ realizado. Novo saldo: %.2f€%n",
                montante, conta.getSaldo());
    }

    /**
     * Consulta o saldo de uma conta.
     */
    static void consultarSaldo(Scanner sc) {
        System.out.println("\n--- CONSULTAR SALDO ---");
        String numero = UtilsIO.lerStringNaoVazia(sc, "Número da conta: ");
        ContaBancaria conta = banco.buscarContaPorNumero(numero);

        if (conta == null) {
            System.out.println("✗ Conta não encontrada.");
            return;
        }

        System.out.printf("Saldo atual: %.2f€%n", conta.getSaldo());
        System.out.println("Detalhes: " + conta);
    }

    /**
     * Remove uma conta.
     */
    static void removerConta(Scanner sc) {
        System.out.println("\n--- REMOVER CONTA ---");
        String numero = UtilsIO.lerStringNaoVazia(sc, "Número da conta a remover: ");
        boolean removida = banco.removerConta(numero);
        System.out.println(removida ? "✓ Conta removida." : "✗ Conta não encontrada.");
    }

    /**
     * Mostra estatísticas do banco.
     * 
     * CONCEITO: POLIMORFISMO
     * - Métodos do Banco trabalham com ContaBancaria (interface)
     * - Funcionam para qualquer tipo de conta
     */
    static void mostrarEstatisticas() {
        System.out.println("\n=== ESTATÍSTICAS ===");
        System.out.printf("Total de contas: %d%n", banco.getNumeroContas());
        System.out.printf("Saldo total: %.2f€%n", banco.saldoTotal());
        System.out.println();
        banco.listarContasComDescoberto();
    }
}

