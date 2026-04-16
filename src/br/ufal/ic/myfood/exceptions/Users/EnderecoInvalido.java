package br.ufal.ic.myfood.exceptions.Users;

public class EnderecoInvalido extends Exception {
    public EnderecoInvalido() {
        super("Endereco invalido");
    }
    public EnderecoInvalido(String message) {super(message);}
}
