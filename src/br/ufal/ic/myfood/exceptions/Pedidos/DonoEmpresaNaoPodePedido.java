package br.ufal.ic.myfood.exceptions.Pedidos;

public class DonoEmpresaNaoPodePedido extends Exception {
    public DonoEmpresaNaoPodePedido() {
        super("Dono de empresa nao pode fazer um pedido");
    }
    public DonoEmpresaNaoPodePedido(String message) {super(message);}
}
