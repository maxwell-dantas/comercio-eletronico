package comercioEletronico.model.entities;

public class Cliente {
    private int id;
    private String nome;
    private String email;
    private String telefone;
    private String senha;

    public Cliente(String nome, String email, String telefone, String senha) {
        validarEstado(nome, email, telefone, senha);
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.senha = senha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (this.id != 0) {
            throw new IllegalStateException("Violação de Segurança: O ID de um cliente não pode ser modificado após ser gerado!");
        }
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        validarEstado(nome, email, telefone, senha);
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        validarEstado(nome, email, telefone, senha);
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        validarEstado(nome, email, telefone, senha);
        this.senha = senha;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        validarEstado(nome, email, telefone, senha);
        this.telefone = telefone;
    }

    private void validarEstado(String nome, String email, String telefone, String senha) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Erro! O nome não pode ser vazio.");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Erro! O email não pode ser vazio.");
        }
        if (telefone == null || telefone.trim().isEmpty()) {
            throw new IllegalArgumentException("Erro! O telefone não pode ser vazio.");
        }

        if (senha == null || senha.trim().isEmpty()) {
            throw new IllegalArgumentException("Erro! A senha não pode ser vazia.");
        }
    }

    @Override
    public String toString() {
        return id + " - " + nome + " - " + email + " - " + telefone;
    }
}