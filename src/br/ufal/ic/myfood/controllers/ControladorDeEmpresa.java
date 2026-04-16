package br.ufal.ic.myfood.controllers;
import br.ufal.ic.myfood.models.DonoEmpresa;
import br.ufal.ic.myfood.models.Empresa;
import br.ufal.ic.myfood.models.Restaurante;
import br.ufal.ic.myfood.exceptions.Empresas.*;
import br.ufal.ic.myfood.models.Usuario;

import java.util.Map;
import java.util.LinkedHashMap;

public class ControladorDeEmpresa {
    private Map<Integer, Empresa> empresa;
    private int proximoId;

    public ControladorDeEmpresa() {
        this.empresa = new LinkedHashMap<>();
        this.proximoId = 1;
    }

    public Map<Integer, Empresa> getEmpresa() {return empresa;}
    public void setEmpresa(Map<Integer, Empresa> empresa) {this.empresa = empresa;}

    public int getProximoId() {return proximoId;}
    public void setProximoId(int proximoId) {this.proximoId = proximoId;}

    public void zerar() {
        this.empresa.clear();
        this.proximoId = 1;
    }

    public int gerarId() {return this.proximoId++;}

    public int criarEmpresa(String tipoEmpresa, Usuario dono, String nome, String endereco, String tipoCozinha) throws Exception {
        if (!(dono instanceof DonoEmpresa)) {
            throw new UsuarioNaoCriaEmpresa();
        }

        for (Empresa e : this.empresa.values()) {
            if (e.getNome().equals(nome)) {
                if (e.getDono() != dono.getId()) {
                    throw new NomeDeEmpresaExiste();
                }
                if (e.getEndereco().equals(endereco)) {
                    throw new ProibidoMesmoNomeLocal();
                }
            }
        }

        int id = gerarId();

        if (tipoEmpresa.equals("restaurante")) {
            Restaurante restaurante = new Restaurante(id, dono.getId(), nome, endereco, tipoCozinha);
            this.empresa.put(id, restaurante);
        }
        return id;
    }

    public String getEmpresasDoUsuario(int idDono) {
        StringBuilder resultado = new StringBuilder("{[");
        boolean primeiro = true;

        for (Empresa e : this.empresa.values()) {
            if (e.getDono()== idDono) {
                if(!primeiro) {
                    resultado.append(", ");
                }
                resultado.append("[").append(e.getNome()).append(", ").append(e.getEndereco()).append("]");
                primeiro = false;
            }
        }
        resultado.append("]}");
        return resultado.toString();
    }

    public int getIdEmpresa(int idDono, String nome, int indice) throws NomeInvalido, IndiceInvalido, NaoExisteEmpresaComNome, IndiceMaiorQueEsperado {
        if (nome == null || nome.trim().isEmpty()) {
            throw new NomeInvalido();
        }
        if (indice < 0) {
            throw new IndiceInvalido();
        }

        int cont = 0;
        boolean encotrouNome = false;

        for (Empresa e: this.empresa.values()) {
            if (e.getDono() == idDono && e.getNome().equals(nome)) {
                encotrouNome = true;
                if (cont == indice) {
                    return e.getId();
                }
                cont++;
            }
        }

        if (!encotrouNome) {
            throw new NaoExisteEmpresaComNome();
        }
        throw new IndiceMaiorQueEsperado();
    }

    public Empresa buscarEmpresaPorId(int id) throws  Exception {
        if (!this.empresa.containsKey(id)) {
            throw new EmpresaNaoCadastrada();
        }
        return this.empresa.get(id);
    }
}


