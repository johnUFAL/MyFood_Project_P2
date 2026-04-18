package br.ufal.ic.myfood.exceptions.Produtos;

public class ProdutoNaoCadastrado extends Exception{
    public ProdutoNaoCadastrado() {
        super("Produto nao cadastrado");
    }
    public ProdutoNaoCadastrado(String message) {super(message);}
}
