package br.ufal.ic.myfood.exceptions.Empresas.Mercado;

public class NaoMercadoValido extends Exception {
    public NaoMercadoValido() {
        super("Nao e um mercado valido");
    }
}
