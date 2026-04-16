package br.ufal.ic.myfood.exceptions.Users;

public class ContaEmailEmUso extends Exception {
    public ContaEmailEmUso() {
        super("Conta com esse email ja existe");
    }
    public ContaEmailEmUso(String message) {super(message);}
}
