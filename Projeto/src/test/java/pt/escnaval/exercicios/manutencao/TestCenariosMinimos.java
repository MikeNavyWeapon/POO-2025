package pt.escnaval.exercicios.manutencao;

import pt.escnaval.exercicios.manutencao.modelo.OrdemTrabalho;
import pt.escnaval.exercicios.manutencao.modelo.PedidoManutencao;
import pt.escnaval.exercicios.manutencao.modelo.Parametro;
import pt.escnaval.exercicios.manutencao.modelo.Utilizador;
import pt.escnaval.exercicios.manutencao.servicos.ServicoManutencao;

import java.time.Instant;
import java.util.List;

public class TestCenariosMinimos {
    public static void main(String[] args) throws Exception {
        ServicoManutencao s = new ServicoManutencao();
        s.carregarTudo();

        Utilizador admin = s.autenticar("admin", "admin");
        if (admin == null) { System.err.println("FAIL: login admin"); System.exit(1); }

        String ativoCodigo = "UC-" + System.currentTimeMillis();
        s.adicionarAtivo(ativoCodigo, "Ativo UC", 4, null, null);

        String sku = "SKU-UC-" + System.currentTimeMillis();
        s.adicionarPeca(sku, "Peca UC", "UN", 2, 1.5);
        s.movimentarStock(sku, "GERAL", pt.escnaval.exercicios.manutencao.modelo.MovimentoStock.Tipo.ENTRADA, 10, null);

        String tecnicoId = "tec-uc-" + System.currentTimeMillis();
        s.adicionarUtilizador(tecnicoId, "Tecnico UC", tecnicoId + "@example.local", Utilizador.Perfil.TECNICO, "1234");

        PedidoManutencao pedido = s.submeterPedido(ativoCodigo, "Pedido UC", admin.getId());
        s.aprovarPedido(pedido.getId(), admin.getId());
        OrdemTrabalho ot = s.converterPedidoEmOt(pedido.getId(), admin.getId());

        s.atribuirTecnico(ot.getId(), tecnicoId, Instant.now());
        s.adicionarTarefa(ot.getId(), "Tarefa UC", 60);
        s.adicionarConsumoPeca(ot.getId(), sku, 1);
        s.registarExecucao(ot.getId(), Instant.now(), Instant.now().plusSeconds(3600), "causa", "acao", tecnicoId, "ok");
        s.adicionarDocumento("OT", ot.getId(), "foto", "path/test.jpg", null);

        s.gerarNotificacoes();
        s.marcarNotificacoesEnviadas();

        List<Parametro> parametros = s.listarParametros();
        if (!parametros.isEmpty()) {
            Parametro p = parametros.get(0);
            s.atualizarParametro(p.getId(), p.getCodigo(), p.getDescricao());
        }

        s.atualizarDashboard(Utilizador.Perfil.GESTOR, "ATIVOS,BACKLOG,MTTR", 10);
        s.guardarTudo();

        System.out.println("PASS");
        System.exit(0);
    }
}
