package br.ufal.ic.myfood.exceptions.Users;

public class CpfInvalido extends Exception {
    public CpfInvalido() {
        super("CPF invalido");
    }
    public CpfInvalido(String message) {super(message);}
}
