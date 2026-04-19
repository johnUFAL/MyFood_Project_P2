package br.ufal.ic.myfood;

import br.ufal.ic.myfood.controllers.ControladorDeUsuarios;
import br.ufal.ic.myfood.controllers.ControladorDeEmpresa;
import br.ufal.ic.myfood.controllers.ControladorDeProduto;
import br.ufal.ic.myfood.controllers.ControladorDePedidos;
import br.ufal.ic.myfood.exceptions.Empresas.*;
import br.ufal.ic.myfood.exceptions.Empresas.AtributoInvalido;
import br.ufal.ic.myfood.exceptions.Pedidos.ProdutoNaoEncontrado;
import br.ufal.ic.myfood.exceptions.Produtos.AtributoNaoExiste;
import br.ufal.ic.myfood.exceptions.Usuarios.*;
import br.ufal.ic.myfood.exceptions.Produtos.*;
import br.ufal.ic.myfood.exceptions.Pedidos.*;
import br.ufal.ic.myfood.models.DonoEmpresa;
import br.ufal.ic.myfood.models.Empresa;
import br.ufal.ic.myfood.models.Restaurante;
import br.ufal.ic.myfood.models.Usuario;
import br.ufal.ic.myfood.models.Produto;
import br.ufal.ic.myfood.models.Pedido;


import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Facade {

    private ControladorDeUsuarios controladorUsuarios;
    private ControladorDeEmpresa controladorDeEmpresa;
    private ControladorDeProduto controladorDeProduto;
    private ControladorDePedidos controladorDePedidos;

    public Facade() {
        carregarDados();
    }

    public void zerarSistema() {
        this.controladorUsuarios.zerar();
        this.controladorDeEmpresa.zerar();
        this.controladorDeProduto.zerar();
        this.controladorDePedidos.zerar();
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

    public int criarProduto(int empresa, String nome, float valor, String categoria) throws Exception {
        return this.controladorDeProduto.criarProduto(empresa, nome, valor, categoria);
    }

    public int criarPedido(int cliente, int empresa) throws  Exception {
        Usuario u = this.controladorUsuarios.buscarUsuarioPorId(cliente);
        if (u instanceof DonoEmpresa) {
            throw  new DonoEmpresaNaoPodePedido();
        }
        Empresa e = this.controladorDeEmpresa.buscarEmpresaPorId(empresa);
        return this.controladorDePedidos.criarPedido(u.getNome(), e.getNome());
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

    public void editarProduto(int produto, String nome, float valor, String categoria) throws Exception {
        this.controladorDeProduto.editarProduto(produto, nome, valor, categoria);
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

    public String getProduto(String nome, int empresa, String atributo) throws Exception {
        Produto p = this.controladorDeProduto.buscarProdutoPorNomeEEmpresa(nome, empresa);

        switch (atributo) {
            case "valor":
                return String.format(java.util.Locale.US, "%.2f", p.getValor());
            case "categoria":
                return p.getCategoria();
            case "empresa":
                return this.controladorDeEmpresa.buscarEmpresaPorId(empresa).getNome();
            default:
                throw new AtributoNaoExiste();
        }
    }

    public String listarProdutos(int empresa) throws Exception {
        try {
            this.controladorDeEmpresa.buscarEmpresaPorId(empresa);
        } catch (Exception e) {
            throw new EmpresaNaoEncontrada();
        }
        return this.controladorDeProduto.listarProdutos(empresa);
    }

    private Produto buscarProdutoPorNomeENomeDaEmpresa(String nomeProduto, String nomeEmpresa) throws Exception {
        for (Produto p : this.controladorDeProduto.getProduto().values()) {
            if (p.getNome().equals(nomeProduto)) {
                Empresa e = this.controladorDeEmpresa.buscarEmpresaPorId(p.getEmpresa());
                if (e.getNome().equals(nomeEmpresa)) {
                    return p;
                }
            }
        }
        throw new ProdutoNaoEncontrado();
    }

    public int getNumeroPedido(int cliente, int empresa, int indice) throws Exception {
        Usuario u = this.controladorUsuarios.buscarUsuarioPorId(cliente);
        Empresa e = this.controladorDeEmpresa.buscarEmpresaPorId(empresa);

        return this.controladorDePedidos.getNumeroPedido(u.getNome(), e.getNome(), indice);
    }

    public void adicionarProduto(int numero, int produto) throws Exception {
        Produto p = this.controladorDeProduto.buscarProdutoPorId(produto);
        Empresa e = this.controladorDeEmpresa.buscarEmpresaPorId(p.getEmpresa());

        this.controladorDePedidos.adicionarProduto(numero, e.getNome(), p.getNome(), p.getValor());
    }

    public void fecharPedido(int numero) throws Exception {
        this.controladorDePedidos.fecharPedido(numero);
    }

    public void removerProduto(int pedido, String produto) throws Exception {
        if (produto == null || produto.trim().isEmpty()) {
            throw new ProdutoInvalido();
        }

        Pedido ped = this.controladorDePedidos.buscarPedidoPorId(pedido);
        if (!ped.getEstado().equals("aberto")) {
            throw new RemoverPedidoFechado();
        }
        Produto p = buscarProdutoPorNomeENomeDaEmpresa(produto, ped.getEmpresa());

        this.controladorDePedidos.removerProduto(pedido, produto, p.getValor());
    }

    public String getPedidos(int numero, String atributo) throws Exception {
        if (atributo == null || atributo.trim().isEmpty()) throw new AtributoInvalido();

        Pedido p = this.controladorDePedidos.buscarPedidoPorId(numero);

        switch (atributo) {
            case "cliente":
                return p.getCliente();
            case "empresa":
                return p.getEmpresa();
            case "estado":
                return p.getEstado();
            case "produtos":
                StringBuilder sb = new StringBuilder("{[");
                for (int i = 0; i < p.getProdutos().size(); i++) {
                    sb.append(p.getProdutos().get(i));
                    if (i < p.getProdutos().size() - 1) sb.append(", ");
                }
                sb.append("]}");
                return sb.toString();
            case "valor":
                return String.format(java.util.Locale.US, "%.2f", p.getValor());
            default:
                throw new AtributoNaoExiste();
        }
    }

    private void salvarDados() {
        try (XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream("banco_myfood.xml")))) {
            encoder.writeObject(this.controladorUsuarios);
            encoder.writeObject(this.controladorDeEmpresa);
            encoder.writeObject(this.controladorDeProduto);
            encoder.writeObject(this.controladorDePedidos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void carregarDados() {
        try (XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream("banco_myfood.xml")))) {
            this.controladorUsuarios = (ControladorDeUsuarios) decoder.readObject();
            this.controladorDeEmpresa = (ControladorDeEmpresa) decoder.readObject();
            this.controladorDeProduto = (ControladorDeProduto) decoder.readObject();
            this.controladorDePedidos = (ControladorDePedidos) decoder.readObject();
        } catch (Exception e) {
            this.controladorUsuarios = new ControladorDeUsuarios();
            this.controladorDeEmpresa = new ControladorDeEmpresa();
            this.controladorDeProduto = new ControladorDeProduto();
            this.controladorDePedidos = new ControladorDePedidos();
        }
    }
}