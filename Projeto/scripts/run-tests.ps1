$ErrorActionPreference = "Stop"

Write-Host "Compiling main sources..."
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse -Filter *.java src\main\java | ForEach-Object { $_.FullName })

Write-Host "Compiling tests..."
javac -encoding UTF-8 -cp out -d out (Get-ChildItem -Recurse -Filter *.java src\test\java | ForEach-Object { $_.FullName })

Write-Host "Running tests..."
java -cp out pt.escnaval.exercicios.manutencao.TestServicoManutencao
java -cp out pt.escnaval.exercicios.manutencao.TestCenariosMinimos

Write-Host "Coverage: no automated tool configured; scenarios executed above."
