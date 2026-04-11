import java.util.Map;
import  java.util.HashMap;

public class ControladorDeUsuarios {
    private  Map<Integer, Usuario> usuarios;
    private int proximoId;

    public ControladorDeUsuarios() {
        this.usuarios = new HashMap<>();
        this.proximoId = 1;
    }

    public Map<Integer, Usuario> getUsuarios() {return usuarios;}
    public int getProximoId() {return proximoId;}

    public void setProximoId(int proximoId) {this.proximoId = proximoId;}

    public void zerar() {
        this.usuarios.clear();
        this.proximoId = 1;
    }

    public int gerarId() {return this.proximoId++;}

    public void criarCliente(String nome, String email, String senha, String endereco) throws Exception {
        validarDadosBase(nome, email, senha, endereco);
        verificarEmailExistente(email);
        int id = gerarId();
        Cliente cliente = new Cliente(id, nome, email, senha, endereco);
        this.usuarios.put(id, cliente);
    }

    public void criarDono(String nome, String email, String senha, String endereco, String cpf) throws Exception {
        validarDadosBase(nome, email, senha, endereco);
        verificarEmailExistente(email);
        validarCpf(cpf);
        int id = gerarId();
        DonoEmpresa dono = new DonoEmpresa(id, nome, email, senha, endereco, cpf);
        this.usuarios.put(id, dono);
    }

    public int login(String email, String senha) throws Exception {
        if (email == null || email.trim().isEmpty() || senha == null || senha.trim().isEmpty()) {
            throw new Exception("Login ou senha invalidos.");
        }
        for (Usuario u : usuarios.values()) {
            if (u.getEmail().equals(email) && u.getSenha().equals(senha)) {
                return u.getId();
            }
        }
        throw new Exception("Login ou senha invalidos");
    }

    public String getAtributoUsuario(int id, String atributo) throws Exception {
        Usuario u = buscarUsuarioPorId(id);
        switch (atributo) {
            case "nome": return u.getNome();
            case "email": return u.getEmail();
            case "senha": return u.getSenha();
            case "endereco": return u.getEndereco();
            case "cpf": {
                if (u instanceof DonoEmpresa) {
                    return ((DonoEmpresa) u).getCpf();
                }
                throw new Exception("Atributo nao existe");
            }
            default: throw  new Exception("Atributo invalido");
        }
    }

    private Usuario buscarUsuarioPorId(int id) throws Exception {
        if (!this.usuarios.containsKey(id)) {
            throw new Exception("Usuario nao cadastrado.");
        }
        return this.usuarios.get(id);
    }

    private void validarDadosBase(String nome, String email, String senha, String endereco) throws Exception {
        if (nome == null || nome.trim().isEmpty()) throw new Exception("Nome invalido");
        if (email == null || email.trim().isEmpty() || !email.contains("@")) throw new Exception("Email invalido");
        if (senha == null || senha.trim().isEmpty()) throw new Exception("Senha invalido");
        if (endereco == null || endereco.trim().isEmpty()) throw new Exception("Endereco invalido");
    }

    private void validarCpf(String cpf) throws Exception {
        if (cpf == null || cpf.length() != 14) throw new Exception("CPF invalido");
    }

    private void verificarEmailExistente(String email) throws Exception {
        for (Usuario u : usuarios.values()) {
            if (u.getEmail().equals(email)) {
                throw new Exception("Conta com esse email ja existe");
            }
        }
    }
}
