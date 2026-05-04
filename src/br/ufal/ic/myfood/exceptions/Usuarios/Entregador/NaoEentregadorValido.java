package br.ufal.ic.myfood.exceptions.Usuarios.Entregador;

public class NaoEentregadorValido extends Exception{
    public NaoEentregadorValido() {
        super("Nao e um entregador valido");
    }
}
