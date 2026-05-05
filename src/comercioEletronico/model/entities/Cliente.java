package comercioEletronico.model.entities;

public class Cliente {
    private int id;
    private String nome;
    private String email;
    private String telefone;

    public Cliente(String nome, String email, String telefone) {
        validarEstado(nome, email, telefone);
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
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
        validarEstado(nome, email, telefone);
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        validarEstado(nome, email, telefone);
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        validarEstado(nome, email, telefone);
        this.telefone = telefone;
    }

    private void validarEstado(String nome, String email, String telefone) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Erro! O nome do cliente não pode ser vazio.");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Erro! O email do cliente não pode ser vazio.");
        }
        if (telefone == null || telefone.trim().isEmpty()) {
            throw new IllegalArgumentException("Erro! O telefone do cliente não pode ser vazio.");
        }
    }

    @Override
    public String toString() {
        return id + " - " + nome + " - " + email + " - " + telefone;
    }
}