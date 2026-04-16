package br.ufal.ic.myfood.exceptions.Users;

public class SenhaInvalida extends Exception {
    public SenhaInvalida() {
        super("Senha invalido");
    }
    public SenhaInvalida(String message) {super(message);}
}
