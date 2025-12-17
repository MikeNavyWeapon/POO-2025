# Lab07 — Mediateca ✅

Resumo dos testes realizados (17-Dec-2025):

- Ficheiros fonte compilados com: javac -encoding UTF-8 -d out (a pasta `out` gerada em `lab07/out`).
- Teste executado com o ficheiro de input: `lab07_test_input.txt`.

Passos executados (resumo):
1. Carreguei a mediateca (não dependente): programa informou "Mediateca: 1 álbum(es) carregado(s)".
2. Adicionei uma faixa ao álbum e verifiquei a listagem.
3. Copiei o ficheiro MP3 a partir da localização fornecida: `C:\Users\PC\Desktop\Programação Orientada a Objetos\Labs\media\exemplo.mp3` — o ficheiro foi copiado para `lab07/media/exemplo_copia.mp3`.
4. Guardei a mediateca em `lab07/data/mediateca.csv`.

Saída relevante capturada (trechos):
```
Ficheiro copiado para: C:\Users\PC\Desktop\Programação Orientada a Objetos\POO\lab07\media\exemplo_copia.mp3
Mediateca guardada em: C:\Users\PC\Desktop\Programação Orientada a Objetos\POO\lab07\data\mediateca.csv
=== ÁLBUNS ===
1) 1 ? aaa (bbb) [2 faixas]
	01. [import] (12s) [d:\JavaWork\Work_POO\Lab07\lab07\media\exemplo.mp3]
	02. Test (120s)
```

Estado dos ficheiros no repositório (verificado):

- `lab07/media/exemplo.mp3` (origem copiada para o repo)
- `lab07/media/exemplo_copia.mp3` (criado pelo programa durante o teste)
- `lab07/data/mediateca.csv` (guardar da mediateca — contém entradas dos álbuns/faixas)

Notas técnicas:
- Corrigi `MenuMediateca` para usar diretórios relativos `data/` e `media/` em vez de `lab07/data` e `lab07/media` — isto evita criar paths duplicados `lab07/lab07/...` quando o programa é executado a partir da pasta `lab07`.

Se quiser, faço o commit destas alterações (README + correção) e envio para o GitHub. ✅

