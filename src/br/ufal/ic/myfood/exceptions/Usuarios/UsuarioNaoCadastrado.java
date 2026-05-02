package br.ufal.ic.myfood.exceptions.Usuarios;

public class UsuarioNaoCadastrado extends Exception {
    public UsuarioNaoCadastrado() {
        super("Usuario nao cadastrado.");
    }
    public UsuarioNaoCadastrado(String message) {super(message);}
}
