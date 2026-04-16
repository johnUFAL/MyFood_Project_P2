package br.ufal.ic.myfood.exceptions.Empresas;

public class NaoExisteEmpresaComNome extends Exception{
    public NaoExisteEmpresaComNome() {
        super("Nao existe empresa com esse nome");
    }
    public NaoExisteEmpresaComNome(String message) {super(message);}
}
