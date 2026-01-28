# Projeto - Gestao de Manutencao 


- Compilar (sem JavaFX):
  `javac -encoding UTF-8 -d out (Get-ChildItem -Recurse -Filter *.java src\main\java | ForEach-Object { $_.FullName })`
- Executar:
  `java -cp out pt.escnaval.exercicios.manutencao.AppManutencao`


Exemplo (Windows):
- Compilar:
  `javac --module-path "C:\path\javafx\lib" --add-modules javafx.controls -encoding UTF-8 -d out (Get-ChildItem -Recurse -Filter *.java src\main\java,src\javafx\java | ForEach-Object { $_.FullName })`
- Executar:
   `java --module-path "C:\Program Files\Java\javafx-sdk-25.0.2\lib" --add-modules javafx.controls -cp out pt.escnaval.exercicios.manutencao.AppManutencao --fx`

## Testes
- Compilar testes (apos compilar o codigo principal):
  `javac -encoding UTF-8 -cp out -d out (Get-ChildItem -Recurse -Filter *.java src\test\java | ForEach-Object { $_.FullName })`
- Executar:
  `java -cp out pt.escnaval.exercicios.manutencao.TestServicoManutencao`
  `java -cp out pt.escnaval.exercicios.manutencao.TestCenariosMinimos`
 - Script rapido:
  `powershell -ExecutionPolicy Bypass -File scripts\run-tests.ps1`

## Cobertura minima
- Os testes executam cenarios essenciais (ativos, OT, inventario, pedidos, execucao, notificacoes).
- Nao ha ferramenta automatica de coverage configurada; a validacao minima e feita pelos testes acima.

## Notas D02/D03
- D02 (KPIs/queries) e D03 (integridade/triggers) sao implementados no servico e persistidos em CSV.
- Como nao ha BD SQL, as "views/triggers" sao equivalentes a logica no dominio.

## Credenciais
- Utilizador: `admin`
- Senha: `admin`
