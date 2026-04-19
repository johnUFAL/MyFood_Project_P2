package br.ufal.ic.myfood.exceptions.Pedidos;

public class PedidoNaoEncontrado extends Exception {
    public PedidoNaoEncontrado() {
        super("Pedido nao encontrado");
    }
    public PedidoNaoEncontrado(String message) {
        super(message);
    }
}
