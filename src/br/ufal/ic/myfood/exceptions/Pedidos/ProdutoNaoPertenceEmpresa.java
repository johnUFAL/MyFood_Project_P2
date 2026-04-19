package br.ufal.ic.myfood.exceptions.Pedidos;

public class ProdutoNaoPertenceEmpresa extends Exception {
    public ProdutoNaoPertenceEmpresa() {
        super("O produto nao pertence a essa empresa");
    }
    public ProdutoNaoPertenceEmpresa(String message) {
        super(message);
    }
}
