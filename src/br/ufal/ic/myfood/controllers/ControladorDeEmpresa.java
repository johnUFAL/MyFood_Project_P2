package br.ufal.ic.myfood.controllers;

import br.ufal.ic.myfood.exceptions.Empresas.*;
import br.ufal.ic.myfood.exceptions.Empresas.Mercado.*;
import br.ufal.ic.myfood.models.*;

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

    private void validarDadosBase(Usuario dono, String nome, String endereco) throws Exception {
        if (!(dono instanceof DonoEmpresa)) throw new UsuarioNaoCriaEmpresa();
        if (nome == null || nome.trim().isEmpty()) throw new NomeInvalido();
        if (endereco == null || endereco.trim().isEmpty()) throw new EnderecoInvalido();
    }

    private void validarDuplicidade(Usuario dono, String nome, String endereco) throws Exception {
        for (Empresa e : this.empresa.values()) {
            if (e.getNome().equals(nome)) {
                if (e.getDono() != dono.getId()) throw new NomeDeEmpresaExiste();
                if (e.getEndereco().equals(endereco)) throw new ProibidoMesmoNomeLocal();
            }
        }
    }

    public int criarEmpresa(String tipoEmpresa, Usuario dono, String nome, String endereco, String tipoCozinha) throws Exception {
        validarDadosBase(dono, nome, endereco);
        validarDuplicidade(dono, nome, endereco);

        int id = gerarId();

        if (tipoEmpresa.equals("restaurante")) {
            Restaurante restaurante = new Restaurante(id, dono.getId(), nome, endereco, tipoCozinha);
            this.empresa.put(id, restaurante);
        }
        return id;
    }

    public int criarMercado(String tipoEmpresa, Usuario dono, String nome, String endereco, String abre, String fecha, String tipoMercado) throws Exception {
        if (tipoEmpresa == null || !tipoEmpresa.equals("mercado")) throw new TipoEmpresaInvalido();

        validarDadosBase(dono, nome, endereco);

        Mercado.validarHorario(abre, fecha);

        if (tipoMercado == null || (!tipoMercado.equals("supermercado") && !tipoMercado.equals("minimercado") && !tipoMercado.equals("atacadista"))) {
            throw new TipoMercadoInvalido();
        }

        validarDuplicidade(dono, nome, endereco);

        int id = gerarId();
        Mercado mercado = new Mercado(id, dono.getId(), nome, endereco, abre, fecha, tipoMercado);
        this.empresa.put(id, mercado);
        return id;
    }

    public int criarFarmacia(String tipoEmpresa, Usuario dono, String nome, String endereco, boolean aberto24Horas, int numeroFuncionarios) throws Exception {
        if (tipoEmpresa == null || !tipoEmpresa.equals("farmacia")) {
            throw new TipoEmpresaInvalido();
        }

        validarDadosBase(dono, nome, endereco);
        validarDuplicidade(dono, nome, endereco);

        int id = gerarId();
        Farmacia farmacia = new Farmacia(id, dono.getId(), nome, endereco, aberto24Horas, numeroFuncionarios);
        this.empresa.put(id, farmacia);
        return id;
    }

    public void alterarFuncionamento(int idMercado, String abre, String fecha) throws Exception {
        Empresa e = buscarEmpresaPorId(idMercado);

        if (!(e instanceof Mercado)) {
            throw new NaoMercadoValido();
        }

        Mercado.validarHorario(abre, fecha);

        Mercado m = (Mercado) e;
        m.setAbre(abre);
        m.setFecha(fecha);
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

    public int getIdEmpresa(int idDono, String nome, int indice) throws Exception {
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

    public Empresa buscarEmpresaPorId(int id) throws Exception {
        if (!this.empresa.containsKey(id)) {
            throw new EmpresaNaoCadastrada();
        }
        return this.empresa.get(id);
    }

    public Empresa buscarEmpresaPorNome(String nome) throws Exception {
        for (Empresa e : this.empresa.values()) {
            if (e.getNome().equals(nome)) {
                return e;
            }
        }
        throw new EmpresaNaoCadastrada();
    }
}