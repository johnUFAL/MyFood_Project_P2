package br.ufal.ic.myfood.exceptions.Usuarios;

public class SenhaInvalida extends Exception {
    public SenhaInvalida() {
        super("Senha invalido");
    }
    public SenhaInvalida(String message) {super(message);}
}
