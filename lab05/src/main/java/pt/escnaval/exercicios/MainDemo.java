package pt.escnaval.exercicios;
public class MainDemo {
 public static void main(String[] args) {
 Banco banco = new Banco(10);
 // Criar endereços
 Endereco end1 = new Endereco("Rua das Flores 10", "Lisboa", "1000-001");
 Endereco end2 = new Endereco("Av. dos Aliados 25", "Porto", "4000-002");
 // Criar clientes
   Cliente c1 = new Cliente(1, "Ana Silva", "ana@example.com", end1, "pass");
   Cliente c2 = new Cliente(2, "Bruno Costa", "bruno@example.com", end2, "pass");
 if (!c1.isValido() || !c2.isValido()) {
 System.out.println("Erro: clientes inválidos");
 return;
 }
 // Criar IBANs
 Iban iban1 = new Iban("PT50000000000000000000000");
 Iban iban2 = new Iban("PT60000000000000000000000");
 if (!iban1.isValid() || !iban2.isValid()) {
 System.out.println("Erro: IBANs inválidos");
 return;
 }
 // Criar contas
 Conta a1 = new Conta(iban1, c1, 100.0);
 Conta a2 = new Conta(iban2, c2, 20.0);
 if (!a1.isValida() || !a2.isValida()) {
 System.out.println("Erro: contas inválidas");
 return;
 }
 // Abrir contas no banco
 if (!banco.abrirConta(a1)) {
 System.out.println("Erro ao abrir conta 1");
 }
 if (!banco.abrirConta(a2)) {
 System.out.println("Erro ao abrir conta 2");
 }
 System.out.println("=== Estado Inicial ===");
 System.out.println(a1);
 System.out.println(a2);
 System.out.println("Número de contas: " + banco.getNumContas());
 // Teste: Depósito válido
 System.out.println("\n=== Teste: Depósito de 25.0 em a1 ===");
 if (a1.depositar(25.0)) {
 System.out.println("Sucesso: " + a1);
 } else {
 System.out.println("Falha no depósito");
 }
 // Teste: Transferência inválida (saldo insuficiente)
 System.out.println("\n=== Teste: Transferência de 300.0 de a1 para a2 (deve falhar) ===");
 if (!a1.transferirPara(a2, 300.0)) {
 System.out.println("Esperado: Transferência falhou (saldo insuficiente)");
 } else {
 System.out.println("Erro: Transferência não deveria ter sucedido");
 }
 // Teste: Transferência válida
 System.out.println("\n=== Teste: Transferência de 30.0 de a1 para a2 ===");
 if (a1.transferirPara(a2, 30.0)) {
 System.out.println("Sucesso na transferência");
 } else {
 System.out.println("Falha na transferência");
    }
 System.out.println("\n=== Estado Final ===");
 System.out.println(a1);
 System.out.println(a2);
 // Teste: Tentar abrir conta com IBAN duplicado
 System.out.println("\n=== Teste: Abrir conta com IBAN duplicado ===");
 Conta a1Dup = new Conta(iban1, c2, 50.0);
 if (!banco.abrirConta(a1Dup)) {
 System.out.println("Esperado: Conta duplicada rejeitada");
 } else {
 System.out.println("Erro: Conta duplicada não deveria ser aceita");
 }
 // Teste: Fechar conta
 System.out.println("\n=== Teste: Fechar conta a2 ===");
 if (banco.fecharConta(iban2)) {
 System.out.println("Conta fechada com sucesso");
 System.out.println("Número de contas: " + banco.getNumContas());
 } else {
 System.out.println("Erro ao fechar conta");
 }
 // Teste: Buscar conta
 System.out.println("\n=== Teste: Buscar conta por IBAN ===");
 Conta encontrada = banco.findByIban(iban1);
 if (encontrada != null) {
 System.out.println("Conta encontrada: " + encontrada);
 } else {
 System.out.println("Conta não encontrada");
 }
 }
}
