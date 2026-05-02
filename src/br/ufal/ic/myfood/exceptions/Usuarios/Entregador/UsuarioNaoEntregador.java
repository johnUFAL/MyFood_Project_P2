package br.ufal.ic.myfood.exceptions.Usuarios.Entregador;

public class UsuarioNaoEntregador extends Exception{
    public UsuarioNaoEntregador() {
        super("Usuario nao e um entregador");
    }
}
