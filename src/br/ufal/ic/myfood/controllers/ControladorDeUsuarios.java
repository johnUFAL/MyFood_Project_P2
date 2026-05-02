package br.ufal.ic.myfood.controllers;


import br.ufal.ic.myfood.exceptions.*;
import br.ufal.ic.myfood.exceptions.Usuarios.*;
import br.ufal.ic.myfood.exceptions.Usuarios.Entregador.placaInvalido;
import br.ufal.ic.myfood.exceptions.Usuarios.Entregador.veiculoInvalido;
import br.ufal.ic.myfood.models.Usuario;
import br.ufal.ic.myfood.models.DonoEmpresa;
import br.ufal.ic.myfood.models.Cliente;
import br.ufal.ic.myfood.models.Entregador;


import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;


public class ControladorDeUsuarios {
    private  Map<Integer, Usuario> usuarios;
    private int proximoId;

    public ControladorDeUsuarios() {
        this.usuarios = new LinkedHashMap<>();
        this.proximoId = 1;
    }

    public Map<Integer, Usuario> getUsuarios() {return usuarios;}
    public void setUsuarios(Map<Integer, Usuario> usuarios) {this.usuarios = usuarios;}

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
        validarCpf(cpf);
        verificarEmailExistente(email);
        int id = gerarId();
        DonoEmpresa dono = new DonoEmpresa(id, nome, email, senha, endereco, cpf);
        this.usuarios.put(id, dono);
    }

    public void criarEntregador(String nome, String email, String senha, String endereco, String veiculo, String placa) throws Exception {
        validarDadosBase(nome, email, senha, endereco);

        if (veiculo == null || veiculo.trim().isEmpty()) throw new veiculoInvalido();
        if (placa == null || placa.trim().isEmpty()) throw new placaInvalido();

        for (Usuario u : usuarios.values()) {
            if (u instanceof Entregador && ((Entregador) u).getPlaca().equals(placa)) {
                throw new placaInvalido();
            }
        }

        verificarEmailExistente(email);

        int id = gerarId();
        Entregador entregador = new Entregador(id, nome, email, senha, endereco, veiculo, placa);
        this.usuarios.put(id, entregador);
    }

    public int login(String email, String senha) throws Exception {
        if (email == null || email.trim().isEmpty() || senha == null || senha.trim().isEmpty()) {
            throw new LoginSenhaException();
        }
        for (Usuario u : usuarios.values()) {
            if (email.equals(u.getEmail()) && senha.equals(u.getSenha())) {
                return u.getId();
            }
        }
        throw new LoginSenhaException();
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
                throw new AtributoNaoExiste();
            }
            case "placa":
                if (u instanceof Entregador) {
                    return ((Entregador) u).getPlaca();
                }
                throw new AtributoNaoExiste();
            case "veiculo":
                if (u instanceof Entregador) {
                    return ((Entregador) u).getVeiculo();
                }
                throw new AtributoNaoExiste();
            default: throw  new AtributoInvalido();
        }
    }

    public Usuario buscarUsuarioPorId(int id) throws Exception {
        if (!this.usuarios.containsKey(id)) {
            throw new UsuarioNaoCadastrado();
        }
        return this.usuarios.get(id);
    }

    private void validarDadosBase(String nome, String email, String senha, String endereco) throws Exception {
        if (nome == null || nome.trim().isEmpty()) throw new NomeInvalido();
        if (email == null || email.trim().isEmpty() || !email.contains("@")) throw new EmailInvalido();
        if (senha == null || senha.trim().isEmpty()) throw new SenhaInvalida();
        if (endereco == null || endereco.trim().isEmpty()) throw new EnderecoInvalido();
    }

    private void validarCpf(String cpf) throws Exception {
        if (cpf == null || cpf.length() != 14) throw new CpfInvalido();
    }

    private void verificarEmailExistente(String email) throws Exception {
        for (Usuario u : usuarios.values()) {
            if (u.getEmail().equals(email)) {
                throw new ContaEmailEmUso();
            }
        }
    }
}
