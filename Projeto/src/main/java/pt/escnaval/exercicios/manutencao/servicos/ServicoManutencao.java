package pt.escnaval.exercicios.manutencao.servicos;

import pt.escnaval.exercicios.manutencao.infra.RepositorioCsv;
import pt.escnaval.exercicios.manutencao.modelo.*;
import pt.escnaval.exercicios.manutencao.utils.AppLogger;
import pt.escnaval.exercicios.manutencao.utils.SenhaUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class ServicoManutencao {
    private final RepositorioCsv repo = new RepositorioCsv(Path.of("data"));

    private final List<Ativo> ativos = new ArrayList<>();
    private final List<Localizacao> localizacoes = new ArrayList<>();
    private final List<Contador> contadores = new ArrayList<>();
    private final List<Documento> documentos = new ArrayList<>();
    private final List<OrdemTrabalho> ots = new ArrayList<>();
    private final List<TarefaOT> tarefas = new ArrayList<>();
    private final List<ConsumoPeca> consumos = new ArrayList<>();
    private final List<RegistoExecucao> execucoes = new ArrayList<>();
    private final List<HistoricoEstadoOT> historicoOT = new ArrayList<>();
    private final List<PlanoPreventivo> planos = new ArrayList<>();
    private final List<RegraGatilho> gatilhos = new ArrayList<>();
    private final List<Peca> pecas = new ArrayList<>();
    private final List<Stock> stocks = new ArrayList<>();
    private final List<MovimentoStock> movimentos = new ArrayList<>();
    private final List<InventarioCiclico> inventarios = new ArrayList<>();
    private final List<Fornecedor> fornecedores = new ArrayList<>();
    private final List<Utilizador> utilizadores = new ArrayList<>();
    private final List<Auditoria> auditoria = new ArrayList<>();
    private final List<PedidoManutencao> pedidos = new ArrayList<>();
    private final List<Notificacao> notificacoes = new ArrayList<>();
    private final List<Parametro> parametros = new ArrayList<>();
    private final List<DashboardConfig> dashboards = new ArrayList<>();
    private final List<VistaPesquisa> vistas = new ArrayList<>();

    private Utilizador utilizadorAtual;

    public void carregarTudo() throws IOException {
        int tentativas = 2;
        IOException ultimo = null;
        for (int i = 0; i < tentativas; i++) {
            try {
                carregarTudoInterno();
                return;
            } catch (IOException e) {
                ultimo = e;
                AppLogger.get().log(Level.WARNING, "Falha ao carregar dados, tentativa " + (i + 1), e);
            }
        }
        throw ultimo;
    }

    private void carregarTudoInterno() throws IOException {
        ativos.clear();
        localizacoes.clear();
        contadores.clear();
        documentos.clear();
        ots.clear();
        tarefas.clear();
        consumos.clear();
        execucoes.clear();
        historicoOT.clear();
        planos.clear();
        gatilhos.clear();
        pecas.clear();
        stocks.clear();
        movimentos.clear();
        inventarios.clear();
        fornecedores.clear();
        utilizadores.clear();
        auditoria.clear();
        pedidos.clear();
        notificacoes.clear();
        parametros.clear();
        dashboards.clear();
        vistas.clear();

        ativos.addAll(repo.carregarAtivos());
        localizacoes.addAll(repo.carregarLocalizacoes());
        contadores.addAll(repo.carregarContadores());
        documentos.addAll(repo.carregarDocumentos());
        ots.addAll(repo.carregarOTs());
        tarefas.addAll(repo.carregarTarefasOT());
        consumos.addAll(repo.carregarConsumos());
        execucoes.addAll(repo.carregarExecucoes());
        historicoOT.addAll(repo.carregarHistoricoOT());
        planos.addAll(repo.carregarPlanos());
        gatilhos.addAll(repo.carregarGatilhos());
        pecas.addAll(repo.carregarPecas());
        stocks.addAll(repo.carregarStock());
        movimentos.addAll(repo.carregarMovimentos());
        inventarios.addAll(repo.carregarInventarios());
        fornecedores.addAll(repo.carregarFornecedores());
        utilizadores.addAll(repo.carregarUtilizadores());
        auditoria.addAll(repo.carregarAuditoria());
        pedidos.addAll(repo.carregarPedidos());
        notificacoes.addAll(repo.carregarNotificacoes());
        parametros.addAll(repo.carregarParametros());
        dashboards.addAll(repo.carregarDashboards());
        vistas.addAll(repo.carregarVistas());

        if (parametros.isEmpty()) {
            parametros.add(new Parametro(novoId(), Parametro.Tipo.PRIORIDADE, "1", "Baixa"));
            parametros.add(new Parametro(novoId(), Parametro.Tipo.PRIORIDADE, "2", "Media"));
            parametros.add(new Parametro(novoId(), Parametro.Tipo.PRIORIDADE, "3", "Alta"));
            parametros.add(new Parametro(novoId(), Parametro.Tipo.PRIORIDADE, "4", "Urgente"));
            parametros.add(new Parametro(novoId(), Parametro.Tipo.PRIORIDADE, "5", "Critica"));
            parametros.add(new Parametro(novoId(), Parametro.Tipo.CATEGORIA_FALHA, "ELEC", "Eletrica"));
            parametros.add(new Parametro(novoId(), Parametro.Tipo.CATEGORIA_FALHA, "MEC", "Mecanica"));
            parametros.add(new Parametro(novoId(), Parametro.Tipo.CENTRO_CUSTO, "GERAL", "Geral"));
            parametros.add(new Parametro(novoId(), Parametro.Tipo.SLA_PRIORIDADE, "1", "24"));
            parametros.add(new Parametro(novoId(), Parametro.Tipo.SLA_PRIORIDADE, "3", "12"));
            parametros.add(new Parametro(novoId(), Parametro.Tipo.SLA_PRIORIDADE, "5", "4"));
            parametros.add(new Parametro(novoId(), Parametro.Tipo.RETENCAO_AUDITORIA_DIAS, "DEFAULT", "365"));
            parametros.add(new Parametro(novoId(), Parametro.Tipo.RETENCAO_EXECUCAO_DIAS, "DEFAULT", "365"));
        }

        if (utilizadores.isEmpty()) {
            utilizadores.add(new Utilizador("admin", "Administrador", "admin@example.local",
                    Utilizador.Perfil.GESTOR, SenhaUtils.hash("admin")));
        }

        if (dashboards.isEmpty()) {
            dashboards.add(new DashboardConfig(novoId(), Utilizador.Perfil.GESTOR, "ATIVOS,BACKLOG,MTTR,MTBF,STOCK", 10));
            dashboards.add(new DashboardConfig(novoId(), Utilizador.Perfil.PLANEADOR, "BACKLOG,MTTR,PLANOS", 10));
            dashboards.add(new DashboardConfig(novoId(), Utilizador.Perfil.TECNICO, "MINHAS_OT,NOTIFICACOES", 10));
            dashboards.add(new DashboardConfig(novoId(), Utilizador.Perfil.SOLICITANTE, "PEDIDOS,NOTIFICACOES", 10));
        }
    }

    public void guardarTudo() throws IOException {
        int tentativas = 2;
        IOException ultimo = null;
        for (int i = 0; i < tentativas; i++) {
            try {
                guardarTudoInterno();
                return;
            } catch (IOException e) {
                ultimo = e;
                AppLogger.get().log(Level.WARNING, "Falha ao guardar dados, tentativa " + (i + 1), e);
            }
        }
        throw ultimo;
    }

    private void guardarTudoInterno() throws IOException {
        List<String> ficheiros = List.of(
                "ativos.csv", "localizacoes.csv", "contadores.csv", "documentos.csv", "ots.csv",
                "ot_tarefas.csv", "ot_consumos.csv", "ot_execucoes.csv", "ot_historico.csv",
                "planos.csv", "planos_gatilhos.csv", "pecas.csv", "stock.csv", "movimentos.csv",
                "inventarios.csv", "fornecedores.csv", "utilizadores.csv", "auditoria.csv", "pedidos.csv",
                "notificacoes.csv", "parametros.csv", "dashboard.csv", "vistas.csv"
        );
        Path backupDir = Path.of("data", "backup");
        Files.createDirectories(backupDir);
        for (String f : ficheiros) {
            Path src = Path.of("data").resolve(f);
            if (Files.exists(src)) {
                Files.copy(src, backupDir.resolve(f + ".bak"), StandardCopyOption.REPLACE_EXISTING);
            }
        }
        try {
            repo.salvarAtivos(ativos);
            repo.salvarLocalizacoes(localizacoes);
            repo.salvarContadores(contadores);
            repo.salvarDocumentos(documentos);
            repo.salvarOTs(ots);
            repo.salvarTarefasOT(tarefas);
            repo.salvarConsumos(consumos);
            repo.salvarExecucoes(execucoes);
            repo.salvarHistoricoOT(historicoOT);
            repo.salvarPlanos(planos);
            repo.salvarGatilhos(gatilhos);
            repo.salvarPecas(pecas);
            repo.salvarStock(stocks);
            repo.salvarMovimentos(movimentos);
            repo.salvarInventarios(inventarios);
            repo.salvarFornecedores(fornecedores);
            repo.salvarUtilizadores(utilizadores);
            repo.salvarAuditoria(auditoria);
            repo.salvarPedidos(pedidos);
            repo.salvarNotificacoes(notificacoes);
            repo.salvarParametros(parametros);
            repo.salvarDashboards(dashboards);
            repo.salvarVistas(vistas);
        } catch (IOException e) {
            AppLogger.get().log(Level.SEVERE, "Falha ao guardar, a tentar restaurar backup", e);
            for (String f : ficheiros) {
                Path bak = backupDir.resolve(f + ".bak");
                if (Files.exists(bak)) {
                    Files.copy(bak, Path.of("data").resolve(f), StandardCopyOption.REPLACE_EXISTING);
                }
            }
            throw e;
        }
    }

    public Utilizador autenticar(String id, String senha) {
        Utilizador u = obterUtilizador(id);
        if (u == null) return null;
        if (u.getEstado() != EstadoUtilizador.ATIVO) return null;
        String hash = SenhaUtils.hash(senha);
        if (hash.equals(u.getPasswordHash())) {
            utilizadorAtual = u;
            return u;
        }
        return null;
    }

    public Utilizador getUtilizadorAtual() { return utilizadorAtual; }
    public void setUtilizadorAtual(Utilizador utilizadorAtual) { this.utilizadorAtual = utilizadorAtual; }

    public List<Ativo> listarAtivos() { return new ArrayList<>(ativos); }
    public List<OrdemTrabalho> listarOrdemTrabalho() { return new ArrayList<>(ots); }
    public List<Peca> listarPecas() { return new ArrayList<>(pecas); }
    public List<PedidoManutencao> listarPedidos() { return new ArrayList<>(pedidos); }
    public List<Utilizador> listarUtilizadores() { return new ArrayList<>(utilizadores); }
    public List<Notificacao> listarNotificacoes() { return new ArrayList<>(notificacoes); }
    public List<PlanoPreventivo> listarPlanos() { return new ArrayList<>(planos); }
    public List<RegraGatilho> listarGatilhos() { return new ArrayList<>(gatilhos); }
    public List<Contador> listarContadores() { return new ArrayList<>(contadores); }
    public List<Stock> listarStock() { return new ArrayList<>(stocks); }
    public List<MovimentoStock> listarMovimentos() { return new ArrayList<>(movimentos); }
    public List<InventarioCiclico> listarInventarios() { return new ArrayList<>(inventarios); }
    public List<Fornecedor> listarFornecedores() { return new ArrayList<>(fornecedores); }
    public List<Documento> listarDocumentos() { return new ArrayList<>(documentos); }
    public List<HistoricoEstadoOT> listarHistoricoOT() { return new ArrayList<>(historicoOT); }
    public List<DashboardConfig> listarDashboards() { return new ArrayList<>(dashboards); }
    public List<VistaPesquisa> listarVistas() { return new ArrayList<>(vistas); }
    public List<Auditoria> listarAuditoria() { return new ArrayList<>(auditoria); }
    public List<Parametro> listarParametros() { return new ArrayList<>(parametros); }

    public Ativo buscarAtivo(String codigo) {
        return ativos.stream().filter(a -> a.getCodigo().equalsIgnoreCase(codigo)).findFirst().orElse(null);
    }

    public OrdemTrabalho buscarOT(String id) {
        return ots.stream().filter(o -> o.getId().equalsIgnoreCase(id)).findFirst().orElse(null);
    }

    public Parametro buscarParametro(String id) {
        return parametros.stream().filter(p -> p.getId().equalsIgnoreCase(id)).findFirst().orElse(null);
    }

    public Utilizador obterUtilizador(String id) {
        return utilizadores.stream().filter(u -> u.getId().equalsIgnoreCase(id)).findFirst().orElse(null);
    }

    public Utilizador adicionarUtilizador(String id, String nome, String email, Utilizador.Perfil perfil, String senha) {
        return adicionarUtilizador(id, nome, email, perfil, senha, null, null, null);
    }

    public Utilizador adicionarUtilizador(String id, String nome, String email, Utilizador.Perfil perfil, String senha,
                                          String equipa, Integer turnoInicio, Integer turnoFim) {
        if (obterUtilizador(id) != null) throw new IllegalArgumentException("Utilizador ja existe");
        Utilizador u = new Utilizador(id, nome, email, perfil, SenhaUtils.hash(senha), EstadoUtilizador.ATIVO, equipa, turnoInicio, turnoFim);
        utilizadores.add(u);
        auditar("Utilizador", id, "novo", null, email);
        return u;
    }

    public void inativarUtilizador(String id) {
        Utilizador u = obterUtilizador(id);
        if (u == null) throw new IllegalArgumentException("Utilizador nao encontrado");
        u.setEstado(EstadoUtilizador.INATIVO);
        auditar("Utilizador", id, "estado", "ATIVO", "INATIVO");
    }

    public Ativo adicionarAtivo(String codigo, String nome, int criticidade, String idPai, String idLocalizacao) {
        validarCriticidade(criticidade);
        if (buscarAtivo(codigo) != null) throw new IllegalArgumentException("Ativo ja existe");
        if (idPai != null && buscarAtivo(idPai) == null) throw new IllegalArgumentException("Ativo pai inexistente");
        if (idLocalizacao != null && buscarLocalizacao(idLocalizacao) == null) throw new IllegalArgumentException("Localizacao inexistente");
        Ativo a = new Ativo(codigo, nome, EstadoAtivo.ATIVO, criticidade, idPai, idLocalizacao);
        ativos.add(a);
        auditar("Ativo", codigo, "novo", null, nome);
        return a;
    }

    public void atualizarAtivo(String codigo, String nome, EstadoAtivo estado, Integer criticidade, String idPai, String idLocalizacao) {
        Ativo a = buscarAtivo(codigo);
        if (a == null) throw new IllegalArgumentException("Ativo nao encontrado");
        if (nome != null && !nome.equals(a.getNome())) {
            auditar("Ativo", codigo, "nome", a.getNome(), nome);
            a.setNome(nome);
        }
        if (estado != null && estado != a.getEstado()) {
            auditar("Ativo", codigo, "estado", a.getEstado().name(), estado.name());
            a.setEstado(estado);
        }
        if (criticidade != null && criticidade != a.getCriticidade()) {
            validarCriticidade(criticidade);
            auditar("Ativo", codigo, "criticidade", Integer.toString(a.getCriticidade()), Integer.toString(criticidade));
            a.setCriticidade(criticidade);
        }
        if (idPai != null) a.setIdAtivoPai(idPai);
        if (idLocalizacao != null) a.setIdLocalizacao(idLocalizacao);
    }

    public void arquivarAtivo(String codigo) {
        atualizarAtivo(codigo, null, EstadoAtivo.ARQUIVADO, null, null, null);
    }

    public Localizacao adicionarLocalizacao(String nome, String idPai) {
        String id = novoId();
        Localizacao l = new Localizacao(id, nome, idPai);
        localizacoes.add(l);
        auditar("Localizacao", id, "novo", null, nome);
        return l;
    }

    public Localizacao buscarLocalizacao(String id) {
        return localizacoes.stream().filter(l -> l.getId().equalsIgnoreCase(id)).findFirst().orElse(null);
    }

    public Contador atualizarContador(String ativoCodigo, Contador.Tipo tipo, double leitura, String unidade) {
        Contador c = contadores.stream()
                .filter(x -> x.getAtivoCodigo().equalsIgnoreCase(ativoCodigo) && x.getTipo() == tipo)
                .findFirst().orElse(null);
        if (c == null) {
            c = new Contador(novoId(), ativoCodigo, tipo, leitura, unidade, Instant.now());
            contadores.add(c);
            auditar("Contador", c.getId(), "novo", null, Double.toString(leitura));
        } else {
            auditar("Contador", c.getId(), "leitura", Double.toString(c.getLeituraAtual()), Double.toString(leitura));
            c.setLeituraAtual(leitura);
            c.setUnidade(unidade);
            c.setAtualizadoEm(Instant.now());
        }
        return c;
    }

    public void simularApiContadores(int incrementos) {
        Random rnd = new Random();
        List<Ativo> ativosAtivos = ativos.stream()
                .filter(a -> a.getEstado() == EstadoAtivo.ATIVO)
                .toList();
        for (int i = 0; i < incrementos; i++) {
            if (ativosAtivos.isEmpty()) break;
            Ativo a = ativosAtivos.get(rnd.nextInt(ativosAtivos.size()));
            Contador.Tipo tipo = rnd.nextBoolean() ? Contador.Tipo.TEMPO : Contador.Tipo.USO;
            double delta = 1 + rnd.nextInt(10);
            Contador c = contadores.stream()
                    .filter(x -> x.getAtivoCodigo().equalsIgnoreCase(a.getCodigo()) && x.getTipo() == tipo)
                    .findFirst().orElse(null);
            if (c == null) {
                c = new Contador(novoId(), a.getCodigo(), tipo, delta, tipo == Contador.Tipo.TEMPO ? "h" : "ciclos", Instant.now());
                contadores.add(c);
            } else {
                c.setLeituraAtual(c.getLeituraAtual() + delta);
                c.setAtualizadoEm(Instant.now());
            }
        }
    }

    public Documento adicionarDocumento(String entidade, String entidadeId, String tipo, String path, String meta) {
        Documento d = new Documento(novoId(), entidade, entidadeId, tipo, path, meta);
        documentos.add(d);
        auditar("Documento", d.getId(), "novo", null, path);
        return d;
    }

    public OrdemTrabalho criarOrdemTrabalho(OrdemTrabalho.Tipo tipo, int prioridade, String descricao, String idAtivo,
                                            String categoriaFalha, String centroCusto, Instant dataLimite) {
        return criarOrdemTrabalhoCompleta(tipo, prioridade, descricao, idAtivo, categoriaFalha, centroCusto,
                dataLimite, null, null, null, null);
    }

    public OrdemTrabalho criarOrdemTrabalhoCompleta(OrdemTrabalho.Tipo tipo, int prioridade, String descricao, String idAtivo,
                                                    String categoriaFalha, String centroCusto, Instant dataLimite,
                                                    Integer slaHoras, String fornecedorId, String ordemExterna, Double custoPrevisto) {
        Ativo ativo = buscarAtivo(idAtivo);
        if (ativo == null) throw new IllegalArgumentException("Ativo inexistente");
        if (prioridade <= 0) prioridade = Math.max(1, Math.min(5, ativo.getCriticidade()));
        validarCriticidade(prioridade);
        OrdemTrabalho ot = new OrdemTrabalho(novoId(), tipo, prioridade, descricao, idAtivo);
        ot.setCategoriaFalha(categoriaFalha);
        ot.setCentroCusto(centroCusto);
        ot.setDataLimite(dataLimite);
        if (custoPrevisto != null) ot.setCustoPrevisto(custoPrevisto);
        if (slaHoras == null) slaHoras = slaPorPrioridade(prioridade);
        ot.setSlaHoras(slaHoras);
        if (fornecedorId != null) {
            if (buscarFornecedor(fornecedorId) == null) throw new IllegalArgumentException("Fornecedor inexistente");
            ot.setFornecedorId(fornecedorId);
        }
        ot.setOrdemExterna(ordemExterna);
        ots.add(ot);
        historicoOT.add(new HistoricoEstadoOT(novoId(), ot.getId(), ot.getEstado(), Instant.now(), idUtilizadorAtual()));
        auditar("OT", ot.getId(), "novo", null, descricao);
        criarNotificacao("OT", "OT criada: " + ot.getId());
        return ot;
    }

    public void alterarEstadoOT(String otId, OrdemTrabalho.Estado novoEstado, String utilizadorId) {
        OrdemTrabalho ot = buscarOT(otId);
        if (ot == null) throw new IllegalArgumentException("OT nao encontrada");
        if (!transicaoValida(ot.getEstado(), novoEstado)) {
            throw new IllegalArgumentException("Transicao invalida: " + ot.getEstado() + " -> " + novoEstado);
        }
        ot.setEstado(novoEstado);
        if (novoEstado == OrdemTrabalho.Estado.CONCLUIDA) ot.setDataFim(Instant.now());
        historicoOT.add(new HistoricoEstadoOT(novoId(), ot.getId(), novoEstado, Instant.now(), utilizadorId));
        auditar("OT", ot.getId(), "estado", null, novoEstado.name());
        criarNotificacao("ESTADO_OT", "OT " + ot.getId() + " passou para " + novoEstado);
    }

    public void atualizarPrioridadeOT(String otId, int prioridade) {
        OrdemTrabalho ot = buscarOT(otId);
        if (ot == null) throw new IllegalArgumentException("OT nao encontrada");
        validarCriticidade(prioridade);
        int anterior = ot.getPrioridade();
        if (anterior == prioridade) return;
        Integer slaAtual = ot.getSlaHoras();
        Integer slaAnterior = slaPorPrioridade(anterior);
        if (slaAtual != null && slaAnterior != null && slaAtual.equals(slaAnterior)) {
            ot.setSlaHoras(slaPorPrioridade(prioridade));
        }
        ot.setPrioridade(prioridade);
        auditar("OT", ot.getId(), "prioridade", Integer.toString(anterior), Integer.toString(prioridade));
    }

    public void atribuirTecnico(String otId, String tecnicoId, Instant inicioPlaneado) {
        OrdemTrabalho ot = buscarOT(otId);
        if (ot == null) throw new IllegalArgumentException("OT nao encontrada");
        Utilizador u = obterUtilizador(tecnicoId);
        if (u == null || u.getPerfil() != Utilizador.Perfil.TECNICO || u.getEstado() != EstadoUtilizador.ATIVO) {
            throw new IllegalArgumentException("Tecnico invalido");
        }
        if (inicioPlaneado != null && !dentroTurno(u, inicioPlaneado)) {
            throw new IllegalArgumentException("Inicio fora do turno do tecnico");
        }
        if (inicioPlaneado != null && existeConflitoAgenda(tecnicoId, inicioPlaneado, duracaoPlaneada(otId))) {
            throw new IllegalArgumentException("Conflito de agenda");
        }
        ot.setIdTecnico(tecnicoId);
        if (inicioPlaneado != null) ot.setDataInicio(inicioPlaneado);
        alterarEstadoOT(otId, OrdemTrabalho.Estado.ATRIBUIDA, idUtilizadorAtual());
    }

    public TarefaOT adicionarTarefa(String otId, String descricao, int duracaoPlaneadaMin) {
        OrdemTrabalho ot = buscarOT(otId);
        if (ot == null) throw new IllegalArgumentException("OT nao encontrada");
        TarefaOT t = new TarefaOT(novoId(), otId, descricao, duracaoPlaneadaMin);
        tarefas.add(t);
        auditar("TarefaOT", t.getId(), "novo", null, descricao);
        return t;
    }

    public RegistoExecucao registarExecucao(String otId, Instant inicio, Instant fim, String causa,
                                            String acao, String tecnicoId, String observacoes) {
        OrdemTrabalho ot = buscarOT(otId);
        if (ot == null) throw new IllegalArgumentException("OT nao encontrada");
        OrdemTrabalho.Estado estadoAnterior = ot.getEstado();
        RegistoExecucao r = new RegistoExecucao(novoId(), otId, inicio, fim, causa, acao, tecnicoId, observacoes);
        execucoes.add(r);
        long dur = r.getDuracaoMin();
        if (dur > 0) ot.adicionarTempo((int) dur);
        if (inicio != null) {
            ot.setDataInicio(inicio);
            if (ot.getEstado() == OrdemTrabalho.Estado.ABERTA || ot.getEstado() == OrdemTrabalho.Estado.ATRIBUIDA) {
                ot.setEstado(OrdemTrabalho.Estado.EM_ANDAMENTO);
            }
        }
        if (fim != null) {
            ot.setDataFim(fim);
            ot.setEstado(OrdemTrabalho.Estado.CONCLUIDA);
        }
        if (estadoAnterior != ot.getEstado()) {
            historicoOT.add(new HistoricoEstadoOT(novoId(), ot.getId(), ot.getEstado(), Instant.now(), idUtilizadorAtual()));
        }
        auditar("Execucao", r.getId(), "novo", null, otId);
        return r;
    }

    public ConsumoPeca adicionarConsumoPeca(String otId, String sku, int quantidade) {
        return executarTransacao(() -> {
            OrdemTrabalho ot = buscarOT(otId);
            if (ot == null) throw new IllegalArgumentException("OT nao encontrada");
            Peca p = buscarPeca(sku);
            if (p == null) throw new IllegalArgumentException("Peca inexistente");
            movimentarStock(sku, "GERAL", MovimentoStock.Tipo.SAIDA, quantidade, otId);
            ConsumoPeca c = new ConsumoPeca(novoId(), otId, sku, quantidade, p.getCustoUnit());
            consumos.add(c);
            ot.adicionarCusto(c.getCustoTotal());
            auditar("Consumo", c.getId(), "novo", null, sku);
            return c;
        });
    }

    public Peca adicionarPeca(String sku, String designacao, String unidade, int pontoReposicao, double custoUnit) {
        if (buscarPeca(sku) != null) throw new IllegalArgumentException("SKU ja existe");
        Peca p = new Peca(sku, designacao, unidade, pontoReposicao, custoUnit);
        pecas.add(p);
        auditar("Peca", sku, "novo", null, designacao);
        return p;
    }

    public Peca buscarPeca(String sku) {
        return pecas.stream().filter(p -> p.getSku().equalsIgnoreCase(sku)).findFirst().orElse(null);
    }

    public Fornecedor adicionarFornecedor(String nome, String contacto, String email, Integer slaHorasPadrao) {
        Fornecedor f = new Fornecedor(novoId(), nome, contacto, email, slaHorasPadrao);
        fornecedores.add(f);
        auditar("Fornecedor", f.getId(), "novo", null, nome);
        return f;
    }

    public Fornecedor buscarFornecedor(String id) {
        return fornecedores.stream().filter(f -> f.getId().equalsIgnoreCase(id)).findFirst().orElse(null);
    }

    public MovimentoStock movimentarStock(String sku, String deposito, MovimentoStock.Tipo tipo, int quantidade, String otId) {
        if (quantidade <= 0) throw new IllegalArgumentException("Quantidade invalida");
        Stock stock = stocks.stream()
                .filter(s -> s.getSku().equalsIgnoreCase(sku) && s.getDeposito().equalsIgnoreCase(deposito))
                .findFirst().orElse(null);
        if (stock == null) {
            stock = new Stock(novoId(), sku, deposito, 0);
            stocks.add(stock);
        }
        int delta = switch (tipo) {
            case ENTRADA -> quantidade;
            case SAIDA -> -quantidade;
            case AJUSTE -> quantidade;
        };
        if (stock.getQuantidade() + delta < 0) throw new IllegalArgumentException("Stock insuficiente");
        stock.setQuantidade(stock.getQuantidade() + delta);
        MovimentoStock mov = new MovimentoStock(novoId(), sku, deposito, tipo, Instant.now(), quantidade, otId);
        movimentos.add(mov);
        auditar("Stock", stock.getId(), "quantidade", null, Integer.toString(stock.getQuantidade()));
        return mov;
    }

    public InventarioCiclico registarInventarioCiclico(String sku, String deposito, int quantidadeContada) {
        if (quantidadeContada < 0) throw new IllegalArgumentException("Quantidade invalida");
        Stock stock = stocks.stream()
                .filter(s -> s.getSku().equalsIgnoreCase(sku) && s.getDeposito().equalsIgnoreCase(deposito))
                .findFirst().orElse(null);
        int atual = stock == null ? 0 : stock.getQuantidade();
        int ajuste = quantidadeContada - atual;
        if (ajuste != 0) {
            MovimentoStock.Tipo tipo = MovimentoStock.Tipo.AJUSTE;
            Stock s = stock;
            if (s == null) {
                s = new Stock(novoId(), sku, deposito, 0);
                stocks.add(s);
            }
            s.setQuantidade(quantidadeContada);
            movimentos.add(new MovimentoStock(novoId(), sku, deposito, tipo, Instant.now(), Math.abs(ajuste), null));
        }
        InventarioCiclico inv = new InventarioCiclico(novoId(), sku, deposito, quantidadeContada, Instant.now(), idUtilizadorAtual());
        inventarios.add(inv);
        auditar("Inventario", inv.getId(), "contagem", Integer.toString(atual), Integer.toString(quantidadeContada));
        return inv;
    }

    public PlanoPreventivo criarPlanoPreventivo(String ativoCodigo, PlanoPreventivo.Politica politica, int periodicidadeDias, int janelaDias) {
        if (buscarAtivo(ativoCodigo) == null) throw new IllegalArgumentException("Ativo inexistente");
        PlanoPreventivo p = new PlanoPreventivo(novoId(), ativoCodigo, politica, periodicidadeDias, janelaDias, null);
        planos.add(p);
        auditar("Plano", p.getId(), "novo", null, ativoCodigo);
        return p;
    }

    public RegraGatilho adicionarGatilho(String planoId, RegraGatilho.Tipo tipo, double valor, String unidade) {
        RegraGatilho g = new RegraGatilho(novoId(), planoId, tipo, valor, unidade);
        gatilhos.add(g);
        auditar("Gatilho", g.getId(), "novo", null, planoId);
        return g;
    }

    public List<OrdemTrabalho> avaliarPlanos() {
        List<OrdemTrabalho> criadas = new ArrayList<>();
        Instant agora = Instant.now();
        for (PlanoPreventivo p : planos) {
            boolean deveCriar = false;
            double valorUso = 0.0;
            if (p.getPolitica() == PlanoPreventivo.Politica.TEMPO || p.getPolitica() == PlanoPreventivo.Politica.MISTA) {
                if (p.getUltimoDisparo() == null) {
                    deveCriar = true;
                } else if (p.getPeriodicidadeDias() > 0) {
                    long dias = Duration.between(p.getUltimoDisparo(), agora).toDays();
                    if (dias >= p.getPeriodicidadeDias()) deveCriar = true;
                }
            }
            if (!deveCriar && (p.getPolitica() == PlanoPreventivo.Politica.USO || p.getPolitica() == PlanoPreventivo.Politica.MISTA)) {
                for (RegraGatilho g : gatilhosDoPlano(p.getId())) {
                    if (g.getTipo() == RegraGatilho.Tipo.USO) {
                        double leitura = totalContadorUso(p.getAtivoCodigo());
                        if (leitura >= g.getValor()) {
                            deveCriar = true;
                            valorUso = g.getValor();
                            break;
                        }
                    }
                }
            }
            if (deveCriar) {
                OrdemTrabalho ot = criarOrdemTrabalho(OrdemTrabalho.Tipo.PREVENTIVA, 3,
                        "Preventiva gerada por plano " + p.getId(), p.getAtivoCodigo(), null, null, null);
                criadas.add(ot);
                p.setUltimoDisparo(agora);
                if (valorUso > 0) reduzirContadoresUso(p.getAtivoCodigo(), valorUso);
            }
        }
        return criadas;
    }

    public PedidoManutencao submeterPedido(String ativoCodigo, String descricao, String solicitanteId) {
        if (buscarAtivo(ativoCodigo) == null) throw new IllegalArgumentException("Ativo inexistente");
        PedidoManutencao p = new PedidoManutencao(novoId(), ativoCodigo, descricao, PedidoManutencao.Estado.SUBMETIDO,
                Instant.now(), solicitanteId, null);
        pedidos.add(p);
        auditar("Pedido", p.getId(), "novo", null, descricao);
        return p;
    }

    public void aprovarPedido(String pedidoId, String aprovadorId) {
        PedidoManutencao p = buscarPedido(pedidoId);
        if (p == null) throw new IllegalArgumentException("Pedido nao encontrado");
        p.setEstado(PedidoManutencao.Estado.APROVADO);
        p.setAprovadorId(aprovadorId);
        auditar("Pedido", p.getId(), "estado", null, p.getEstado().name());
    }

    public void rejeitarPedido(String pedidoId, String aprovadorId) {
        PedidoManutencao p = buscarPedido(pedidoId);
        if (p == null) throw new IllegalArgumentException("Pedido nao encontrado");
        p.setEstado(PedidoManutencao.Estado.REJEITADO);
        p.setAprovadorId(aprovadorId);
        auditar("Pedido", p.getId(), "estado", null, p.getEstado().name());
    }

    public OrdemTrabalho converterPedidoEmOt(String pedidoId, String aprovadorId) {
        PedidoManutencao p = buscarPedido(pedidoId);
        if (p == null) throw new IllegalArgumentException("Pedido nao encontrado");
        if (p.getEstado() != PedidoManutencao.Estado.APROVADO) throw new IllegalArgumentException("Pedido nao aprovado");
        OrdemTrabalho ot = criarOrdemTrabalho(OrdemTrabalho.Tipo.CORRETIVA, 3, p.getDescricao(), p.getAtivoCodigo(), null, null, null);
        ot.setIdPedido(p.getId());
        p.setEstado(PedidoManutencao.Estado.CONVERTIDO);
        p.setAprovadorId(aprovadorId);
        return ot;
    }

    public PedidoManutencao buscarPedido(String id) {
        return pedidos.stream().filter(p -> p.getId().equalsIgnoreCase(id)).findFirst().orElse(null);
    }

    public List<Ativo> pesquisarAtivos(String termo) {
        String t = termo.toLowerCase(Locale.ROOT);
        return ativos.stream()
                .filter(a -> a.getCodigo().toLowerCase(Locale.ROOT).contains(t) || a.getNome().toLowerCase(Locale.ROOT).contains(t))
                .collect(Collectors.toList());
    }

    public List<OrdemTrabalho> pesquisarOTs(String termo) {
        String t = termo.toLowerCase(Locale.ROOT);
        return ots.stream()
                .filter(o -> o.getId().toLowerCase(Locale.ROOT).contains(t) || o.getDescricao().toLowerCase(Locale.ROOT).contains(t))
                .collect(Collectors.toList());
    }

    public DashboardConfig obterDashboardPorPerfil(Utilizador.Perfil perfil) {
        return dashboards.stream().filter(d -> d.getPerfil() == perfil).findFirst().orElse(null);
    }

    public void atualizarDashboard(Utilizador.Perfil perfil, String widgets, Integer pageSize) {
        DashboardConfig d = obterDashboardPorPerfil(perfil);
        if (d == null) {
            dashboards.add(new DashboardConfig(novoId(), perfil, widgets, pageSize));
        } else {
            d.setWidgets(widgets);
            d.setPageSize(pageSize);
        }
        auditar("Dashboard", perfil.name(), "widgets", null, widgets);
    }

    public VistaPesquisa guardarVista(String entidade, String filtro, String ordenacao, Integer pageSize) {
        VistaPesquisa v = new VistaPesquisa(novoId(), idUtilizadorAtual(), entidade, filtro, ordenacao, pageSize);
        vistas.add(v);
        auditar("VistaPesquisa", v.getId(), "novo", null, entidade);
        return v;
    }

    public List<VistaPesquisa> listarVistasUtilizador(String entidade) {
        String uid = idUtilizadorAtual();
        return vistas.stream()
                .filter(v -> v.getUtilizadorId().equalsIgnoreCase(uid) && v.getEntidade().equalsIgnoreCase(entidade))
                .collect(Collectors.toList());
    }

    public double calcularMTTRMin() {
        List<Long> duracoes = execucoes.stream().map(RegistoExecucao::getDuracaoMin).filter(d -> d > 0).toList();
        if (duracoes.isEmpty()) return 0.0;
        long total = duracoes.stream().mapToLong(Long::longValue).sum();
        return (double) total / duracoes.size();
    }

    public double calcularMTBFHoras() {
        Map<String, List<OrdemTrabalho>> porAtivo = ots.stream()
                .filter(o -> o.getTipo() == OrdemTrabalho.Tipo.CORRETIVA && o.getDataFim() != null)
                .collect(Collectors.groupingBy(OrdemTrabalho::getIdAtivo));
        List<Long> diffs = new ArrayList<>();
        for (List<OrdemTrabalho> lista : porAtivo.values()) {
            lista.sort(Comparator.comparing(OrdemTrabalho::getDataFim));
            for (int i = 1; i < lista.size(); i++) {
                Instant a = lista.get(i - 1).getDataFim();
                Instant b = lista.get(i).getDataFim();
                if (a != null && b != null) {
                    diffs.add(Duration.between(a, b).toHours());
                }
            }
        }
        if (diffs.isEmpty()) return 0.0;
        long total = diffs.stream().mapToLong(Long::longValue).sum();
        return (double) total / diffs.size();
    }

    public long backlogTotal() {
        return ots.stream().filter(o -> o.getEstado() != OrdemTrabalho.Estado.CONCLUIDA
                && o.getEstado() != OrdemTrabalho.Estado.CANCELADA).count();
    }

    public double cumprimentoPlanosPercent() {
        if (planos.isEmpty()) return 0.0;
        Instant agora = Instant.now();
        long ok = 0;
        for (PlanoPreventivo p : planos) {
            if (p.getUltimoDisparo() == null || p.getPeriodicidadeDias() <= 0) continue;
            long dias = Duration.between(p.getUltimoDisparo(), agora).toDays();
            if (dias <= p.getPeriodicidadeDias() + p.getJanelaDias()) ok++;
        }
        return 100.0 * ok / planos.size();
    }

    public double cumprimentoSlaPercent() {
        long total = ots.stream().filter(o -> o.getSlaHoras() != null && o.getDataCriacao() != null).count();
        if (total == 0) return 0.0;
        long ok = ots.stream()
                .filter(o -> o.getSlaHoras() != null && o.getDataCriacao() != null && o.getDataFim() != null)
                .filter(o -> Duration.between(o.getDataCriacao(), o.getDataFim()).toHours() <= o.getSlaHoras())
                .count();
        return 100.0 * ok / total;
    }

    public Map<Integer, Long> backlogPorCriticidade() {
        Map<String, Integer> criticidadePorAtivo = ativos.stream().collect(Collectors.toMap(Ativo::getCodigo, Ativo::getCriticidade, (a, b) -> a));
        Map<Integer, Long> out = new TreeMap<>();
        for (OrdemTrabalho o : ots) {
            if (o.getEstado() == OrdemTrabalho.Estado.CONCLUIDA || o.getEstado() == OrdemTrabalho.Estado.CANCELADA) continue;
            int crit = criticidadePorAtivo.getOrDefault(o.getIdAtivo(), 1);
            out.put(crit, out.getOrDefault(crit, 0L) + 1);
        }
        return out;
    }

    public List<Notificacao> gerarNotificacoes() {
        List<Notificacao> novas = new ArrayList<>();
        for (Peca p : pecas) {
            int total = stocks.stream().filter(s -> s.getSku().equalsIgnoreCase(p.getSku())).mapToInt(Stock::getQuantidade).sum();
            if (total <= p.getPontoReposicao()) {
                novas.add(criarNotificacao("STOCK", "Stock baixo para " + p.getSku() + " (" + total + ")"));
            }
        }
        Instant agora = Instant.now();
        for (OrdemTrabalho o : ots) {
            if (o.getDataLimite() != null && o.getEstado() != OrdemTrabalho.Estado.CONCLUIDA && o.getEstado() != OrdemTrabalho.Estado.CANCELADA) {
                if (agora.isAfter(o.getDataLimite())) {
                    novas.add(criarNotificacao("PRAZO", "OT em atraso: " + o.getId()));
                }
            }
            if (o.getSlaHoras() != null && o.getDataCriacao() != null) {
                long horas = Duration.between(o.getDataCriacao(), agora).toHours();
                if (horas > o.getSlaHoras()) {
                    novas.add(criarNotificacao("SLA", "OT SLA excedido: " + o.getId()));
                }
            }
        }
        return novas;
    }

    public void marcarNotificacoesEnviadas() {
        for (Notificacao n : notificacoes) {
            if (n.getEstado() == Notificacao.Estado.NOVA) {
                n.setEstado(Notificacao.Estado.ENVIADA);
            }
        }
    }

    public void exportarDados(Path destino) throws IOException {
        RepositorioCsv exportRepo = new RepositorioCsv(destino);
        exportRepo.salvarAtivos(ativos);
        exportRepo.salvarLocalizacoes(localizacoes);
        exportRepo.salvarContadores(contadores);
        exportRepo.salvarDocumentos(documentos);
        exportRepo.salvarOTs(ots);
        exportRepo.salvarTarefasOT(tarefas);
        exportRepo.salvarConsumos(consumos);
        exportRepo.salvarExecucoes(execucoes);
        exportRepo.salvarHistoricoOT(historicoOT);
        exportRepo.salvarPlanos(planos);
        exportRepo.salvarGatilhos(gatilhos);
        exportRepo.salvarPecas(pecas);
        exportRepo.salvarStock(stocks);
        exportRepo.salvarMovimentos(movimentos);
        exportRepo.salvarInventarios(inventarios);
        exportRepo.salvarFornecedores(fornecedores);
        exportRepo.salvarUtilizadores(utilizadores);
        exportRepo.salvarAuditoria(auditoria);
        exportRepo.salvarPedidos(pedidos);
        exportRepo.salvarNotificacoes(notificacoes);
        exportRepo.salvarParametros(parametros);
        exportRepo.salvarDashboards(dashboards);
        exportRepo.salvarVistas(vistas);
    }

    public void importarContadores(Path ficheiroCsv) throws IOException {
        List<String> linhas = Files.readAllLines(ficheiroCsv);
        for (String linha : linhas) {
            if (linha.isBlank()) continue;
            String[] f = linha.split(";", -1);
            if (f.length < 3) continue;
            String ativoCodigo = f[0];
            try {
                Contador.Tipo tipo = Contador.Tipo.valueOf(f[1].trim().toUpperCase());
                double leitura = Double.parseDouble(f[2]);
                String unidade = f.length > 3 ? f[3] : "h";
                atualizarContador(ativoCodigo, tipo, leitura, unidade);
            } catch (Exception e) {
                AppLogger.get().log(Level.WARNING, "Linha invalida em importacao de contadores: " + linha, e);
            }
        }
    }

    public void importarAtivos(Path ficheiroCsv) throws IOException {
        List<String> linhas = Files.readAllLines(ficheiroCsv);
        for (String linha : linhas) {
            if (linha.isBlank()) continue;
            String[] f = pt.escnaval.exercicios.manutencao.utils.CsvUtils.split(linha);
            String codigo = pt.escnaval.exercicios.manutencao.utils.CsvUtils.get(f, 0);
            String nome = pt.escnaval.exercicios.manutencao.utils.CsvUtils.get(f, 1);
            String rawEstado = pt.escnaval.exercicios.manutencao.utils.CsvUtils.get(f, 2);
            EstadoAtivo estado = EstadoAtivo.ATIVO;
            int criticidade = 1;
            if (rawEstado != null && !rawEstado.isBlank()) {
                try { estado = EstadoAtivo.valueOf(rawEstado.trim().toUpperCase()); }
                catch (IllegalArgumentException e) { criticidade = parseInt(rawEstado, 1); }
            }
            if (f.length > 3) criticidade = parseInt(pt.escnaval.exercicios.manutencao.utils.CsvUtils.get(f, 3), criticidade);
            String idPai = f.length > 4 ? f[4] : null;
            String idLoc = f.length > 5 ? f[5] : null;
            Ativo existente = buscarAtivo(codigo);
            if (existente == null) {
                ativos.add(new Ativo(codigo, nome, estado, criticidade, emptyToNull(idPai), emptyToNull(idLoc)));
            } else {
                existente.setNome(nome);
                existente.setEstado(estado);
                existente.setCriticidade(criticidade);
            }
        }
    }

    public void importarPecas(Path ficheiroCsv) throws IOException {
        List<String> linhas = Files.readAllLines(ficheiroCsv);
        for (String linha : linhas) {
            if (linha.isBlank()) continue;
            String[] f = pt.escnaval.exercicios.manutencao.utils.CsvUtils.split(linha);
            String sku = pt.escnaval.exercicios.manutencao.utils.CsvUtils.get(f, 0);
            String designacao = pt.escnaval.exercicios.manutencao.utils.CsvUtils.get(f, 1);
            String unidade = pt.escnaval.exercicios.manutencao.utils.CsvUtils.get(f, 2);
            int repos = parseInt(pt.escnaval.exercicios.manutencao.utils.CsvUtils.get(f, 3), 0);
            double custo = parseDouble(pt.escnaval.exercicios.manutencao.utils.CsvUtils.get(f, 4), 0.0);
            Peca p = buscarPeca(sku);
            if (p == null) {
                pecas.add(new Peca(sku, designacao, unidade, repos, custo));
            } else {
                p.setDesignacao(designacao);
                p.setUnidade(unidade);
                p.setPontoReposicao(repos);
                p.setCustoUnit(custo);
            }
        }
    }

    public void importarOTs(Path ficheiroCsv) throws IOException {
        List<String> linhas = Files.readAllLines(ficheiroCsv);
        for (String linha : linhas) {
            if (linha.isBlank()) continue;
            String[] f = pt.escnaval.exercicios.manutencao.utils.CsvUtils.split(linha);
            if (f.length < 6) continue;
            String id = f[0];
            OrdemTrabalho.Tipo tipo = OrdemTrabalho.Tipo.valueOf(f[1].trim().toUpperCase());
            int prioridade = parseInt(f[2], 1);
            OrdemTrabalho.Estado estado = OrdemTrabalho.Estado.valueOf(f[3].trim().toUpperCase());
            String descricao = f[4];
            String idAtivo = f[5];
            OrdemTrabalho ot = buscarOT(id);
            if (ot == null) {
                ot = new OrdemTrabalho(id, tipo, prioridade, estado, descricao, idAtivo, null, null, null, null,
                        0.0, 0.0, null, null, null, null, null, null, null, 0);
                ots.add(ot);
            } else {
                ot.setTipo(tipo);
                ot.setPrioridade(prioridade);
                ot.setEstado(estado);
                ot.setDescricao(descricao);
            }
        }
    }

    private boolean transicaoValida(OrdemTrabalho.Estado atual, OrdemTrabalho.Estado novo) {
        if (atual == OrdemTrabalho.Estado.CONCLUIDA) return false;
        if (novo == OrdemTrabalho.Estado.CANCELADA) return true;
        return switch (atual) {
            case ABERTA -> novo == OrdemTrabalho.Estado.ATRIBUIDA || novo == OrdemTrabalho.Estado.EM_ANDAMENTO;
            case ATRIBUIDA -> novo == OrdemTrabalho.Estado.EM_ANDAMENTO || novo == OrdemTrabalho.Estado.SUSPENSA;
            case EM_ANDAMENTO -> novo == OrdemTrabalho.Estado.SUSPENSA || novo == OrdemTrabalho.Estado.CONCLUIDA;
            case SUSPENSA -> novo == OrdemTrabalho.Estado.EM_ANDAMENTO;
            default -> false;
        };
    }

    private boolean existeConflitoAgenda(String tecnicoId, Instant inicio, int duracaoMin) {
        if (inicio == null) return false;
        Instant fim = inicio.plus(Duration.ofMinutes(duracaoMin));
        for (OrdemTrabalho o : ots) {
            if (!tecnicoId.equalsIgnoreCase(o.getIdTecnico())) continue;
            if (o.getDataInicio() == null) continue;
            int dur = duracaoPlaneada(o.getId());
            Instant ofim = o.getDataInicio().plus(Duration.ofMinutes(dur));
            if (intervaloSobrepoe(inicio, fim, o.getDataInicio(), ofim)) return true;
        }
        return false;
    }

    private boolean intervaloSobrepoe(Instant aInicio, Instant aFim, Instant bInicio, Instant bFim) {
        return !aFim.isBefore(bInicio) && !bFim.isBefore(aInicio);
    }

    private int duracaoPlaneada(String otId) {
        return tarefas.stream().filter(t -> t.getOtId().equalsIgnoreCase(otId)).mapToInt(TarefaOT::getDuracaoPlaneadaMin).sum();
    }

    private double totalContadorUso(String ativoCodigo) {
        return contadores.stream()
                .filter(c -> c.getAtivoCodigo().equalsIgnoreCase(ativoCodigo) && c.getTipo() == Contador.Tipo.USO)
                .mapToDouble(Contador::getLeituraAtual).sum();
    }

    private void reduzirContadoresUso(String ativoCodigo, double valor) {
        for (Contador c : contadores) {
            if (!c.getAtivoCodigo().equalsIgnoreCase(ativoCodigo) || c.getTipo() != Contador.Tipo.USO) continue;
            double novo = c.getLeituraAtual() - valor;
            c.setLeituraAtual(Math.max(0, novo));
            break;
        }
    }

    private List<RegraGatilho> gatilhosDoPlano(String planoId) {
        return gatilhos.stream().filter(g -> g.getPlanoId().equalsIgnoreCase(planoId)).collect(Collectors.toList());
    }

    private void validarCriticidade(int valor) {
        if (valor < 1 || valor > 5) throw new IllegalArgumentException("Criticidade/prioridade deve ser 1-5");
    }

    public void aplicarRetencaoDados() {
        int diasAuditoria = parametroInt(Parametro.Tipo.RETENCAO_AUDITORIA_DIAS, 365);
        int diasExecucao = parametroInt(Parametro.Tipo.RETENCAO_EXECUCAO_DIAS, 365);
        Instant limiteAud = Instant.now().minus(Duration.ofDays(diasAuditoria));
        Instant limiteExec = Instant.now().minus(Duration.ofDays(diasExecucao));
        auditoria.removeIf(a -> a.getTimestamp().isBefore(limiteAud));
        execucoes.removeIf(e -> e.getInicio() != null && e.getInicio().isBefore(limiteExec));
        notificacoes.removeIf(n -> n.getData() != null && n.getData().isBefore(limiteAud));
        auditar("Retencao", "sistema", "aplicada", null, "auditoria=" + diasAuditoria + " execucao=" + diasExecucao);
    }

    public void atualizarParametro(String id, String codigo, String descricao) {
        Parametro p = buscarParametro(id);
        if (p == null) throw new IllegalArgumentException("Parametro nao encontrado");
        if (codigo != null) {
            String novo = codigo.trim();
            if (novo.isEmpty()) throw new IllegalArgumentException("Codigo invalido");
            boolean existe = parametros.stream().anyMatch(x -> x != p && x.getTipo() == p.getTipo()
                    && x.getCodigo().equalsIgnoreCase(novo));
            if (existe) throw new IllegalArgumentException("Codigo ja existe para este tipo");
            if (!novo.equals(p.getCodigo())) {
                auditar("Parametro", id, "codigo", p.getCodigo(), novo);
                p.setCodigo(novo);
            }
        }
        if (descricao != null) {
            String novo = descricao.trim();
            if (novo.isEmpty()) throw new IllegalArgumentException("Descricao invalida");
            if (!novo.equals(p.getDescricao())) {
                auditar("Parametro", id, "descricao", p.getDescricao(), novo);
                p.setDescricao(novo);
            }
        }
    }

    public void anonimizarUtilizador(String id) {
        Utilizador u = obterUtilizador(id);
        if (u == null) throw new IllegalArgumentException("Utilizador nao encontrado");
        String antigo = u.getEmail();
        u.setEmail("anonimizado@" + id);
        u.setNome("Utilizador " + id);
        u.setEstado(EstadoUtilizador.INATIVO);
        auditar("Utilizador", id, "anonimizado", antigo, u.getEmail());
    }

    private Notificacao criarNotificacao(String tipo, String mensagem) {
        Notificacao n = new Notificacao(novoId(), tipo, mensagem, Instant.now(), Notificacao.Estado.NOVA);
        notificacoes.add(n);
        return n;
    }

    private void auditar(String entidade, String idEntidade, String campo, String antigo, String novo) {
        String uid = idUtilizadorAtual();
        auditoria.add(new Auditoria(novoId(), entidade, idEntidade, campo, antigo, novo, Instant.now(), uid));
    }

    private String idUtilizadorAtual() {
        return utilizadorAtual == null ? "system" : utilizadorAtual.getId();
    }

    private String novoId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    private <T> T executarTransacao(Supplier<T> acao) {
        Path txDir = Path.of("data", "tx_" + System.currentTimeMillis());
        try {
            exportarDados(txDir);
            T res = acao.get();
            return res;
        } catch (Exception e) {
            try {
                restaurarDe(txDir);
            } catch (Exception ex) {
                AppLogger.get().log(Level.SEVERE, "Falha ao restaurar transacao", ex);
            }
            if (e instanceof RuntimeException re) throw re;
            throw new RuntimeException(e);
        }
    }

    private void restaurarDe(Path dir) throws IOException {
        RepositorioCsv r = new RepositorioCsv(dir);
        ativos.clear();
        localizacoes.clear();
        contadores.clear();
        documentos.clear();
        ots.clear();
        tarefas.clear();
        consumos.clear();
        execucoes.clear();
        historicoOT.clear();
        planos.clear();
        gatilhos.clear();
        pecas.clear();
        stocks.clear();
        movimentos.clear();
        inventarios.clear();
        fornecedores.clear();
        utilizadores.clear();
        auditoria.clear();
        pedidos.clear();
        notificacoes.clear();
        parametros.clear();
        dashboards.clear();
        vistas.clear();

        ativos.addAll(r.carregarAtivos());
        localizacoes.addAll(r.carregarLocalizacoes());
        contadores.addAll(r.carregarContadores());
        documentos.addAll(r.carregarDocumentos());
        ots.addAll(r.carregarOTs());
        tarefas.addAll(r.carregarTarefasOT());
        consumos.addAll(r.carregarConsumos());
        execucoes.addAll(r.carregarExecucoes());
        historicoOT.addAll(r.carregarHistoricoOT());
        planos.addAll(r.carregarPlanos());
        gatilhos.addAll(r.carregarGatilhos());
        pecas.addAll(r.carregarPecas());
        stocks.addAll(r.carregarStock());
        movimentos.addAll(r.carregarMovimentos());
        inventarios.addAll(r.carregarInventarios());
        fornecedores.addAll(r.carregarFornecedores());
        utilizadores.addAll(r.carregarUtilizadores());
        auditoria.addAll(r.carregarAuditoria());
        pedidos.addAll(r.carregarPedidos());
        notificacoes.addAll(r.carregarNotificacoes());
        parametros.addAll(r.carregarParametros());
        dashboards.addAll(r.carregarDashboards());
        vistas.addAll(r.carregarVistas());
    }

    private int parametroInt(Parametro.Tipo tipo, int def) {
        for (Parametro p : parametros) {
            if (p.getTipo() == tipo) {
                try { return Integer.parseInt(p.getDescricao()); }
                catch (NumberFormatException e) { return def; }
            }
        }
        return def;
    }

    private Integer slaPorPrioridade(int prioridade) {
        for (Parametro p : parametros) {
            if (p.getTipo() == Parametro.Tipo.SLA_PRIORIDADE && p.getCodigo().equals(Integer.toString(prioridade))) {
                try { return Integer.parseInt(p.getDescricao()); }
                catch (NumberFormatException e) { return null; }
            }
        }
        return null;
    }

    private int parseInt(String value, int def) {
        if (value == null || value.isBlank()) return def;
        try { return Integer.parseInt(value); }
        catch (NumberFormatException e) { return def; }
    }

    private double parseDouble(String value, double def) {
        if (value == null || value.isBlank()) return def;
        try { return Double.parseDouble(value); }
        catch (NumberFormatException e) { return def; }
    }

    private String emptyToNull(String value) {
        return (value == null || value.isBlank()) ? null : value;
    }

    private boolean dentroTurno(Utilizador u, Instant inicio) {
        if (u.getTurnoInicio() == null || u.getTurnoFim() == null) return true;
        java.time.ZonedDateTime zdt = inicio.atZone(java.time.ZoneId.systemDefault());
        int hora = zdt.getHour();
        int ini = u.getTurnoInicio();
        int fim = u.getTurnoFim();
        if (ini == fim) return true;
        if (ini < fim) return hora >= ini && hora < fim;
        return hora >= ini || hora < fim;
    }
}
