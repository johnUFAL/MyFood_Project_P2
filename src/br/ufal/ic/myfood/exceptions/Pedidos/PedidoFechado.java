package br.ufal.ic.myfood.exceptions.Pedidos;

public class PedidoFechado extends Exception {
    public PedidoFechado() {
        super("Nao e possivel adcionar produtos a um pedido fechado");
    }
    public PedidoFechado(String message) {
        super(message);
    }
}
