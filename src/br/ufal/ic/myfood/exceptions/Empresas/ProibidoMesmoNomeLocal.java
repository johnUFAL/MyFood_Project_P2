package br.ufal.ic.myfood.exceptions.Empresas;

public class ProibidoMesmoNomeLocal extends Exception {
    public ProibidoMesmoNomeLocal() {
        super("Proibido cadastrar duas empresas com o mesmo nome e local");
    }
    public ProibidoMesmoNomeLocal(String message) {super(message);}
}
