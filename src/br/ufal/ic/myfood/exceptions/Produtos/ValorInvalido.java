package br.ufal.ic.myfood.exceptions.Produtos;

public class ValorInvalido extends Exception{
    public ValorInvalido() {
        super("Valor invalido");
    }
    public ValorInvalido(String message) {super(message);}
}
