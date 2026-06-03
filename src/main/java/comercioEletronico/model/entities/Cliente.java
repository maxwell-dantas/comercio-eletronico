package comercioEletronico.model.entities;

public class Cliente implements Identificavel {
    private int id;
    private int idFuncao;
    private String nome;
    private String email;
    private String telefone;
    private String senha;

    public Cliente() {}

    public Cliente(String nome, String telefone, String email, String senha, int idFuncao) {
        setNome(nome);
        setTelefone(telefone);
        setEmail(email);
        setSenha(senha);
        setIdFuncao(idFuncao);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
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
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Erro de validação: o nome não pode ser vazio.");
        }
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        if (telefone == null || telefone.trim().isEmpty()) {
            throw new IllegalArgumentException("Erro de validação: o telefone não pode ser vazio.");
        }
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Erro de validação: o email não pode ser vazio.");
        }
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        if (senha == null || senha.trim().isEmpty()) {
            throw new IllegalArgumentException("Erro de validação: a senha não pode ser vazia.");
        }
        this.senha = senha;
    }

    public int getIdFuncao() {
        return idFuncao;
    }

    public void setIdFuncao(int idFuncao) {
        if (idFuncao < 1 || idFuncao > 3) {
            throw new IllegalArgumentException("Erro de validação: ID de função inválido (1=Admin, 2=Cliente, 3=Entregador).");
        }
        this.idFuncao = idFuncao;
    }

    @Override
    public String toString() {
        return id + " - " + nome + " - " + telefone + " - " + email + " - Função: " + idFuncao;
    }
}