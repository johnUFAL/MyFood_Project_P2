package br.ufal.ic.myfood.exceptions.Entregas;

public class PedidoNaoProntoEntrega extends Exception{
    public PedidoNaoProntoEntrega() {
        super("Pedido nao esta pronto para entrega");
    }
}
