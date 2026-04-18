package br.ufal.ic.myfood.exceptions.Produtos;

public class ProibidoMesmoNomeLocal extends Exception{
    public ProibidoMesmoNomeLocal() {
        super("Ja existe um produto com esse nome para essa empresa");
    }
    public ProibidoMesmoNomeLocal(String message) {super(message);}
}
