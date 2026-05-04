package br.ufal.ic.myfood.exceptions.Entregas;

public class NaoExisteNadaEntregaID extends Exception{
    public NaoExisteNadaEntregaID() {
        super("Nao existe nada para ser entregue com esse id");
    }
}
