package br.ufal.ic.myfood.exceptions.Usuarios;

public class LoginSenhaException extends Exception {
    public LoginSenhaException() {
        super("Login ou senha invalidos");
    }

    public LoginSenhaException(String message) {super(message);}
}