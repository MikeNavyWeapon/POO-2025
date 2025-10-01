@echo off
echo Criando estrutura de diretorios para lab01...

mkdir lab01
mkdir lab01\.vscode
mkdir lab01\src
mkdir lab01\src\main
mkdir lab01\src\main\java
mkdir lab01\src\main\java\escnaval
mkdir lab01\src\main\java\escnaval\exemplo
mkdir lab01\src\main\resources
mkdir lab01\src\test
mkdir lab01\src\test\java
mkdir lab01\out
mkdir lab01\docs

echo. > lab01\README.md
echo. > lab01\.gitignore
echo. > lab01\.vscode\launch.json
echo. > lab01\.vscode\tasks.json
echo. > lab01\src\main\java\escnaval\exemplo\HelloWorld.java

echo Estrutura criada com sucesso!
pause
