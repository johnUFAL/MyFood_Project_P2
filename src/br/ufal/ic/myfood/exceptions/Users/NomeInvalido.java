package br.ufal.ic.myfood.exceptions.Users;

public class NomeInvalido extends Exception{
    public NomeInvalido() {
        super("Nome invalido");
    }
    public NomeInvalido(String message) {super(message);}
}
