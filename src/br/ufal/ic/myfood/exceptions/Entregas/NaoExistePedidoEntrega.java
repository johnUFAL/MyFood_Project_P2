package br.ufal.ic.myfood.exceptions.Entregas;

public class NaoExistePedidoEntrega extends Exception{
    public NaoExistePedidoEntrega() {
        super("Nao existe pedido para entrega");
    }
}

