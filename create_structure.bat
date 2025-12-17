@echo off
echo Criando estrutura de diretorios para lab06...

mkdir lab07
mkdir lab07\.vscode
mkdir lab07\src
mkdir lab07\src\main
mkdir lab07\src\main\java
mkdir lab07\src\main\java\pt
mkdir lab07\src\main\java\pt\escnaval
mkdir lab07\src\main\java\pt\escnaval\exercicios
mkdir lab07\src\main\java\pt\escnaval\exercicios\mediateca
mkdir lab07\src\main\java\pt\escnaval\exercicios\mediateca\modelo
mkdir lab07\src\main\java\pt\escnaval\exercicios\mediateca\servicos
mkdir lab07\src\main\java\pt\escnaval\exercicios\mediateca\utils
mkdir lab07\src\data
mkdir lab07\src\media

echo. > lab07\src\main\java\pt\escnaval\exercicios\mediateca\modelo\Album.java
echo. > lab07\src\main\java\pt\escnaval\exercicios\mediateca\modelo\Faixa.java
echo. > lab07\src\main\java\pt\escnaval\exercicios\mediateca\servicos\Mediateca.java
echo. > lab07\src\main\java\pt\escnaval\exercicios\mediateca\servicos\RepositorioTexto.java
echo. > lab07\src\main\java\pt\escnaval\exercicios\mediateca\servicos\Mp3Util.java
echo. > lab07\src\main\java\pt\escnaval\exercicios\mediateca\utils\UtilsIO.java
echo. > lab07\src\main\java\pt\escnaval\exercicios\mediateca\AppMediateca.java
echo. > lab07\src\main\java\pt\escnaval\exercicios\mediateca\MenuMediateca.java
echo. > lab07\README.md
echo. > lab07\gitignore


echo Estrutura criada com sucesso!
pause
