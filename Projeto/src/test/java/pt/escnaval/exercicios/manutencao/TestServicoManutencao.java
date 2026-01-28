package pt.escnaval.exercicios.manutencao;

import pt.escnaval.exercicios.manutencao.modelo.OrdemTrabalho;
import pt.escnaval.exercicios.manutencao.modelo.Utilizador;
import pt.escnaval.exercicios.manutencao.servicos.ServicoManutencao;

public class TestServicoManutencao {
    public static void main(String[] args) throws Exception {
        ServicoManutencao s = new ServicoManutencao();
        s.carregarTudo();

        Utilizador admin = s.autenticar("admin", "admin");
        if (admin == null) { System.err.println("FAIL: login admin"); System.exit(1); }

        String ativoCodigo = "TST-" + System.currentTimeMillis();
        s.adicionarAtivo(ativoCodigo, "Ativo Teste", 3, null, null);
        if (s.buscarAtivo(ativoCodigo) == null) { System.err.println("FAIL: ativo nao criado"); System.exit(1); }

        OrdemTrabalho ot = s.criarOrdemTrabalho(OrdemTrabalho.Tipo.CORRETIVA, 3, "Teste OT", ativoCodigo, null, null, null);
        if (s.buscarOT(ot.getId()) == null) { System.err.println("FAIL: OT nao criada"); System.exit(1); }

        String sku = "SKU-" + System.currentTimeMillis();
        s.adicionarPeca(sku, "Peca Teste", "UN", 1, 2.5);
        s.movimentarStock(sku, "GERAL", pt.escnaval.exercicios.manutencao.modelo.MovimentoStock.Tipo.ENTRADA, 5, null);

        var fornecedor = s.adicionarFornecedor("Fornecedor Teste", null, null, 8);
        var otExt = s.criarOrdemTrabalhoCompleta(OrdemTrabalho.Tipo.EXTERNA, 4, "OT Externa", ativoCodigo, null, null, null,
                8, fornecedor.getId(), "EXT-1", 100.0);
        if (s.buscarOT(otExt.getId()) == null) { System.err.println("FAIL: OT externa nao criada"); System.exit(1); }

        s.registarInventarioCiclico(sku, "GERAL", 4);
        s.aplicarRetencaoDados();

        s.guardarTudo();
        System.out.println("PASS");
        System.exit(0);
    }
}
