package br.ufal.ic.myfood.exceptions.Produtos;

public class ProdutoInvalido extends Exception {
    public ProdutoInvalido() {
        super("Produto invalido");
    }
    public ProdutoInvalido(String message) {
        super(message);
    }
}
