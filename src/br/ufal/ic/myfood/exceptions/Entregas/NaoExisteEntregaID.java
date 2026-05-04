package br.ufal.ic.myfood.exceptions.Entregas;

public class NaoExisteEntregaID extends Exception{
    public NaoExisteEntregaID() {
        super("Nao existe entrega com esse id");
    }
}
