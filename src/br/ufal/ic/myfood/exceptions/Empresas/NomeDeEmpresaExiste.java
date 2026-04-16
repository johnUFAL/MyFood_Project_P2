package br.ufal.ic.myfood.exceptions.Empresas;

public class NomeDeEmpresaExiste extends Exception{
    public NomeDeEmpresaExiste() {
            super("Empresa com esse nome ja existe");
    }
    public NomeDeEmpresaExiste (String message) {super(message);}
}
