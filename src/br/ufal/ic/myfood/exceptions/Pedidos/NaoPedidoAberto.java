package br.ufal.ic.myfood.exceptions.Pedidos;

public class NaoPedidoAberto extends Exception {
    public NaoPedidoAberto() {
        super("Nao existe pedido em aberto");
    }
    public NaoPedidoAberto(String message) {
        super(message);
    }
}
