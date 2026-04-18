package br.ufal.ic.myfood.exceptions.Produtos;

public class NomeInvalido extends Exception{
    public NomeInvalido() {
        super("Nome invalido");
    }
    public NomeInvalido(String message) {super(message);}
}
