package pt.escnaval.exercicios;

public class DemoArmadilhas {
    
    // Classe mal definida: equals sem hashCode (NÃO FAZER)
    static class SemHash {
        final int id;
        SemHash(int id) { this.id = id; }
        
        @Override
        public boolean equals(Object o) {
            return (o instanceof SemHash) && ((SemHash)o).id == id;
        }
        // hashCode ausente → comportamento inconsistente
    }
    
    // Classe com hash baseado em campo mutável (NÃO FAZER)
    static class HashMutavel {
        int x;
        HashMutavel(int x) { this.x = x; }
        
        @Override
        public boolean equals(Object o) {
            return (o instanceof HashMutavel) && ((HashMutavel)o).x == x;
        }
        
        @Override
        public int hashCode() {
            return Integer.hashCode(x);
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== Demonstração de Armadilhas (Pitfalls) ===\n");
        
        // 1) equals sem hashCode
        System.out.println("--- Armadilha 1: equals sem hashCode ---");
        SemHash sh1 = new SemHash(1);
        SemHash sh2 = new SemHash(1);
        System.out.println("sh1.equals(sh2)? " + sh1.equals(sh2) + " (true)");
        System.out.println("sh1.hashCode() == sh2.hashCode()? " + 
            (sh1.hashCode() == sh2.hashCode()) + " (provavelmente false - PROBLEMA!)");
        System.out.println("Problema: quebra o contrato equals/hashCode");
        
        // 2) Hash com campo mutável
        System.out.println("\n--- Armadilha 2: hashCode com campo mutável ---");
        HashMutavel hm = new HashMutavel(10);
        int hashAntes = hm.hashCode();
        System.out.println("Hash inicial: " + hashAntes);
        
        // Simular inserção em array (como se fosse HashSet)
        HashMutavel[] conjunto = new HashMutavel[10];
        conjunto[0] = hm;
        
        // Mutar o objeto
        hm.x = 20;
        int hashDepois = hm.hashCode();
        System.out.println("Hash após mutação: " + hashDepois);
        System.out.println("Hash mudou? " + (hashAntes != hashDepois) + " (PROBLEMA!)");
        System.out.println("Problema: hash instável torna o objeto 'perdido' em estruturas baseadas em hash");
    }
}