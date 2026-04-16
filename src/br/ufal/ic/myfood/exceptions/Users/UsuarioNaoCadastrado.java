package br.ufal.ic.myfood.exceptions.Users;

public class UsuarioNaoCadastrado extends Exception {
    public UsuarioNaoCadastrado() {
        super("br.ufal.ic.myfood.models.Usuario nao cadastrado.");
    }
    public UsuarioNaoCadastrado(String message) {super(message);}
}
