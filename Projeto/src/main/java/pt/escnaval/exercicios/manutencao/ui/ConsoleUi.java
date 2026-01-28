package pt.escnaval.exercicios.manutencao.ui;

import pt.escnaval.exercicios.manutencao.modelo.*;
import pt.escnaval.exercicios.manutencao.servicos.ServicoManutencao;
import pt.escnaval.exercicios.manutencao.utils.UtilsIO;

import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ConsoleUi implements UiApp {
    private final ServicoManutencao serv = new ServicoManutencao();
    private Scanner sc;

    @Override
    public void start() {
        try {
            serv.carregarTudo();
            System.out.println("Dados carregados.");
        } catch (Exception e) {
            System.out.println("Aviso: nao foi possivel carregar dados: " + e.getMessage());
        }
        sc = new Scanner(System.in);
        if (!login()) return;

        while (true) {
            mostrarMenu();
            int op = UtilsIO.lerInt(sc, "Opção -> ");
            switch (op) {
                case 1 -> dashboard();
                case 2 -> menuAtivos();
                case 3 -> menuOTs();
                case 4 -> menuPreventiva();
                case 5 -> menuInventario();
                case 6 -> menuPedidos();
                case 7 -> menuUtilizadores();
                case 8 -> menuFornecedores();
                case 9 -> menuParametros();
                case 10 -> menuAuditoria();
                case 11 -> menuRelatorios();
                case 12 -> menuImportExport();
                case 13 -> menuNotificacoes();
                case 14 -> menuPrivacidade();
                case 15 -> guardar();
                case 0 -> { System.out.println("A terminar..."); return; }
                default -> System.out.println("Opção inválida");
            }
        }
    }

    private boolean login() {
        for (int i = 0; i < 3; i++) {
            String id = UtilsIO.lerStringNaoVazia(sc, "Utilizador: ");
            String senha = UtilsIO.lerStringNaoVazia(sc, "Senha: ");
            Utilizador u = serv.autenticar(id, senha);
            if (u != null) {
                System.out.println("Bem-vindo " + u.getNome() + " (" + u.getPerfil() + ")");
                return true;
            }
            System.out.println("Credenciais invalidas.");
        }
        return false;
    }

    private void mostrarMenu() {
        System.out.println();
        System.out.println("=== GESTÃO DE MANUTENÇÃO ===");
        System.out.println("1) Dashboard");
        System.out.println("2) Ativos");
        System.out.println("3) Ordens de Trabalho");
        System.out.println("4) Preventiva");
        System.out.println("5) Inventario");
        System.out.println("6) Pedidos");
        System.out.println("7) Utilizadores");
        System.out.println("8) Fornecedores");
        System.out.println("9) Parametros");
        System.out.println("10) Auditoria");
        System.out.println("11) Relatorios");
        System.out.println("12) Importar/Exportar");
        System.out.println("13) Notificacoes");
        System.out.println("14) Privacidade/Retencao");
        System.out.println("15) Guardar dados");
        System.out.println("0) Sair");
    }

    private void dashboard() {
        System.out.println("--- Dashboard ---");
        Utilizador atual = serv.getUtilizadorAtual();
        var cfg = serv.obterDashboardPorPerfil(atual.getPerfil());
        String widgets = cfg == null ? "" : cfg.getWidgets();
        for (String w : widgets.split(",")) {
            String widget = w.trim().toUpperCase();
            if (widget.isBlank()) continue;
            switch (widget) {
                case "ATIVOS" -> System.out.println("Ativos: " + serv.listarAtivos().size());
                case "BACKLOG" -> System.out.println("OTs abertas: " + serv.backlogTotal());
                case "MTTR" -> System.out.printf("MTTR (min): %.2f%n", serv.calcularMTTRMin());
                case "MTBF" -> System.out.printf("MTBF (h): %.2f%n", serv.calcularMTBFHoras());
                case "STOCK" -> System.out.println("Pecas: " + serv.listarPecas().size());
                case "PLANOS" -> System.out.println("Planos preventivos: " + serv.listarPlanos().size());
                case "PEDIDOS" -> System.out.println("Pedidos: " + serv.listarPedidos().size());
                case "NOTIFICACOES" -> System.out.println("Notificacoes novas: " + serv.listarNotificacoes().size());
                case "MINHAS_OT" -> System.out.println("OTs atribuídas: " + serv.listarOrdemTrabalho().stream()
                        .filter(o -> atual.getId().equalsIgnoreCase(o.getIdTecnico())).count());
                default -> System.out.println("Widget desconhecido: " + widget);
            }
        }
        Map<Integer, Long> backlog = serv.backlogPorCriticidade();
        if (!backlog.isEmpty()) {
            System.out.println("Lista por criticidade: " + backlog);
        }
    }

    private void menuAtivos() {
        while (true) {
            System.out.println("--- Ativos ---");
            System.out.println("1) Listar");
            System.out.println("2) Adicionar");
            System.out.println("3) Editar");
            System.out.println("4) Arquivar");
            System.out.println("5) Pesquisar");
            System.out.println("6) Criar localizacao");
            System.out.println("7) Atualizar contador");
            System.out.println("8) Vistas de pesquisa");
            System.out.println("9) Simular API contadores");
            System.out.println("0) Voltar");
            int op = UtilsIO.lerInt(sc, "Opcao -> ");
            switch (op) {
                case 1 -> listarAtivos();
                case 2 -> adicionarAtivo();
                case 3 -> editarAtivo();
                case 4 -> arquivarAtivo();
                case 5 -> pesquisarAtivos();
                case 6 -> adicionarLocalizacao();
                case 7 -> atualizarContador();
                case 8 -> menuVistas("ATIVOS");
                case 9 -> simularApiContadores();
                case 0 -> { return; }
                default -> System.out.println("Opção inválida");
            }
        }
    }

    private void adicionarAtivo() {
        String codigo = UtilsIO.lerStringNaoVazia(sc, "Código: ");
        String nome = UtilsIO.lerStringNaoVazia(sc, "Nome: ");
        int criticidade = UtilsIO.lerInt(sc, "Criticidade (1-5): ", 1, 5);
        String pai = UtilsIO.lerStringOpcional(sc, "ID ativo pai (enter para nenhum): ");
        String loc = UtilsIO.lerStringOpcional(sc, "ID localizacao (enter para nenhum): ");
        try {
            serv.adicionarAtivo(codigo, nome, criticidade, pai, loc);
            System.out.println("Ativo criado.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void editarAtivo() {
        String codigo = UtilsIO.lerStringNaoVazia(sc, "Código do ativo: ");
        Ativo a = serv.buscarAtivo(codigo);
        if (a == null) { System.out.println("Ativo não encontrado."); return; }
        String nome = UtilsIO.lerStringOpcional(sc, "Novo nome (enter para manter): ");
        String estadoTxt = UtilsIO.lerStringOpcional(sc, "Estado (ATIVO/INATIVO/OBSOLETO/ARQUIVADO, enter para manter): ");
        EstadoAtivo estado = null;
        if (estadoTxt != null) {
            try { estado = EstadoAtivo.valueOf(estadoTxt.trim().toUpperCase()); }
            catch (IllegalArgumentException e) { System.out.println("Estado inválido."); }
        }
        String critTxt = UtilsIO.lerStringOpcional(sc, "Criticidade (1-5, enter para manter): ");
        Integer crit = null;
        if (critTxt != null) {
            try { crit = Integer.parseInt(critTxt); }
            catch (NumberFormatException e) { System.out.println("Criticidade inválida."); }
        }
        String pai = UtilsIO.lerStringOpcional(sc, "ID ativo pai (enter para manter): ");
        String loc = UtilsIO.lerStringOpcional(sc, "ID localização (enter para manter): ");
        try {
            serv.atualizarAtivo(codigo, nome, estado, crit, pai, loc);
            System.out.println("Ativo atualizado.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void arquivarAtivo() {
        String codigo = UtilsIO.lerStringNaoVazia(sc, "Código do ativo: ");
        try {
            serv.arquivarAtivo(codigo);
            System.out.println("Ativo arquivado.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void pesquisarAtivos() {
        String termo = UtilsIO.lerStringNaoVazia(sc, "Termo: ");
        List<Ativo> res = serv.pesquisarAtivos(termo);
        if (res.isEmpty()) System.out.println("(sem resultados)");
        else res.forEach(System.out::println);
    }

    private void listarAtivos() {
        String filtro = UtilsIO.lerStringOpcional(sc, "Filtro (enter para todos): ");
        String ordenar = UtilsIO.lerStringOpcional(sc, "Ordenar por (codigo/nome/criticidade): ");
        List<Ativo> lista = filtro == null ? serv.listarAtivos() : serv.pesquisarAtivos(filtro);
        if (ordenar != null) {
            switch (ordenar.toLowerCase()) {
                case "nome" -> lista.sort(java.util.Comparator.comparing(Ativo::getNome));
                case "criticidade" -> lista.sort(java.util.Comparator.comparingInt(Ativo::getCriticidade).reversed());
                default -> lista.sort(java.util.Comparator.comparing(Ativo::getCodigo));
            }
        }
        listarPaginado(lista, tamanhoPagina());
        if (UtilsIO.lerSimNao(sc, "Guardar esta vista")) {
            serv.guardarVista("ATIVOS", filtro, ordenar, tamanhoPagina());
        }
    }

    private void adicionarLocalizacao() {
        String nome = UtilsIO.lerStringNaoVazia(sc, "Nome localizacao: ");
        String pai = UtilsIO.lerStringOpcional(sc, "ID pai (enter para nenhum): ");
        serv.adicionarLocalizacao(nome, pai);
        System.out.println("Localização criada.");
    }

    private void atualizarContador() {
        String ativo = UtilsIO.lerStringNaoVazia(sc, "Código do ativo: ");
        Contador.Tipo tipo = UtilsIO.lerEnum(sc, "Tipo", Contador.Tipo.class);
        double leitura = UtilsIO.lerDouble(sc, "Leitura atual: ");
        String unidade = UtilsIO.lerStringNaoVazia(sc, "Unidade: ");
        serv.atualizarContador(ativo, tipo, leitura, unidade);
        System.out.println("Contador atualizado.");
    }

    private void simularApiContadores() {
        int n = UtilsIO.lerInt(sc, "Número de atualizações: ");
        serv.simularApiContadores(n);
        System.out.println("Simulação concluída.");
    }

    private void menuOTs() {
        while (true) {
            System.out.println("--- Ordens de Trabalho ---");
            System.out.println("1) Listar");
            System.out.println("2) Criar OT (wizard)");
            System.out.println("3) Criar OT rapida");
            System.out.println("4) Alterar estado");
            System.out.println("5) Atribuir tecnico");
            System.out.println("6) Adicionar tarefa");
            System.out.println("7) Registar execucao");
            System.out.println("8) Adicionar consumo de peca");
            System.out.println("9) Anexar documento");
            System.out.println("10) Pesquisar OT");
            System.out.println("11) Vistas de pesquisa");
            System.out.println("0) Voltar");
            int op = UtilsIO.lerInt(sc, "Opcao -> ");
            switch (op) {
                case 1 -> listarOTs();
                case 2 -> criarOTWizard();
                case 3 -> criarOT();
                case 4 -> alterarEstadoOT();
                case 5 -> atribuirTecnico();
                case 6 -> adicionarTarefa();
                case 7 -> registarExecucao();
                case 8 -> adicionarConsumo();
                case 9 -> anexarDocumento();
                case 10 -> pesquisarOT();
                case 11 -> menuVistas("OTS");
                case 0 -> { return; }
                default -> System.out.println("Opção inválida");
            }
        }
    }

    private void criarOT() {
        String ativo = UtilsIO.lerStringNaoVazia(sc, "Código do ativo: ");
        String desc = UtilsIO.lerStringNaoVazia(sc, "Descrição: ");
        int prioridade = UtilsIO.lerInt(sc, "Prioridade (1-5, 0 auto): ", 0, 5);
        OrdemTrabalho.Tipo tipo = UtilsIO.lerEnum(sc, "Tipo", OrdemTrabalho.Tipo.class);
        String categoria = UtilsIO.lerStringOpcional(sc, "Categoria falha (opcional): ");
        String centro = UtilsIO.lerStringOpcional(sc, "Centro custo (opcional): ");
        Instant limite = UtilsIO.lerDataHoraOpcional(sc, "Data limite");
        try {
            serv.criarOrdemTrabalho(tipo, prioridade, desc, ativo, categoria, centro, limite);
            System.out.println("OT criada.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void criarOTWizard() {
        System.out.println("--- Wizard OT ---");
        Utilizador atual = serv.getUtilizadorAtual();
        String ativo = UtilsIO.lerStringNaoVazia(sc, "Código do ativo: ");
        String desc = UtilsIO.lerStringNaoVazia(sc, "Descrição: ");
        OrdemTrabalho.Tipo tipo = UtilsIO.lerEnum(sc, "Tipo", OrdemTrabalho.Tipo.class);
        int prioridade = UtilsIO.lerInt(sc, "Prioridade (1-5, 0 auto): ", 0, 5);
        String categoria = UtilsIO.lerStringOpcional(sc, "Categoria falha (opcional): ");
        String centro = UtilsIO.lerStringOpcional(sc, "Centro custo (opcional): ");
        Instant limite = UtilsIO.lerDataHoraOpcional(sc, "Data limite");
        Integer sla = null;
        if (UtilsIO.lerSimNao(sc, "Definir SLA manual")) {
            sla = UtilsIO.lerInt(sc, "SLA (horas): ");
        }
        String fornecedorId = null;
        String ordemExterna = null;
        Double custoPrevisto = null;
        if (tipo == OrdemTrabalho.Tipo.EXTERNA) {
            fornecedorId = UtilsIO.lerStringNaoVazia(sc, "ID fornecedor: ");
            ordemExterna = UtilsIO.lerStringOpcional(sc, "Ordem externa (opcional): ");
            custoPrevisto = UtilsIO.lerDouble(sc, "Custo previsto: ");
        }

        if (atual.getPerfil() == Utilizador.Perfil.SOLICITANTE) {
            serv.submeterPedido(ativo, desc, atual.getId());
            System.out.println("Pedido submetido para aprovação.");
            return;
        }
        if (atual.getPerfil() == Utilizador.Perfil.TECNICO) {
            if (!UtilsIO.lerSimNao(sc, "Criar pedido para aprovação")) {
                System.out.println("Operação cancelada.");
                return;
            }
            serv.submeterPedido(ativo, desc, atual.getId());
            System.out.println("Pedido submetido para aprovação.");
            return;
        }

        OrdemTrabalho ot;
        try {
            ot = serv.criarOrdemTrabalhoCompleta(tipo, prioridade, desc, ativo, categoria, centro, limite,
                    sla, fornecedorId, ordemExterna, custoPrevisto);
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            return;
        }

        while (UtilsIO.lerSimNao(sc, "Adicionar tarefa")) {
            adicionarTarefaParaOt(ot.getId());
        }
        while (UtilsIO.lerSimNao(sc, "Adicionar consumo de peca")) {
            adicionarConsumoParaOt(ot.getId());
        }
        if (UtilsIO.lerSimNao(sc, "Registar execução agora")) {
            registarExecucaoParaOt(ot.getId());
        }
        System.out.println("Wizard concluído. OT: " + ot.getId());
    }

    private void alterarEstadoOT() {
        String otId = UtilsIO.lerStringNaoVazia(sc, "ID OT: ");
        OrdemTrabalho.Estado estado = UtilsIO.lerEnum(sc, "Novo estado", OrdemTrabalho.Estado.class);
        try {
            serv.alterarEstadoOT(otId, estado, serv.getUtilizadorAtual().getId());
            System.out.println("Estado atualizado.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void atribuirTecnico() {
        String otId = UtilsIO.lerStringNaoVazia(sc, "ID OT: ");
        String tecnico = UtilsIO.lerStringNaoVazia(sc, "ID técnico: ");
        Instant inicio = UtilsIO.lerDataHoraOpcional(sc, "Início planeado");
        try {
            serv.atribuirTecnico(otId, tecnico, inicio);
            System.out.println("Técnico atribuído.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void adicionarTarefa() {
        String otId = UtilsIO.lerStringNaoVazia(sc, "ID OT: ");
        String desc = UtilsIO.lerStringNaoVazia(sc, "Descrição: ");
        int dur = UtilsIO.lerInt(sc, "Duração planeada (min): ");
        try {
            serv.adicionarTarefa(otId, desc, dur);
            System.out.println("Tarefa adicionada.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void adicionarTarefaParaOt(String otId) {
        String desc = UtilsIO.lerStringNaoVazia(sc, "Descrição: ");
        int dur = UtilsIO.lerInt(sc, "Duração planeada (min): ");
        try {
            serv.adicionarTarefa(otId, desc, dur);
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void registarExecucao() {
        String otId = UtilsIO.lerStringNaoVazia(sc, "ID OT: ");
        Instant inicio = UtilsIO.lerDataHoraOpcional(sc, "Início");
        Instant fim = UtilsIO.lerDataHoraOpcional(sc, "Fim");
        String causa = UtilsIO.lerStringOpcional(sc, "Causa (opcional): ");
        String acao = UtilsIO.lerStringOpcional(sc, "Ação (opcional): ");
        String tecnico = UtilsIO.lerStringOpcional(sc, "Técnico (opcional): ");
        String obs = UtilsIO.lerStringOpcional(sc, "Observações (opcional): ");
        try {
            serv.registarExecucao(otId, inicio, fim, causa, acao, tecnico, obs);
            System.out.println("Execução registada.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void registarExecucaoParaOt(String otId) {
        Instant inicio = UtilsIO.lerDataHoraOpcional(sc, "Início");
        Instant fim = UtilsIO.lerDataHoraOpcional(sc, "Fim");
        String causa = UtilsIO.lerStringOpcional(sc, "Causa (opcional): ");
        String acao = UtilsIO.lerStringOpcional(sc, "Ação (opcional): ");
        String tecnico = UtilsIO.lerStringOpcional(sc, "Técnico (opcional): ");
        String obs = UtilsIO.lerStringOpcional(sc, "Observações (opcional): ");
        try {
            serv.registarExecucao(otId, inicio, fim, causa, acao, tecnico, obs);
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void adicionarConsumo() {
        String otId = UtilsIO.lerStringNaoVazia(sc, "ID OT: ");
        String sku = UtilsIO.lerStringNaoVazia(sc, "SKU: ");
        int q = UtilsIO.lerInt(sc, "Quantidade: ");
        try {
            serv.adicionarConsumoPeca(otId, sku, q);
            System.out.println("Consumo registado.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void adicionarConsumoParaOt(String otId) {
        String sku = UtilsIO.lerStringNaoVazia(sc, "SKU: ");
        int q = UtilsIO.lerInt(sc, "Quantidade: ");
        try {
            serv.adicionarConsumoPeca(otId, sku, q);
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void anexarDocumento() {
        String entidadeId = UtilsIO.lerStringNaoVazia(sc, "ID OT: ");
        String tipo = UtilsIO.lerStringNaoVazia(sc, "Tipo: ");
        String path = UtilsIO.lerStringNaoVazia(sc, "Path: ");
        String meta = UtilsIO.lerStringOpcional(sc, "Meta (opcional): ");
        serv.adicionarDocumento("OT", entidadeId, tipo, path, meta);
        System.out.println("Documento registado.");
    }

    private void pesquisarOT() {
        String termo = UtilsIO.lerStringNaoVazia(sc, "Termo: ");
        List<OrdemTrabalho> res = serv.pesquisarOTs(termo);
        if (res.isEmpty()) System.out.println("(sem resultados)");
        else res.forEach(System.out::println);
    }

    private void listarOTs() {
        String filtro = UtilsIO.lerStringOpcional(sc, "Filtro (enter para todos): ");
        String ordenar = UtilsIO.lerStringOpcional(sc, "Ordenar por (id/prioridade/estado): ");
        List<OrdemTrabalho> lista = filtro == null ? serv.listarOrdemTrabalho() : serv.pesquisarOTs(filtro);
        if (ordenar != null) {
            switch (ordenar.toLowerCase()) {
                case "prioridade" -> lista.sort(java.util.Comparator.comparingInt(OrdemTrabalho::getPrioridade).reversed());
                case "estado" -> lista.sort(java.util.Comparator.comparing(OrdemTrabalho::getEstado));
                default -> lista.sort(java.util.Comparator.comparing(OrdemTrabalho::getId));
            }
        }
        listarPaginado(lista, tamanhoPagina());
        if (UtilsIO.lerSimNao(sc, "Guardar esta vista")) {
            serv.guardarVista("OTS", filtro, ordenar, tamanhoPagina());
        }
    }

    private void menuPreventiva() {
        while (true) {
            System.out.println("--- Preventiva ---");
            System.out.println("1) Listar planos");
            System.out.println("2) Criar plano");
            System.out.println("3) Adicionar gatilho");
            System.out.println("4) Avaliar planos");
            System.out.println("5) Listar gatilhos");
            System.out.println("0) Voltar");
            int op = UtilsIO.lerInt(sc, "Opção -> ");
            switch (op) {
                case 1 -> serv.listarPlanos().forEach(System.out::println);
                case 2 -> criarPlano();
                case 3 -> adicionarGatilho();
                case 4 -> avaliarPlanos();
                case 5 -> serv.listarGatilhos().forEach(System.out::println);
                case 0 -> { return; }
                default -> System.out.println("Opção inválida");
            }
        }
    }

    private void criarPlano() {
        String ativo = UtilsIO.lerStringNaoVazia(sc, "Código ativo: ");
        PlanoPreventivo.Politica politica = UtilsIO.lerEnum(sc, "Política", PlanoPreventivo.Politica.class);
        int periodicidade = UtilsIO.lerInt(sc, "Periodicidade (dias): ");
        int janela = UtilsIO.lerInt(sc, "Janela (dias): ");
        try {
            serv.criarPlanoPreventivo(ativo, politica, periodicidade, janela);
            System.out.println("Plano criado.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void adicionarGatilho() {
        String planoId = UtilsIO.lerStringNaoVazia(sc, "ID plano: ");
        RegraGatilho.Tipo tipo = UtilsIO.lerEnum(sc, "Tipo", RegraGatilho.Tipo.class);
        double valor = UtilsIO.lerDouble(sc, "Valor: ");
        String unidade = UtilsIO.lerStringNaoVazia(sc, "Unidade: ");
        try {
            serv.adicionarGatilho(planoId, tipo, valor, unidade);
            System.out.println("Gatilho criado.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void avaliarPlanos() {
        List<OrdemTrabalho> novas = serv.avaliarPlanos();
        if (novas.isEmpty()) System.out.println("Sem OTs preventivas geradas.");
        else {
            System.out.println("OTs preventivas criadas: " + novas.size());
            novas.forEach(System.out::println);
        }
    }

    private void menuInventario() {
        while (true) {
            System.out.println("--- Inventário ---");
            System.out.println("1) Listar peças");
            System.out.println("2) Adicionar peça");
            System.out.println("3) Movimento de stock");
            System.out.println("4) Ver stock");
            System.out.println("5) Ver movimentos");
            System.out.println("6) Inventario ciclico");
            System.out.println("7) Ver inventários ciclicos");
            System.out.println("0) Voltar");
            int op = UtilsIO.lerInt(sc, "Opção -> ");
            switch (op) {
                case 1 -> listarPecas();
                case 2 -> adicionarPeca();
                case 3 -> movimentoStock();
                case 4 -> serv.listarStock().forEach(System.out::println);
                case 5 -> serv.listarMovimentos().forEach(System.out::println);
                case 6 -> inventarioCiclico();
                case 7 -> serv.listarInventarios().forEach(System.out::println);
                case 0 -> { return; }
                default -> System.out.println("Opção inválida");
            }
        }
    }

    private void adicionarPeca() {
        String sku = UtilsIO.lerStringNaoVazia(sc, "SKU: ");
        String designacao = UtilsIO.lerStringNaoVazia(sc, "Designacao: ");
        String unidade = UtilsIO.lerStringNaoVazia(sc, "Unidade: ");
        int repos = UtilsIO.lerInt(sc, "Ponto reposicao: ");
        double custo = UtilsIO.lerDouble(sc, "Custo unitário: ");
        try {
            serv.adicionarPeca(sku, designacao, unidade, repos, custo);
            System.out.println("Peça adicionada.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void listarPecas() {
        String filtro = UtilsIO.lerStringOpcional(sc, "Filtro (enter para todos): ");
        List<Peca> lista = filtro == null ? serv.listarPecas() : serv.listarPecas().stream()
                .filter(p -> p.getSku().toLowerCase().contains(filtro.toLowerCase())
                        || p.getDesignacao().toLowerCase().contains(filtro.toLowerCase()))
                .toList();
        listarPaginado(lista, tamanhoPagina());
    }

    private void movimentoStock() {
        String sku = UtilsIO.lerStringNaoVazia(sc, "SKU: ");
        String deposito = UtilsIO.lerStringNaoVazia(sc, "Depósito: ");
        MovimentoStock.Tipo tipo = UtilsIO.lerEnum(sc, "Tipo", MovimentoStock.Tipo.class);
        int qtd = UtilsIO.lerInt(sc, "Quantidade: ");
        String otId = UtilsIO.lerStringOpcional(sc, "OT (opcional): ");
        try {
            serv.movimentarStock(sku, deposito, tipo, qtd, otId);
            System.out.println("Movimento registado.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void inventarioCiclico() {
        String sku = UtilsIO.lerStringNaoVazia(sc, "SKU: ");
        String deposito = UtilsIO.lerStringNaoVazia(sc, "Deposito: ");
        int qtd = UtilsIO.lerInt(sc, "Quantidade contada: ");
        try {
            serv.registarInventarioCiclico(sku, deposito, qtd);
            System.out.println("Inventário ciclico registado.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void menuPedidos() {
        while (true) {
            System.out.println("--- Pedidos ---");
            System.out.println("1) Listar");
            System.out.println("2) Submeter");
            System.out.println("3) Aprovar");
            System.out.println("4) Rejeitar");
            System.out.println("5) Converter em OT");
            System.out.println("0) Voltar");
            int op = UtilsIO.lerInt(sc, "Opção -> ");
            switch (op) {
                case 1 -> serv.listarPedidos().forEach(System.out::println);
                case 2 -> submeterPedido();
                case 3 -> aprovarPedido();
                case 4 -> rejeitarPedido();
                case 5 -> converterPedido();
                case 0 -> { return; }
                default -> System.out.println("Opção inválida");
            }
        }
    }

    private void submeterPedido() {
        String ativo = UtilsIO.lerStringNaoVazia(sc, "Código ativo: ");
        String desc = UtilsIO.lerStringNaoVazia(sc, "Descrição: ");
        try {
            serv.submeterPedido(ativo, desc, serv.getUtilizadorAtual().getId());
            System.out.println("Pedido submetido.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void aprovarPedido() {
        if (!requerPerfil(Utilizador.Perfil.GESTOR, Utilizador.Perfil.PLANEADOR)) return;
        String id = UtilsIO.lerStringNaoVazia(sc, "ID pedido: ");
        try {
            serv.aprovarPedido(id, serv.getUtilizadorAtual().getId());
            System.out.println("Pedido aprovado.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void rejeitarPedido() {
        if (!requerPerfil(Utilizador.Perfil.GESTOR, Utilizador.Perfil.PLANEADOR)) return;
        String id = UtilsIO.lerStringNaoVazia(sc, "ID pedido: ");
        try {
            serv.rejeitarPedido(id, serv.getUtilizadorAtual().getId());
            System.out.println("Pedido rejeitado.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void converterPedido() {
        if (!requerPerfil(Utilizador.Perfil.GESTOR, Utilizador.Perfil.PLANEADOR)) return;
        String id = UtilsIO.lerStringNaoVazia(sc, "ID pedido: ");
        try {
            OrdemTrabalho ot = serv.converterPedidoEmOt(id, serv.getUtilizadorAtual().getId());
            System.out.println("OT criada: " + ot.getId());
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void menuUtilizadores() {
        if (!requerPerfil(Utilizador.Perfil.GESTOR)) return;
        while (true) {
            System.out.println("--- Utilizadores ---");
            System.out.println("1) Listar");
            System.out.println("2) Adicionar");
            System.out.println("3) Inativar");
            System.out.println("4) Anonimizar");
            System.out.println("0) Voltar");
            int op = UtilsIO.lerInt(sc, "Opção -> ");
            switch (op) {
                case 1 -> serv.listarUtilizadores().forEach(System.out::println);
                case 2 -> adicionarUtilizador();
                case 3 -> inativarUtilizador();
                case 4 -> anonimizarUtilizador();
                case 0 -> { return; }
                default -> System.out.println("Opção inválida");
            }
        }
    }

    private void adicionarUtilizador() {
        String id = UtilsIO.lerStringNaoVazia(sc, "ID: ");
        String nome = UtilsIO.lerStringNaoVazia(sc, "Nome: ");
        String email = UtilsIO.lerStringNaoVazia(sc, "Email: ");
        Utilizador.Perfil perfil = UtilsIO.lerEnum(sc, "Perfil", Utilizador.Perfil.class);
        String senha = UtilsIO.lerStringNaoVazia(sc, "Senha: ");
        String equipa = UtilsIO.lerStringOpcional(sc, "Equipa (opcional): ");
        Integer turnoInicio = null;
        Integer turnoFim = null;
        if (UtilsIO.lerSimNao(sc, "Definir turno")) {
            turnoInicio = UtilsIO.lerInt(sc, "Turno início (0-23): ", 0, 23);
            turnoFim = UtilsIO.lerInt(sc, "Turno fim (0-23): ", 0, 23);
        }
        try {
            serv.adicionarUtilizador(id, nome, email, perfil, senha, equipa, turnoInicio, turnoFim);
            System.out.println("Utilizador criado.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void inativarUtilizador() {
        String id = UtilsIO.lerStringNaoVazia(sc, "ID utilizador: ");
        try {
            serv.inativarUtilizador(id);
            System.out.println("Utilizador inativado.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void anonimizarUtilizador() {
        String id = UtilsIO.lerStringNaoVazia(sc, "ID utilizador: ");
        try {
            serv.anonimizarUtilizador(id);
            System.out.println("Utilizador anonimizado.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void menuRelatorios() {
        System.out.println("--- Relatorios ---");
        System.out.printf("MTTR (min): %.2f%n", serv.calcularMTTRMin());
        System.out.printf("MTBF (h): %.2f%n", serv.calcularMTBFHoras());
        System.out.println("Listagem total: " + serv.backlogTotal());
        System.out.println("Listagem por criticidade: " + serv.backlogPorCriticidade());
        System.out.printf("Cumprimento planos (%%): %.2f%n", serv.cumprimentoPlanosPercent());
        System.out.printf("Cumprimento SLA (%%): %.2f%n", serv.cumprimentoSlaPercent());
    }

    private void menuFornecedores() {
        if (!requerPerfil(Utilizador.Perfil.GESTOR, Utilizador.Perfil.PLANEADOR)) return;
        while (true) {
            System.out.println("--- Fornecedores ---");
            System.out.println("1) Listar");
            System.out.println("2) Adicionar");
            System.out.println("0) Voltar");
            int op = UtilsIO.lerInt(sc, "Opção -> ");
            switch (op) {
                case 1 -> serv.listarFornecedores().forEach(System.out::println);
                case 2 -> adicionarFornecedor();
                case 0 -> { return; }
                default -> System.out.println("Opção inválida");
            }
        }
    }

    private void adicionarFornecedor() {
        String nome = UtilsIO.lerStringNaoVazia(sc, "Nome: ");
        String contacto = UtilsIO.lerStringOpcional(sc, "Contacto (opcional): ");
        String email = UtilsIO.lerStringOpcional(sc, "Email (opcional): ");
        Integer sla = null;
        if (UtilsIO.lerSimNao(sc, "Definir SLA padrao")) {
            sla = UtilsIO.lerInt(sc, "SLA (horas): ");
        }
        try {
            serv.adicionarFornecedor(nome, contacto, email, sla);
            System.out.println("Fornecedor criado.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void menuImportExport() {
        while (true) {
            System.out.println("--- Importar/Exportar ---");
            System.out.println("1) Exportar dados (CSV)");
            System.out.println("2) Importar contadores (CSV)");
            System.out.println("3) Importar ativos (CSV)");
            System.out.println("4) Importar pecas (CSV)");
            System.out.println("5) Importar OTs (CSV)");
            System.out.println("0) Voltar");
            int op = UtilsIO.lerInt(sc, "Opção -> ");
            switch (op) {
                case 1 -> exportarDados();
                case 2 -> importarContadores();
                case 3 -> importarAtivos();
                case 4 -> importarPecas();
                case 5 -> importarOTs();
                case 0 -> { return; }
                default -> System.out.println("Opção inválida");
            }
        }
    }

    private void exportarDados() {
        String dir = UtilsIO.lerStringNaoVazia(sc, "Diretório destino: ");
        try {
            serv.exportarDados(Path.of(dir));
            System.out.println("Exportação concluída.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void importarContadores() {
        String path = UtilsIO.lerStringNaoVazia(sc, "Ficheiro CSV: ");
        try {
            serv.importarContadores(Path.of(path));
            System.out.println("Importação concluída.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void importarAtivos() {
        String path = UtilsIO.lerStringNaoVazia(sc, "Ficheiro CSV: ");
        try {
            serv.importarAtivos(Path.of(path));
            System.out.println("Importação concluída.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void importarPecas() {
        String path = UtilsIO.lerStringNaoVazia(sc, "Ficheiro CSV: ");
        try {
            serv.importarPecas(Path.of(path));
            System.out.println("Importação concluída.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void importarOTs() {
        String path = UtilsIO.lerStringNaoVazia(sc, "Ficheiro CSV: ");
        try {
            serv.importarOTs(Path.of(path));
            System.out.println("Importação concluída.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void menuNotificacoes() {
        List<Notificacao> novas = serv.gerarNotificacoes();
        if (novas.isEmpty()) {
            System.out.println("(sem notificações novas)");
        } else {
            System.out.println("Notificações geradas: " + novas.size());
            novas.forEach(System.out::println);
            serv.marcarNotificacoesEnviadas();
        }
    }

    private void menuParametros() {
        if (!requerPerfil(Utilizador.Perfil.GESTOR, Utilizador.Perfil.PLANEADOR)) return;
        while (true) {
            System.out.println("--- Parametros ---");
            System.out.println("1) Listar");
            System.out.println("2) Editar");
            System.out.println("3) Configurar dashboard por perfil");
            System.out.println("0) Voltar");
            int op = UtilsIO.lerInt(sc, "Opção -> ");
            switch (op) {
                case 1 -> serv.listarParametros().forEach(System.out::println);
                case 2 -> editarParametro();
                case 3 -> configurarDashboard();
                case 0 -> { return; }
                default -> System.out.println("Opção inválida");
            }
        }
    }

    private void editarParametro() {
        List<Parametro> lista = serv.listarParametros();
        if (lista.isEmpty()) { System.out.println("(sem parâmetros)"); return; }
        for (int i = 0; i < lista.size(); i++) {
            System.out.println((i + 1) + ") " + lista.get(i));
        }
        int op = UtilsIO.lerInt(sc, "Escolha (0 para sair): ");
        if (op <= 0 || op > lista.size()) return;
        Parametro p = lista.get(op - 1);
        String novaDesc = UtilsIO.lerStringNaoVazia(sc, "Nova descrição/valor: ");
        p.setDescricao(novaDesc);
        System.out.println("Parâmetro atualizado.");
    }

    private void configurarDashboard() {
        Utilizador.Perfil perfil = UtilsIO.lerEnum(sc, "Perfil", Utilizador.Perfil.class);
        String widgets = UtilsIO.lerStringNaoVazia(sc, "Widgets (ex: ATIVOS,BACKLOG,MTTR): ");
        Integer pageSize = null;
        if (UtilsIO.lerSimNao(sc, "Definir tamanho de página")) {
            pageSize = UtilsIO.lerInt(sc, "Tamanho de página: ");
        }
        serv.atualizarDashboard(perfil, widgets, pageSize);
        System.out.println("Dashboard atualizado.");
    }

    private void menuAuditoria() {
        if (!requerPerfil(Utilizador.Perfil.GESTOR, Utilizador.Perfil.PLANEADOR)) return;
        String entidade = UtilsIO.lerStringOpcional(sc, "Filtrar entidade (enter para todas): ");
        String id = UtilsIO.lerStringOpcional(sc, "Filtrar id (enter para todos): ");
        List<Auditoria> lista = serv.listarAuditoria().stream()
                .filter(a -> entidade == null || a.getEntidade().equalsIgnoreCase(entidade))
                .filter(a -> id == null || a.getIdEntidade().equalsIgnoreCase(id))
                .toList();
        listarPaginado(lista, tamanhoPagina());
    }

    private void menuPrivacidade() {
        if (!requerPerfil(Utilizador.Perfil.GESTOR)) return;
        while (true) {
            System.out.println("--- Privacidade/Retencao ---");
            System.out.println("1) Aplicar retencao de dados");
            System.out.println("0) Voltar");
            int op = UtilsIO.lerInt(sc, "Opcao -> ");
            switch (op) {
                case 1 -> {
                    serv.aplicarRetencaoDados();
                    System.out.println("Retenção aplicada.");
                }
                case 0 -> { return; }
                default -> System.out.println("Opção inválida");
            }
        }
    }

    private void guardar() {
        try {
            serv.guardarTudo();
            System.out.println("Dados guardados em data/");
        } catch (Exception e) {
            System.out.println("Erro ao guardar: " + e.getMessage());
        }
    }

    private int tamanhoPagina() {
        DashboardConfig cfg = serv.obterDashboardPorPerfil(serv.getUtilizadorAtual().getPerfil());
        return cfg != null && cfg.getPageSize() != null ? cfg.getPageSize() : 10;
    }

    private void menuVistas(String entidade) {
        List<VistaPesquisa> vistas = serv.listarVistasUtilizador(entidade);
        if (vistas.isEmpty()) {
            System.out.println("(sem vistas guardadas)");
            return;
        }
        for (int i = 0; i < vistas.size(); i++) {
            System.out.println((i + 1) + ") " + vistas.get(i));
        }
        int op = UtilsIO.lerInt(sc, "Aplicar vista (0 para sair): ");
        if (op <= 0 || op > vistas.size()) return;
        VistaPesquisa v = vistas.get(op - 1);
        if ("ATIVOS".equalsIgnoreCase(entidade)) {
            List<Ativo> lista = v.getFiltro() == null ? serv.listarAtivos() : serv.pesquisarAtivos(v.getFiltro());
            if (v.getOrdenacao() != null) {
                switch (v.getOrdenacao().toLowerCase()) {
                    case "nome" -> lista.sort(java.util.Comparator.comparing(Ativo::getNome));
                    case "criticidade" -> lista.sort(java.util.Comparator.comparingInt(Ativo::getCriticidade).reversed());
                    default -> lista.sort(java.util.Comparator.comparing(Ativo::getCodigo));
                }
            }
            listarPaginado(lista, v.getPageSize() == null ? tamanhoPagina() : v.getPageSize());
        } else if ("OTS".equalsIgnoreCase(entidade)) {
            List<OrdemTrabalho> lista = v.getFiltro() == null ? serv.listarOrdemTrabalho() : serv.pesquisarOTs(v.getFiltro());
            if (v.getOrdenacao() != null) {
                switch (v.getOrdenacao().toLowerCase()) {
                    case "prioridade" -> lista.sort(java.util.Comparator.comparingInt(OrdemTrabalho::getPrioridade).reversed());
                    case "estado" -> lista.sort(java.util.Comparator.comparing(OrdemTrabalho::getEstado));
                    default -> lista.sort(java.util.Comparator.comparing(OrdemTrabalho::getId));
                }
            }
            listarPaginado(lista, v.getPageSize() == null ? tamanhoPagina() : v.getPageSize());
        }
    }

    private boolean requerPerfil(Utilizador.Perfil... perfis) {
        Utilizador atual = serv.getUtilizadorAtual();
        for (Utilizador.Perfil p : perfis) {
            if (atual.getPerfil() == p) return true;
        }
        System.out.println("Permissão insuficiente.");
        return false;
    }

    private <T> void listarPaginado(List<T> lista, int pageSize) {
        if (lista.isEmpty()) {
            System.out.println("(sem resultados)");
            return;
        }
        int tamanho = lista.size();
        int pagina = 0;
        int totalPaginas = (int) Math.ceil((double) tamanho / pageSize);
        while (true) {
            int inicio = pagina * pageSize;
            int fim = Math.min(inicio + pageSize, tamanho);
            System.out.println(String.format("Página %d/%d (%d-%d de %d)", pagina + 1, totalPaginas, inicio + 1, fim, tamanho));
            for (int i = inicio; i < fim; i++) {
                System.out.println(lista.get(i));
            }
            if (totalPaginas <= 1) return;
            System.out.print("[n]ext, [p]rev, [q]uit: ");
            String cmd = sc.nextLine().trim().toLowerCase();
            if ("n".equals(cmd) && pagina + 1 < totalPaginas) pagina++;
            else if ("p".equals(cmd) && pagina > 0) pagina--;
            else if ("q".equals(cmd) || cmd.isBlank()) return;
        }
    }
}
