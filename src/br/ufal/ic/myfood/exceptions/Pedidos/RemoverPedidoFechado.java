package br.ufal.ic.myfood.exceptions.Pedidos;

public class RemoverPedidoFechado extends Exception {
    public RemoverPedidoFechado() {
        super("Nao e possivel remover produtos de um pedido fechado");
    }
    public RemoverPedidoFechado(String message) {
        super(message);
    }
}
