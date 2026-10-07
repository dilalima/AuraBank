public class Cliente {
    private final String nome, cpf, email, senha, telefone;

    public Cliente(String nome, String cpf, String email, String senha, String telefone) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
    }

    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public String getEmail() { return email; }
    public String getTelefone() { return telefone; }
    public boolean autenticar(String senha) { return this.senha.equals(senha); }
}
