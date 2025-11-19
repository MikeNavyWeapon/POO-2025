package pt.escnaval.exercicios;

import java.util.Arrays;

/**
 * Demonstração de ordenação e busca em arrays sem usar coleções.
 * Implementa ordenação manual por nome e busca binária.
 */
public class DemoOrdenacao {
    public static void main(String[] args) {
        System.out.println("=== Demonstração de Ordenação e Busca ===\n");

        // Criar clientes desordenados
        Endereco end1 = new Endereco("Rua A", "Lisboa", "1000-001");
        Endereco end2 = new Endereco("Rua B", "Porto", "4000-001");
        Endereco end3 = new Endereco("Rua C", "Coimbra", "3000-001");

        Cliente[] clientes = new Cliente[5];
    clientes[0] = new Cliente(3, "Carlos Pereira", "carlos@example.com", end1, "pass");
    clientes[1] = new Cliente(1, "Ana Silva", "ana@example.com", end2, "pass");
    clientes[2] = new Cliente(5, "Bruno Costa", "bruno@example.com", end3, "pass");
    clientes[3] = new Cliente(2, "Ana Santos", "ana.santos@example.com", end1, "pass");
    clientes[4] = new Cliente(4, "Diana Lopes", "diana@example.com", end2, "pass");

        // Mostrar array original
        System.out.println("--- Array Original (desordenado) ---");
        mostrarClientes(clientes);

        // Ordenar por nome (alfabético) e depois por id (critério de desempate)
        System.out.println("\n--- Ordenação por Nome (depois ID) ---");
        ordenarPorNomeEId(clientes);
        mostrarClientes(clientes);

        // Busca sequencial (linear)
        System.out.println("\n--- Busca Sequencial ---");
        Cliente encontrado = buscaSequencial(clientes, "Bruno Costa");
        if (encontrado != null) {
            System.out.println("✓ Encontrado: " + encontrado);
        } else {
            System.out.println("✗ Cliente não encontrado");
        }

        // Busca por cliente inexistente
        encontrado = buscaSequencial(clientes, "João Silva");
        if (encontrado != null) {
            System.out.println("✓ Encontrado: " + encontrado);
        } else {
            System.out.println("✗ Cliente 'João Silva' não encontrado (esperado)");
        }

        // Busca binária (requer array ordenado)
        System.out.println("\n--- Busca Binária (array ordenado) ---");
        int indice = buscaBinariaPorNome(clientes, "Diana Lopes");
        if (indice >= 0) {
            System.out.println("✓ Encontrado no índice " + indice + ": " + clientes[indice]);
        } else {
            System.out.println("✗ Cliente não encontrado");
        }

        // Busca binária - cliente inexistente
        indice = buscaBinariaPorNome(clientes, "Zélia Costa");
        if (indice >= 0) {
            System.out.println("✓ Encontrado: " + clientes[indice]);
        } else {
            System.out.println("✗ Cliente 'Zélia Costa' não encontrado (esperado)");
        }

        // Demonstração de comparação com tie-breaker
        System.out.println("\n--- Análise de Empates (tie-breaker por ID) ---");
        System.out.println("Clientes com nome 'Ana':");
        for (Cliente c : clientes) {
            if (c.getNome().startsWith("Ana")) {
                System.out.println("  ID=" + c.getId() + " -> " + c.getNome());
            }
        }
        System.out.println("Ordem: Ana Santos (ID=2) vem antes de Ana Silva (ID=1)? " + 
                          (indiceDeCliente(clientes, 2) < indiceDeCliente(clientes, 1)));
    }

    /**
     * Ordena array de clientes por nome (alfabético) e depois por id (desempate).
     * Usa Bubble Sort simplificado (adequado para fins didáticos).
     * Complexidade: O(n²)
     */
    private static void ordenarPorNomeEId(Cliente[] clientes) {
        int n = clientes.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                // Comparar por nome primeiro
                int cmpNome = clientes[j].getNome().compareTo(clientes[j + 1].getNome());
                
                // Se nomes iguais, comparar por id (tie-breaker)
                boolean trocar = false;
                if (cmpNome > 0) {
                    trocar = true;  // nome j vem depois de j+1
                } else if (cmpNome == 0) {
                    // Nomes iguais: desempatar por id
                    if (clientes[j].getId() > clientes[j + 1].getId()) {
                        trocar = true;
                    }
                }

                if (trocar) {
                    Cliente temp = clientes[j];
                    clientes[j] = clientes[j + 1];
                    clientes[j + 1] = temp;
                }
            }
        }
    }

    /**
     * Busca sequencial (linear) por nome.
     * Complexidade: O(n)
     */
    private static Cliente buscaSequencial(Cliente[] clientes, String nome) {
        for (Cliente c : clientes) {
            if (c.getNome().equals(nome)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Busca binária por nome em array ORDENADO.
     * Retorna índice do cliente ou -1 se não encontrado.
     * Complexidade: O(log n)
     */
    private static int buscaBinariaPorNome(Cliente[] clientes, String nome) {
        int esquerda = 0;
        int direita = clientes.length - 1;

        while (esquerda <= direita) {
            int meio = esquerda + (direita - esquerda) / 2;
            int cmp = clientes[meio].getNome().compareTo(nome);

            if (cmp == 0) {
                return meio;  // encontrado
            } else if (cmp < 0) {
                esquerda = meio + 1;  // buscar na metade direita
            } else {
                direita = meio - 1;   // buscar na metade esquerda
            }
        }
        return -1;  // não encontrado
    }

    /**
     * Encontra o índice de um cliente pelo ID.
     */
    private static int indiceDeCliente(Cliente[] clientes, int id) {
        for (int i = 0; i < clientes.length; i++) {
            if (clientes[i].getId() == id) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Exibe array de clientes formatado.
     */
    private static void mostrarClientes(Cliente[] clientes) {
        for (int i = 0; i < clientes.length; i++) {
            System.out.printf("[%d] %s\n", i, clientes[i]);
        }
    }
}
