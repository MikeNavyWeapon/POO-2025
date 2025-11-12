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
