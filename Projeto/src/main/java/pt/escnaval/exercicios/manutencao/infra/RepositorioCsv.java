package pt.escnaval.exercicios.manutencao.infra;

import pt.escnaval.exercicios.manutencao.modelo.*;
import pt.escnaval.exercicios.manutencao.utils.CsvUtils;
import pt.escnaval.exercicios.manutencao.utils.DateUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class RepositorioCsv {
    private final Path baseDir;

    public RepositorioCsv(Path baseDir) {
        this.baseDir = baseDir;
    }

    public List<Ativo> carregarAtivos() throws IOException {
        List<String> linhas = readAll(file("ativos.csv"));
        List<Ativo> out = new ArrayList<>();
        for (String linha : linhas) {
            if (linha.isBlank()) continue;
            String[] f = CsvUtils.split(linha);
            String codigo = CsvUtils.get(f, 0);
            String nome = CsvUtils.get(f, 1);
            String rawEstado = CsvUtils.get(f, 2);
            EstadoAtivo estado = parseEnum(EstadoAtivo.class, rawEstado, EstadoAtivo.ATIVO);
            int criticidade = CsvUtils.getInt(f, 3, 1);
            if (f.length <= 3 && isInt(rawEstado)) {
                criticidade = Integer.parseInt(rawEstado);
            }
            String idPai = CsvUtils.get(f, 4);
            String idLoc = CsvUtils.get(f, 5);
            out.add(new Ativo(codigo, nome, estado, criticidade, emptyToNull(idPai), emptyToNull(idLoc)));
        }
        return out;
    }

    public void salvarAtivos(List<Ativo> ativos) throws IOException {
        List<String> linhas = new ArrayList<>();
        for (Ativo a : ativos) {
            linhas.add(join(a.getCodigo(), a.getNome(), a.getEstado().name(),
                    Integer.toString(a.getCriticidade()), nullToEmpty(a.getIdAtivoPai()), nullToEmpty(a.getIdLocalizacao())));
        }
        writeAtomic(file("ativos.csv"), linhas);
    }

    public List<Localizacao> carregarLocalizacoes() throws IOException {
        List<String> linhas = readAll(file("localizacoes.csv"));
        List<Localizacao> out = new ArrayList<>();
        for (String linha : linhas) {
            if (linha.isBlank()) continue;
            String[] f = CsvUtils.split(linha);
            out.add(new Localizacao(CsvUtils.get(f, 0), CsvUtils.get(f, 1), emptyToNull(CsvUtils.get(f, 2))));
        }
        return out;
    }

    public void salvarLocalizacoes(List<Localizacao> lista) throws IOException {
        List<String> linhas = new ArrayList<>();
        for (Localizacao l : lista) {
            linhas.add(join(l.getId(), l.getNome(), nullToEmpty(l.getIdPai())));
        }
        writeAtomic(file("localizacoes.csv"), linhas);
    }

    public List<Contador> carregarContadores() throws IOException {
        List<String> linhas = readAll(file("contadores.csv"));
        List<Contador> out = new ArrayList<>();
        for (String linha : linhas) {
            if (linha.isBlank()) continue;
            String[] f = CsvUtils.split(linha);
            Instant at = DateUtils.parseInstant(CsvUtils.get(f, 5));
            out.add(new Contador(CsvUtils.get(f, 0), CsvUtils.get(f, 1),
                    parseEnum(Contador.Tipo.class, CsvUtils.get(f, 2), Contador.Tipo.TEMPO),
                    CsvUtils.getDouble(f, 3, 0.0), CsvUtils.get(f, 4), at));
        }
        return out;
    }

    public void salvarContadores(List<Contador> lista) throws IOException {
        List<String> linhas = new ArrayList<>();
        for (Contador c : lista) {
            linhas.add(join(c.getId(), c.getAtivoCodigo(), c.getTipo().name(),
                    Double.toString(c.getLeituraAtual()), c.getUnidade(), DateUtils.formatInstant(c.getAtualizadoEm())));
        }
        writeAtomic(file("contadores.csv"), linhas);
    }

    public List<Documento> carregarDocumentos() throws IOException {
        List<String> linhas = readAll(file("documentos.csv"));
        List<Documento> out = new ArrayList<>();
        for (String linha : linhas) {
            if (linha.isBlank()) continue;
            String[] f = CsvUtils.split(linha);
            out.add(new Documento(CsvUtils.get(f, 0), CsvUtils.get(f, 1), CsvUtils.get(f, 2),
                    CsvUtils.get(f, 3), CsvUtils.get(f, 4), CsvUtils.get(f, 5)));
        }
        return out;
    }

    public void salvarDocumentos(List<Documento> lista) throws IOException {
        List<String> linhas = new ArrayList<>();
        for (Documento d : lista) {
            linhas.add(join(d.getId(), d.getEntidade(), d.getEntidadeId(), d.getTipo(), d.getPath(), nullToEmpty(d.getMeta())));
        }
        writeAtomic(file("documentos.csv"), linhas);
    }

    public List<OrdemTrabalho> carregarOTs() throws IOException {
        List<String> linhas = readAll(file("ots.csv"));
        List<OrdemTrabalho> out = new ArrayList<>();
        for (String linha : linhas) {
            if (linha.isBlank()) continue;
            String[] f = CsvUtils.split(linha);
            String id = CsvUtils.get(f, 0);
            OrdemTrabalho.Tipo tipo = parseEnum(OrdemTrabalho.Tipo.class, CsvUtils.get(f, 1), OrdemTrabalho.Tipo.CORRETIVA);
            int prioridade = CsvUtils.getInt(f, 2, 1);
            OrdemTrabalho.Estado estado = parseEnum(OrdemTrabalho.Estado.class, CsvUtils.get(f, 3), OrdemTrabalho.Estado.ABERTA);
            String descricao = CsvUtils.get(f, 4);
            String idAtivo = CsvUtils.get(f, 5);
            Instant dataCriacao = null;
            Instant dataInicio = null;
            Instant dataFim = null;
            Instant dataLimite = null;
            double custo = 0.0;
            double custoPrevisto = 0.0;
            String idTecnico = null;
            Integer slaHoras = null;
            String idPedido = null;
            String categoria = null;
            String centro = null;
            String fornecedorId = null;
            String ordemExterna = null;
            int tempo = 0;
            if (f.length <= 7) {
                tempo = CsvUtils.getInt(f, 6, 0);
            } else {
                dataCriacao = DateUtils.parseInstant(CsvUtils.get(f, 6));
                dataInicio = DateUtils.parseInstant(CsvUtils.get(f, 7));
                dataFim = DateUtils.parseInstant(CsvUtils.get(f, 8));
                dataLimite = DateUtils.parseInstant(CsvUtils.get(f, 9));
                custo = CsvUtils.getDouble(f, 10, 0.0);
                custoPrevisto = CsvUtils.getDouble(f, 11, 0.0);
                idTecnico = emptyToNull(CsvUtils.get(f, 12));
                slaHoras = parseIntNullable(CsvUtils.get(f, 13));
                idPedido = emptyToNull(CsvUtils.get(f, 14));
                categoria = emptyToNull(CsvUtils.get(f, 15));
                centro = emptyToNull(CsvUtils.get(f, 16));
                fornecedorId = emptyToNull(CsvUtils.get(f, 17));
                ordemExterna = emptyToNull(CsvUtils.get(f, 18));
                tempo = CsvUtils.getInt(f, 19, 0);
            }
            OrdemTrabalho ot = new OrdemTrabalho(id, tipo, prioridade, estado, descricao, idAtivo, dataCriacao,
                    dataInicio, dataFim, dataLimite, custo, custoPrevisto, idTecnico, slaHoras, idPedido, categoria,
                    centro, fornecedorId, ordemExterna, tempo);
            out.add(ot);
        }
        return out;
    }

    public void salvarOTs(List<OrdemTrabalho> ots) throws IOException {
        List<String> linhas = new ArrayList<>();
        for (OrdemTrabalho o : ots) {
            linhas.add(join(o.getId(), o.getTipo().name(), Integer.toString(o.getPrioridade()), o.getEstado().name(),
                    o.getDescricao(), o.getIdAtivo(), DateUtils.formatInstant(o.getDataCriacao()),
                    DateUtils.formatInstant(o.getDataInicio()), DateUtils.formatInstant(o.getDataFim()),
                    DateUtils.formatInstant(o.getDataLimite()), Double.toString(o.getCustoTotal()),
                    Double.toString(o.getCustoPrevisto()), nullToEmpty(o.getIdTecnico()),
                    o.getSlaHoras() == null ? "" : Integer.toString(o.getSlaHoras()), nullToEmpty(o.getIdPedido()),
                    nullToEmpty(o.getCategoriaFalha()), nullToEmpty(o.getCentroCusto()),
                    nullToEmpty(o.getFornecedorId()), nullToEmpty(o.getOrdemExterna()),
                    Integer.toString(o.getTempoGastoTotalMin())));
        }
        writeAtomic(file("ots.csv"), linhas);
    }

    public List<TarefaOT> carregarTarefasOT() throws IOException {
        List<String> linhas = readAll(file("ot_tarefas.csv"));
        List<TarefaOT> out = new ArrayList<>();
        for (String linha : linhas) {
            if (linha.isBlank()) continue;
            String[] f = CsvUtils.split(linha);
            out.add(new TarefaOT(CsvUtils.get(f, 0), CsvUtils.get(f, 1), CsvUtils.get(f, 2), CsvUtils.getInt(f, 3, 0)));
        }
        return out;
    }

    public void salvarTarefasOT(List<TarefaOT> lista) throws IOException {
        List<String> linhas = new ArrayList<>();
        for (TarefaOT t : lista) {
            linhas.add(join(t.getId(), t.getOtId(), t.getDescricao(), Integer.toString(t.getDuracaoPlaneadaMin())));
        }
        writeAtomic(file("ot_tarefas.csv"), linhas);
    }

    public List<ConsumoPeca> carregarConsumos() throws IOException {
        List<String> linhas = readAll(file("ot_consumos.csv"));
        List<ConsumoPeca> out = new ArrayList<>();
        for (String linha : linhas) {
            if (linha.isBlank()) continue;
            String[] f = CsvUtils.split(linha);
            out.add(new ConsumoPeca(CsvUtils.get(f, 0), CsvUtils.get(f, 1), CsvUtils.get(f, 2),
                    CsvUtils.getInt(f, 3, 0), CsvUtils.getDouble(f, 4, 0.0)));
        }
        return out;
    }

    public void salvarConsumos(List<ConsumoPeca> lista) throws IOException {
        List<String> linhas = new ArrayList<>();
        for (ConsumoPeca c : lista) {
            linhas.add(join(c.getId(), c.getOtId(), c.getSku(), Integer.toString(c.getQuantidade()),
                    Double.toString(c.getCustoUnit())));
        }
        writeAtomic(file("ot_consumos.csv"), linhas);
    }

    public List<RegistoExecucao> carregarExecucoes() throws IOException {
        List<String> linhas = readAll(file("ot_execucoes.csv"));
        List<RegistoExecucao> out = new ArrayList<>();
        for (String linha : linhas) {
            if (linha.isBlank()) continue;
            String[] f = CsvUtils.split(linha);
            out.add(new RegistoExecucao(CsvUtils.get(f, 0), CsvUtils.get(f, 1),
                    DateUtils.parseInstant(CsvUtils.get(f, 2)), DateUtils.parseInstant(CsvUtils.get(f, 3)),
                    CsvUtils.get(f, 4), CsvUtils.get(f, 5), emptyToNull(CsvUtils.get(f, 6)), emptyToNull(CsvUtils.get(f, 7))));
        }
        return out;
    }

    public void salvarExecucoes(List<RegistoExecucao> lista) throws IOException {
        List<String> linhas = new ArrayList<>();
        for (RegistoExecucao r : lista) {
            linhas.add(join(r.getId(), r.getOtId(), DateUtils.formatInstant(r.getInicio()), DateUtils.formatInstant(r.getFim()),
                    nullToEmpty(r.getCausa()), nullToEmpty(r.getAcao()), nullToEmpty(r.getTecnicoId()), nullToEmpty(r.getObservacoes())));
        }
        writeAtomic(file("ot_execucoes.csv"), linhas);
    }

    public List<HistoricoEstadoOT> carregarHistoricoOT() throws IOException {
        List<String> linhas = readAll(file("ot_historico.csv"));
        List<HistoricoEstadoOT> out = new ArrayList<>();
        for (String linha : linhas) {
            if (linha.isBlank()) continue;
            String[] f = CsvUtils.split(linha);
            out.add(new HistoricoEstadoOT(CsvUtils.get(f, 0), CsvUtils.get(f, 1),
                    parseEnum(OrdemTrabalho.Estado.class, CsvUtils.get(f, 2), OrdemTrabalho.Estado.ABERTA),
                    DateUtils.parseInstant(CsvUtils.get(f, 3)), emptyToNull(CsvUtils.get(f, 4))));
        }
        return out;
    }

    public void salvarHistoricoOT(List<HistoricoEstadoOT> lista) throws IOException {
        List<String> linhas = new ArrayList<>();
        for (HistoricoEstadoOT h : lista) {
            linhas.add(join(h.getId(), h.getOtId(), h.getEstado().name(), DateUtils.formatInstant(h.getData()), nullToEmpty(h.getUtilizadorId())));
        }
        writeAtomic(file("ot_historico.csv"), linhas);
    }

    public List<PlanoPreventivo> carregarPlanos() throws IOException {
        List<String> linhas = readAll(file("planos.csv"));
        List<PlanoPreventivo> out = new ArrayList<>();
        for (String linha : linhas) {
            if (linha.isBlank()) continue;
            String[] f = CsvUtils.split(linha);
            out.add(new PlanoPreventivo(CsvUtils.get(f, 0), CsvUtils.get(f, 1),
                    parseEnum(PlanoPreventivo.Politica.class, CsvUtils.get(f, 2), PlanoPreventivo.Politica.TEMPO),
                    CsvUtils.getInt(f, 3, 0), CsvUtils.getInt(f, 4, 0), DateUtils.parseInstant(CsvUtils.get(f, 5))));
        }
        return out;
    }

    public void salvarPlanos(List<PlanoPreventivo> lista) throws IOException {
        List<String> linhas = new ArrayList<>();
        for (PlanoPreventivo p : lista) {
            linhas.add(join(p.getId(), p.getAtivoCodigo(), p.getPolitica().name(),
                    Integer.toString(p.getPeriodicidadeDias()), Integer.toString(p.getJanelaDias()),
                    DateUtils.formatInstant(p.getUltimoDisparo())));
        }
        writeAtomic(file("planos.csv"), linhas);
    }

    public List<RegraGatilho> carregarGatilhos() throws IOException {
        List<String> linhas = readAll(file("planos_gatilhos.csv"));
        List<RegraGatilho> out = new ArrayList<>();
        for (String linha : linhas) {
            if (linha.isBlank()) continue;
            String[] f = CsvUtils.split(linha);
            out.add(new RegraGatilho(CsvUtils.get(f, 0), CsvUtils.get(f, 1),
                    parseEnum(RegraGatilho.Tipo.class, CsvUtils.get(f, 2), RegraGatilho.Tipo.TEMPO),
                    CsvUtils.getDouble(f, 3, 0.0), CsvUtils.get(f, 4)));
        }
        return out;
    }

    public void salvarGatilhos(List<RegraGatilho> lista) throws IOException {
        List<String> linhas = new ArrayList<>();
        for (RegraGatilho g : lista) {
            linhas.add(join(g.getId(), g.getPlanoId(), g.getTipo().name(), Double.toString(g.getValor()), g.getUnidade()));
        }
        writeAtomic(file("planos_gatilhos.csv"), linhas);
    }

    public List<Peca> carregarPecas() throws IOException {
        List<String> linhas = readAll(file("pecas.csv"));
        List<Peca> out = new ArrayList<>();
        for (String linha : linhas) {
            if (linha.isBlank()) continue;
            String[] f = CsvUtils.split(linha);
            out.add(new Peca(CsvUtils.get(f, 0), CsvUtils.get(f, 1), CsvUtils.get(f, 2),
                    CsvUtils.getInt(f, 3, 0), CsvUtils.getDouble(f, 4, 0.0)));
        }
        return out;
    }

    public void salvarPecas(List<Peca> lista) throws IOException {
        List<String> linhas = new ArrayList<>();
        for (Peca p : lista) {
            linhas.add(join(p.getSku(), p.getDesignacao(), p.getUnidade(),
                    Integer.toString(p.getPontoReposicao()), Double.toString(p.getCustoUnit())));
        }
        writeAtomic(file("pecas.csv"), linhas);
    }

    public List<Stock> carregarStock() throws IOException {
        List<String> linhas = readAll(file("stock.csv"));
        List<Stock> out = new ArrayList<>();
        for (String linha : linhas) {
            if (linha.isBlank()) continue;
            String[] f = CsvUtils.split(linha);
            out.add(new Stock(CsvUtils.get(f, 0), CsvUtils.get(f, 1), CsvUtils.get(f, 2), CsvUtils.getInt(f, 3, 0)));
        }
        return out;
    }

    public void salvarStock(List<Stock> lista) throws IOException {
        List<String> linhas = new ArrayList<>();
        for (Stock s : lista) {
            linhas.add(join(s.getId(), s.getSku(), s.getDeposito(), Integer.toString(s.getQuantidade())));
        }
        writeAtomic(file("stock.csv"), linhas);
    }

    public List<MovimentoStock> carregarMovimentos() throws IOException {
        List<String> linhas = readAll(file("movimentos.csv"));
        List<MovimentoStock> out = new ArrayList<>();
        for (String linha : linhas) {
            if (linha.isBlank()) continue;
            String[] f = CsvUtils.split(linha);
            out.add(new MovimentoStock(CsvUtils.get(f, 0), CsvUtils.get(f, 1), CsvUtils.get(f, 2),
                    parseEnum(MovimentoStock.Tipo.class, CsvUtils.get(f, 3), MovimentoStock.Tipo.ENTRADA),
                    DateUtils.parseInstant(CsvUtils.get(f, 4)), CsvUtils.getInt(f, 5, 0), emptyToNull(CsvUtils.get(f, 6))));
        }
        return out;
    }

    public void salvarMovimentos(List<MovimentoStock> lista) throws IOException {
        List<String> linhas = new ArrayList<>();
        for (MovimentoStock m : lista) {
            linhas.add(join(m.getId(), m.getSku(), m.getDeposito(), m.getTipo().name(),
                    DateUtils.formatInstant(m.getData()), Integer.toString(m.getQuantidade()), nullToEmpty(m.getOtId())));
        }
        writeAtomic(file("movimentos.csv"), linhas);
    }

    public List<InventarioCiclico> carregarInventarios() throws IOException {
        List<String> linhas = readAll(file("inventarios.csv"));
        List<InventarioCiclico> out = new ArrayList<>();
        for (String linha : linhas) {
            if (linha.isBlank()) continue;
            String[] f = CsvUtils.split(linha);
            out.add(new InventarioCiclico(CsvUtils.get(f, 0), CsvUtils.get(f, 1), CsvUtils.get(f, 2),
                    CsvUtils.getInt(f, 3, 0), DateUtils.parseInstant(CsvUtils.get(f, 4)), emptyToNull(CsvUtils.get(f, 5))));
        }
        return out;
    }

    public void salvarInventarios(List<InventarioCiclico> lista) throws IOException {
        List<String> linhas = new ArrayList<>();
        for (InventarioCiclico i : lista) {
            linhas.add(join(i.getId(), i.getSku(), i.getDeposito(), Integer.toString(i.getQuantidadeContada()),
                    DateUtils.formatInstant(i.getData()), nullToEmpty(i.getUtilizadorId())));
        }
        writeAtomic(file("inventarios.csv"), linhas);
    }

    public List<Fornecedor> carregarFornecedores() throws IOException {
        List<String> linhas = readAll(file("fornecedores.csv"));
        List<Fornecedor> out = new ArrayList<>();
        for (String linha : linhas) {
            if (linha.isBlank()) continue;
            String[] f = CsvUtils.split(linha);
            out.add(new Fornecedor(CsvUtils.get(f, 0), CsvUtils.get(f, 1),
                    emptyToNull(CsvUtils.get(f, 2)), emptyToNull(CsvUtils.get(f, 3)),
                    parseIntNullable(CsvUtils.get(f, 4))));
        }
        return out;
    }

    public void salvarFornecedores(List<Fornecedor> lista) throws IOException {
        List<String> linhas = new ArrayList<>();
        for (Fornecedor f : lista) {
            linhas.add(join(f.getId(), f.getNome(), nullToEmpty(f.getContacto()), nullToEmpty(f.getEmail()),
                    f.getSlaHorasPadrao() == null ? "" : Integer.toString(f.getSlaHorasPadrao())));
        }
        writeAtomic(file("fornecedores.csv"), linhas);
    }

    public List<DashboardConfig> carregarDashboards() throws IOException {
        List<String> linhas = readAll(file("dashboard.csv"));
        List<DashboardConfig> out = new ArrayList<>();
        for (String linha : linhas) {
            if (linha.isBlank()) continue;
            String[] f = CsvUtils.split(linha);
            out.add(new DashboardConfig(CsvUtils.get(f, 0),
                    parseEnum(Utilizador.Perfil.class, CsvUtils.get(f, 1), Utilizador.Perfil.TECNICO),
                    CsvUtils.get(f, 2), parseIntNullable(CsvUtils.get(f, 3))));
        }
        return out;
    }

    public void salvarDashboards(List<DashboardConfig> lista) throws IOException {
        List<String> linhas = new ArrayList<>();
        for (DashboardConfig d : lista) {
            linhas.add(join(d.getId(), d.getPerfil().name(), d.getWidgets(),
                    d.getPageSize() == null ? "" : Integer.toString(d.getPageSize())));
        }
        writeAtomic(file("dashboard.csv"), linhas);
    }

    public List<VistaPesquisa> carregarVistas() throws IOException {
        List<String> linhas = readAll(file("vistas.csv"));
        List<VistaPesquisa> out = new ArrayList<>();
        for (String linha : linhas) {
            if (linha.isBlank()) continue;
            String[] f = CsvUtils.split(linha);
            out.add(new VistaPesquisa(CsvUtils.get(f, 0), CsvUtils.get(f, 1), CsvUtils.get(f, 2),
                    emptyToNull(CsvUtils.get(f, 3)), emptyToNull(CsvUtils.get(f, 4)), parseIntNullable(CsvUtils.get(f, 5))));
        }
        return out;
    }

    public void salvarVistas(List<VistaPesquisa> lista) throws IOException {
        List<String> linhas = new ArrayList<>();
        for (VistaPesquisa v : lista) {
            linhas.add(join(v.getId(), v.getUtilizadorId(), v.getEntidade(), nullToEmpty(v.getFiltro()),
                    nullToEmpty(v.getOrdenacao()), v.getPageSize() == null ? "" : Integer.toString(v.getPageSize())));
        }
        writeAtomic(file("vistas.csv"), linhas);
    }

    public List<Utilizador> carregarUtilizadores() throws IOException {
        List<String> linhas = readAll(file("utilizadores.csv"));
        List<Utilizador> out = new ArrayList<>();
        for (String linha : linhas) {
            if (linha.isBlank()) continue;
            String[] f = CsvUtils.split(linha);
            EstadoUtilizador estado = parseEnum(EstadoUtilizador.class, CsvUtils.get(f, 5), EstadoUtilizador.ATIVO);
            Integer turnoInicio = parseIntNullable(CsvUtils.get(f, 7));
            Integer turnoFim = parseIntNullable(CsvUtils.get(f, 8));
            out.add(new Utilizador(CsvUtils.get(f, 0), CsvUtils.get(f, 1), CsvUtils.get(f, 2),
                    parseEnum(Utilizador.Perfil.class, CsvUtils.get(f, 3), Utilizador.Perfil.TECNICO),
                    CsvUtils.get(f, 4), estado, emptyToNull(CsvUtils.get(f, 6)), turnoInicio, turnoFim));
        }
        return out;
    }

    public void salvarUtilizadores(List<Utilizador> lista) throws IOException {
        List<String> linhas = new ArrayList<>();
        for (Utilizador u : lista) {
            linhas.add(join(u.getId(), u.getNome(), u.getEmail(), u.getPerfil().name(), u.getPasswordHash(),
                    u.getEstado().name(), nullToEmpty(u.getEquipa()),
                    u.getTurnoInicio() == null ? "" : Integer.toString(u.getTurnoInicio()),
                    u.getTurnoFim() == null ? "" : Integer.toString(u.getTurnoFim())));
        }
        writeAtomic(file("utilizadores.csv"), linhas);
    }

    public List<Auditoria> carregarAuditoria() throws IOException {
        List<String> linhas = readAll(file("auditoria.csv"));
        List<Auditoria> out = new ArrayList<>();
        for (String linha : linhas) {
            if (linha.isBlank()) continue;
            String[] f = CsvUtils.split(linha);
            out.add(new Auditoria(CsvUtils.get(f, 0), CsvUtils.get(f, 1), CsvUtils.get(f, 2), CsvUtils.get(f, 3),
                    CsvUtils.get(f, 4), CsvUtils.get(f, 5), DateUtils.parseInstant(CsvUtils.get(f, 6)),
                    CsvUtils.get(f, 7)));
        }
        return out;
    }

    public void salvarAuditoria(List<Auditoria> lista) throws IOException {
        List<String> linhas = new ArrayList<>();
        for (Auditoria a : lista) {
            linhas.add(join(a.getId(), a.getEntidade(), a.getIdEntidade(), a.getCampo(),
                    nullToEmpty(a.getValorAntigo()), nullToEmpty(a.getValorNovo()),
                    DateUtils.formatInstant(a.getTimestamp()), a.getUtilizadorId()));
        }
        writeAtomic(file("auditoria.csv"), linhas);
    }

    public List<PedidoManutencao> carregarPedidos() throws IOException {
        List<String> linhas = readAll(file("pedidos.csv"));
        List<PedidoManutencao> out = new ArrayList<>();
        for (String linha : linhas) {
            if (linha.isBlank()) continue;
            String[] f = CsvUtils.split(linha);
            out.add(new PedidoManutencao(CsvUtils.get(f, 0), CsvUtils.get(f, 1), CsvUtils.get(f, 2),
                    parseEnum(PedidoManutencao.Estado.class, CsvUtils.get(f, 3), PedidoManutencao.Estado.SUBMETIDO),
                    DateUtils.parseInstant(CsvUtils.get(f, 4)), emptyToNull(CsvUtils.get(f, 5)), emptyToNull(CsvUtils.get(f, 6))));
        }
        return out;
    }

    public void salvarPedidos(List<PedidoManutencao> lista) throws IOException {
        List<String> linhas = new ArrayList<>();
        for (PedidoManutencao p : lista) {
            linhas.add(join(p.getId(), p.getAtivoCodigo(), p.getDescricao(), p.getEstado().name(),
                    DateUtils.formatInstant(p.getData()), nullToEmpty(p.getSolicitanteId()), nullToEmpty(p.getAprovadorId())));
        }
        writeAtomic(file("pedidos.csv"), linhas);
    }

    public List<Notificacao> carregarNotificacoes() throws IOException {
        List<String> linhas = readAll(file("notificacoes.csv"));
        List<Notificacao> out = new ArrayList<>();
        for (String linha : linhas) {
            if (linha.isBlank()) continue;
            String[] f = CsvUtils.split(linha);
            out.add(new Notificacao(CsvUtils.get(f, 0), CsvUtils.get(f, 1), CsvUtils.get(f, 2),
                    DateUtils.parseInstant(CsvUtils.get(f, 3)),
                    parseEnum(Notificacao.Estado.class, CsvUtils.get(f, 4), Notificacao.Estado.NOVA)));
        }
        return out;
    }

    public void salvarNotificacoes(List<Notificacao> lista) throws IOException {
        List<String> linhas = new ArrayList<>();
        for (Notificacao n : lista) {
            linhas.add(join(n.getId(), n.getTipo(), n.getMensagem(), DateUtils.formatInstant(n.getData()), n.getEstado().name()));
        }
        writeAtomic(file("notificacoes.csv"), linhas);
    }

    public List<Parametro> carregarParametros() throws IOException {
        List<String> linhas = readAll(file("parametros.csv"));
        List<Parametro> out = new ArrayList<>();
        for (String linha : linhas) {
            if (linha.isBlank()) continue;
            String[] f = CsvUtils.split(linha);
            out.add(new Parametro(CsvUtils.get(f, 0),
                    parseEnum(Parametro.Tipo.class, CsvUtils.get(f, 1), Parametro.Tipo.PRIORIDADE),
                    CsvUtils.get(f, 2), CsvUtils.get(f, 3)));
        }
        return out;
    }

    public void salvarParametros(List<Parametro> lista) throws IOException {
        List<String> linhas = new ArrayList<>();
        for (Parametro p : lista) {
            linhas.add(join(p.getId(), p.getTipo().name(), p.getCodigo(), p.getDescricao()));
        }
        writeAtomic(file("parametros.csv"), linhas);
    }

    private Path file(String name) {
        return baseDir.resolve(name);
    }

    private static List<String> readAll(Path path) throws IOException {
        if (!Files.exists(path)) return List.of();
        return Files.readAllLines(path, StandardCharsets.UTF_8);
    }

    private static void writeAtomic(Path path, List<String> lines) throws IOException {
        Files.createDirectories(path.getParent());
        Path tmp = path.resolveSibling(path.getFileName().toString() + ".tmp");
        Files.write(tmp, lines, StandardCharsets.UTF_8);
        Files.move(tmp, path, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
    }

    private static String join(String... fields) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) sb.append(';');
            sb.append(CsvUtils.escape(fields[i]));
        }
        return sb.toString();
    }

    private static <E extends Enum<E>> E parseEnum(Class<E> enumClass, String value, E def) {
        if (value == null || value.isBlank()) return def;
        String normalized = value.trim().toUpperCase().replace(' ', '_');
        try {
            return Enum.valueOf(enumClass, normalized);
        } catch (IllegalArgumentException e) {
            return def;
        }
    }

    private static Integer parseIntNullable(String value) {
        if (value == null || value.isBlank()) return null;
        try { return Integer.parseInt(value); }
        catch (NumberFormatException e) { return null; }
    }

    private static String emptyToNull(String value) {
        return (value == null || value.isBlank()) ? null : value;
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    private static boolean isInt(String value) {
        if (value == null || value.isBlank()) return false;
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (i == 0 && c == '-') continue;
            if (c < '0' || c > '9') return false;
        }
        return true;
    }
}
