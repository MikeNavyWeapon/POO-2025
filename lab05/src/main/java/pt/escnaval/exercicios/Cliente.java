package pt.escnaval.exercicios;

import java.util.regex.Pattern;

public class Cliente implements Autenticavel {
    private final int id;           // identidade
    private String nome;
    private String email;
    private Endereco endereco;      // composição: tem-um endereço
    private String senha;           // hash simplificado (em produção usar bcrypt)
    private final boolean valido;

    private static final Pattern EMAIL_RE =
        Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    public Cliente(int id, String nome, String email, Endereco endereco, String senha) {
        this.id = id;
        // Validação sem exceções
        if (id <= 0 || nome == null || nome.trim().isEmpty() ||
            email == null || !EMAIL_RE.matcher(email.trim()).matches() ||
            endereco == null || !endereco.isValido() ||
            senha == null || senha.length() < 4) {
            this.nome = "";
            this.email = "";
            this.endereco = null;
            this.senha = "";
            this.valido = false;
        } else {
            this.nome = nome.trim();
            this.email = email.trim();
            this.endereco = endereco;
            this.senha = senha; // simplificado: em produção, usar hash seguro
            this.valido = true;
        }
    }

    // removed convenience constructor: callers must provide a password explicitly

    public boolean isValido() { return valido; }
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public Endereco getEndereco() { return endereco; }

    public boolean mudarEmail(String novoEmail) {
        if (novoEmail == null || !EMAIL_RE.matcher(novoEmail.trim()).matches()) {
            return false;
        }
        this.email = novoEmail.trim();
        return true;
    }

    public boolean mudarEndereco(Endereco novo) {
        if (novo == null || !novo.isValido()) {
            return false;
        }
        this.endereco = novo;
        return true;
    }
    
    public boolean mudarSenha(String senhaAtual, String novaSenha) {
        if (!autenticar(senhaAtual) || novaSenha == null || novaSenha.length() < 4) {
            return false;
        }
        this.senha = novaSenha;
        return true;
    }

    // Implementação da interface Autenticavel
    @Override
    public boolean autenticar(String senha) {
        if (!valido || senha == null) return false;
        return this.senha.equals(senha); // simplificado
    }

    @Override
    public String getIdentificador() {
        return email;
    }

    @Override
    public String toString() {
        if (!valido) return "[Cliente inválido]";
        return String.format("%d\t%-18s <%s> @ %s", id, nome, email, endereco);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cliente)) return false;
        Cliente other = (Cliente) o;
        return id == other.id; // identidade por id
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}