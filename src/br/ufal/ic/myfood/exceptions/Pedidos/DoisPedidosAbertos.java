package br.ufal.ic.myfood.exceptions.Pedidos;

public class DoisPedidosAbertos extends Exception {
    public DoisPedidosAbertos() {
        super("Nao e permitido ter dois pedidos em aberto para a mesma empresa");
    }
    public DoisPedidosAbertos(String message) {
        super(message);
    }
}
