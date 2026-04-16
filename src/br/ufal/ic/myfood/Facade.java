package br.ufal.ic.myfood;

import br.ufal.ic.myfood.controllers.ControladorDeUsuarios;
import br.ufal.ic.myfood.controllers.ControladorDeEmpresa;
import br.ufal.ic.myfood.exceptions.Empresas.UsuarioNaoCriaEmpresa;
import br.ufal.ic.myfood.exceptions.Users.AtributoInvalido;
import br.ufal.ic.myfood.models.DonoEmpresa;
import br.ufal.ic.myfood.models.Empresa;
import br.ufal.ic.myfood.models.Restaurante;
import br.ufal.ic.myfood.models.Usuario;
import br.ufal.ic.myfood.exceptions.Users.*;
import br.ufal.ic.myfood.exceptions.Empresas.*;


import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Facade {

    private ControladorDeUsuarios controladorUsuarios;
    private ControladorDeEmpresa controladorDeEmpresa;

    public Facade() {
        carregarDados();
    }

    public void zerarSistema() {
        this.controladorUsuarios.zerar();
        this.controladorDeEmpresa.zerar();
    }

    public void encerrarSistema() {
        salvarDados();
    }

    public void criarUsuario(String nome, String email, String senha, String endereco) throws Exception {
        this.controladorUsuarios.criarCliente(nome, email, senha, endereco);
    }

    public void criarUsuario(String nome, String email, String senha, String endereco, String cpf) throws Exception {
        this.controladorUsuarios.criarDono(nome, email, senha, endereco, cpf);
    }

    public int criarEmpresa(String tipoEmpresa, int dono, String nome, String endereco, String tipoCozinha) throws Exception {
        Usuario usuario = this.controladorUsuarios.buscarUsuarioPorId(dono);
        return this.controladorDeEmpresa.criarEmpresa(tipoEmpresa, usuario, nome, endereco, tipoCozinha);
    }

    public int login(String email, String senha) throws Exception {
        return this.controladorUsuarios.login(email, senha);
    }

    public String getAtributoUsuario(int id, String atributo) throws Exception {
        return this.controladorUsuarios.getAtributoUsuario(id, atributo);
    }

    public String getEmpresasDoUsuario(int idDono) throws Exception {
        Usuario u = this.controladorUsuarios.buscarUsuarioPorId(idDono);

        if (!(u instanceof DonoEmpresa)) {
            throw new UsuarioNaoCriaEmpresa();
        }
        return this.controladorDeEmpresa.getEmpresasDoUsuario(idDono);
    }

    public int getIdEmpresa(int idDono, String nome, int indice) throws Exception {
        return this.controladorDeEmpresa.getIdEmpresa(idDono, nome, indice);
    }

    public String getAtributoEmpresa(int idEmpresa, String atributo) throws Exception {
        Empresa e = this.controladorDeEmpresa.buscarEmpresaPorId(idEmpresa);

        if (atributo == null || atributo.trim().isEmpty()) {
            throw new AtributoInvalido();
        }

        switch (atributo) {
            case "nome":
                return e.getNome();
            case "endereco":
                return e.getEndereco();
            case "tipoCozinha":
                if (e instanceof Restaurante) {
                    return ((Restaurante) e).getTipoCozinha();
                }
                throw new AtributoInvalido();
            case "dono":
                return this.controladorUsuarios.buscarUsuarioPorId(e.getDono()).getNome();
            default:
                throw new AtributoInvalido();
        }
    }

    private void salvarDados() {
        try (XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream("banco_myfood.xml")))) {
            encoder.writeObject(this.controladorUsuarios);
            encoder.writeObject((this.controladorDeEmpresa));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void carregarDados() {
        try (XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream("banco_myfood.xml")))) {
            this.controladorUsuarios = (ControladorDeUsuarios) decoder.readObject();
            this.controladorDeEmpresa = (ControladorDeEmpresa) decoder.readObject();
        } catch (Exception e) {
            this.controladorUsuarios = new ControladorDeUsuarios();
            this.controladorDeEmpresa = new ControladorDeEmpresa();
        }
    }
}