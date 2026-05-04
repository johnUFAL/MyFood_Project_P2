package br.ufal.ic.myfood.exceptions.Entregas;

public class PedidoJaLiberado extends Exception{
    public PedidoJaLiberado() {
        super("Pedido ja liberado");
    }
}
