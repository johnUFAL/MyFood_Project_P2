package br.ufal.ic.myfood.exceptions.Pedidos;

public class ProdutoNaoEncontrado extends Exception {
    public ProdutoNaoEncontrado() {
        super("Produto nao encontrado");
    }
    public ProdutoNaoEncontrado(String message) {
        super(message);
    }
}
