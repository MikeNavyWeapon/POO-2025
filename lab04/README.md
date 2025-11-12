# Lab 04 — Gestão de Alunos

Este repositório contém a implementação do exercício de herança e coleções para gestão de alunos.

## Tabela de testes (exemplo)
Preencher com saídas reais quando executar os casos.

| # | Caso | Passos / Entrada | Saída esperada |
|---:|---|---|---|
| 1 | Listar vazio | 1 | (nenhum resultado) ou mensagem equivalente |
| 2 | Adição válida | 3 → id=1, nome=Ana | Adicionado. (mostrar na listagem) |
| 3 | Duplicado | 3 → id=1, nome=outra | Falha: ID já existente. |
| 4 | Remover existente | 4 → id=1 | Removido. |
| 5 | Remover inexistente | 4 → id=42 | ID não encontrado. |
| 6 | Buscar existente | 5 → "ana" | Mostra 1: Ana |
| 7 | Buscar inexistente | 5 → "zzz" | (nenhum resultado) |
| 8 | Renomear existente | 6 → id=1 → novo="Ana Pereira" | Atualizado. |
| 9 | Renomear inexistente | 6 → id=99 | ID não encontrado. |
| 10 | Nome inválido | 3 → id=2 → nome="   " | Erro: nome não pode ser vazio |

### Executar/Escolha de shell
Compile/Execute (Linux/macOS):

```bash
# a partir de lab04 (diretório do projecto)
javac -d out $(find src -name '*.java')
java -cp out pt.escnaval.exercicios.MenuAlunos
```

PowerShell (Windows):

```powershell
# a partir de lab04
Get-ChildItem -Recurse -Filter *.java | % { $_.FullName } | % { & javac -d out $_ }
java -cp out pt.escnaval.exercicios.MenuAlunos
```

Observação: é preferível compilar todos os ficheiros ao mesmo tempo (um único `javac -d out <lista-de-ficheiros>`) para evitar problemas de dependência.

## 4) Critérios de avaliação (formativa)
- Classe de domínio correta (encapsulamento, invariantes, equals/hashCode, toString). 
- Requisitos funcionais: CRUD (listar, buscar, adicionar, remover, renomear), e segurança I/O na leitura.
- Validação e mensagens de erro claras; sem exceções inesperadas no terminal.
- README com tabela de testes e instruções de execução; código formatado e legível.

## 5) Resolução de problemas (FAQ)
- Duplicados → verifique `findById` e a validação de `adicionar`.
- Ordenação ou lista com elementos nulos → certifique-se de copiar apenas o tamanho atual do array antes de ordenar.
- `Could not find or load main class ...` → normalmente causa por:
	- não compilar os ficheiros (ficou erro de compilação);
	- `javac` não criado `out/pt/.../Classe.class` (compile com `-d out` e paths corretos);
	- executar com `java -cp out <fully.qualified.MainClass>`.
- `Could not find or load main class` após corrigir compilação: compila tudo com uma única invocação do `javac`.

## 6) Cronograma sugerido (120 min)
- 0–10 min: Ambiente e leitura do enunciado
- 10–30 min: Esqueleto das classes (Pessoa, Colaborador, Gestor, Aluno)
- 30–50 min: Implementar `AlunoRepo` (adicionar, remover, procurar)
- 50–70 min: Implementar validações e `toString`/`equals`/`hashCode`
- 70–90 min: Implementar I/O (UtilsIO) e menu
- 90–110 min: Testes manuais e corrigir bugs
- 110–120 min: Preparar README e tabela de testes

## 7) Apêndice — Colecções e classes (detalhado)
- Scanner: leitura de strings e inteiros, tratar `nextLine()` após `nextInt()`.
- String: `trim()`, `toLowerCase()`, `contains()` para buscas por termo.
- Arrays dinâmicos caseiros: redimensionar quando cheio; manter `tamanho` real.
- Comparações: uso de `compareTo` para ordenação por nome e `==`/`equals` para identidade por `id`.

---

Se quiser, posso também:
- Gerar um script de testes (shell/PowerShell) que executa os casos da tabela automaticamente e captura a saída;
- Adicionar um ficheiro `tests/` com entradas de teste e um pequeno runner Java que valida as saídas.

Diz-me se queres que eu commite este `README.md` no `lab04` agora e faça push.

      
