package br.ufal.ic.myfood.exceptions.Usuarios;

public class EnderecoInvalido extends Exception {
    public EnderecoInvalido() {
        super("Endereco invalido");
    }
    public EnderecoInvalido(String message) {super(message);}
}
