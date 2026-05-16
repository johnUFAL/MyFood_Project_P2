package br.ufal.ic.myfood;

import br.ufal.ic.myfood.controllers.*;
import br.ufal.ic.myfood.exceptions.*;
import br.ufal.ic.myfood.exceptions.Produtos.*;
import br.ufal.ic.myfood.exceptions.Pedidos.*;
import br.ufal.ic.myfood.exceptions.Empresas.*;
import br.ufal.ic.myfood.exceptions.Entregas.*;
import br.ufal.ic.myfood.exceptions.Usuarios.Entregador.NaoEentregadorValido;
import br.ufal.ic.myfood.exceptions.Usuarios.Entregador.UsuarioNaoEntregador;
import br.ufal.ic.myfood.models.*;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Ponto de entrada único do sistema MyFood.
 * Delega as operações aos controladores responsáveis sem conter regras de negócio próprias.
 */
public class Facade {

    private ControladorDeUsuarios controladorUsuarios;
    private ControladorDeEmpresa controladorDeEmpresa;
    private ControladorDeProduto controladorDeProduto;
    private ControladorDePedidos controladorDePedidos;
    private ControladorDeEntregas controladorDeEntregas;

    /**
     * Inicializa a Facade carregando os dados salvos em disco, se existirem.
     */
    public Facade() {
        carregarDados();
    }

    // =========================================================================
    // 1. MÉTODOS DE CICLO DE VIDA E SISTEMA
    // =========================================================================

    /**
     * Apaga todos os dados do sistema, devolvendo-o ao estado inicial.
     */
    public void zerarSistema() {
        this.controladorUsuarios.zerar();
        this.controladorDeEmpresa.zerar();
        this.controladorDeProduto.zerar();
        this.controladorDePedidos.zerar();
        if (this.controladorDeEntregas != null) {
            this.controladorDeEntregas.zerar();
        }
    }

    /**
     * Persiste os dados em disco e encerra o sistema.
     */
    public void encerrarSistema() {
        salvarDados();
    }

    // =========================================================================
    // 2. GERENCIAMENTO DE USUÁRIOS
    // =========================================================================

    /**
     * Cadastra um cliente.
     * @param nome Nome completo.
     * @param email E-mail de acesso (deve ser único).
     * @param senha Senha de acesso.
     * @param endereco Endereço de entrega padrão.
     * @throws Exception Se os dados forem inválidos ou o e-mail já estiver em uso.
     */
    public void criarUsuario(String nome, String email, String senha, String endereco) throws Exception {
        this.controladorUsuarios.criarCliente(nome, email, senha, endereco);
    }

    /**
     * Cadastra um dono de empresa.
     * @param nome Nome completo.
     * @param email E-mail de acesso (deve ser único).
     * @param senha Senha de acesso.
     * @param endereco Endereço do proprietário.
     * @param cpf CPF do proprietário.
     * @throws Exception Se os dados forem inválidos ou o CPF já estiver em uso.
     */
    public void criarUsuario(String nome, String email, String senha, String endereco, String cpf) throws Exception {
        this.controladorUsuarios.criarDono(nome, email, senha, endereco, cpf);
    }

    /**
     * Cadastra um entregador.
     * @param nome Nome completo.
     * @param email E-mail de acesso (deve ser único).
     * @param senha Senha de acesso.
     * @param endereco Endereço do entregador.
     * @param veiculo Tipo do veículo (ex: moto, bicicleta).
     * @param placa Placa do veículo.
     * @throws Exception Se os dados forem inválidos ou a placa já estiver cadastrada.
     */
    public void criarUsuario(String nome, String email, String senha, String endereco, String veiculo, String placa) throws Exception {
        this.controladorUsuarios.criarEntregador(nome, email, senha, endereco, veiculo, placa);
    }

    /**
     * Autentica um usuário e retorna seu ID.
     * @param email E-mail cadastrado.
     * @param senha Senha correspondente.
     * @return ID do usuário autenticado.
     * @throws Exception Se as credenciais estiverem incorretas ou em branco.
     */
    public int login(String email, String senha) throws Exception {
        return this.controladorUsuarios.login(email, senha);
    }

    /**
     * Retorna o valor de um atributo de um usuário.
     * @param id ID do usuário.
     * @param atributo Nome do atributo (ex: nome, email, cpf, veiculo, placa).
     * @return Valor do atributo como String.
     * @throws Exception Se o usuário ou atributo não existirem.
     */
    public String getAtributoUsuario(int id, String atributo) throws Exception {
        return this.controladorUsuarios.getAtributoUsuario(id, atributo);
    }

    // =========================================================================
    // 3. GERENCIAMENTO DE EMPRESAS
    // =========================================================================

    /**
     * Cadastra um restaurante.
     * @param tipoEmpresa Deve ser "restaurante".
     * @param dono ID do usuário dono.
     * @param nome Nome do restaurante.
     * @param endereco Endereço do restaurante.
     * @param tipoCozinha Tipo de culinária (ex: italiana, japonesa).
     * @return ID do restaurante criado.
     * @throws Exception Se o usuário não for dono de empresa ou o restaurante já existir.
     */
    public int criarEmpresa(String tipoEmpresa, int dono, String nome, String endereco, String tipoCozinha) throws Exception {
        Usuario usuario = this.controladorUsuarios.buscarUsuarioPorId(dono);
        return this.controladorDeEmpresa.criarEmpresa(tipoEmpresa, usuario, nome, endereco, tipoCozinha);
    }

    /**
     * Cadastra um mercado.
     * @param tipoEmpresa Deve ser "mercado".
     * @param dono ID do usuário dono.
     * @param nome Nome do mercado.
     * @param endereco Endereço do mercado.
     * @param abre Horário de abertura no formato HH:MM.
     * @param fecha Horário de fechamento no formato HH:MM.
     * @param tipoMercado Segmento do mercado (ex: supermercado, minimercado).
     * @return ID do mercado criado.
     * @throws Exception Se os horários ou o tipo de mercado forem inválidos.
     */
    public int criarEmpresa(String tipoEmpresa, int dono, String nome, String endereco, String abre, String fecha, String tipoMercado) throws Exception {
        Usuario usuario = this.controladorUsuarios.buscarUsuarioPorId(dono);
        return this.controladorDeEmpresa.criarMercado(tipoEmpresa, usuario, nome, endereco, abre, fecha, tipoMercado);
    }

    /**
     * Cadastra uma farmácia.
     * @param tipoEmpresa Deve ser "farmacia".
     * @param dono ID do usuário dono.
     * @param nome Nome da farmácia.
     * @param endereco Endereço da farmácia.
     * @param aberto24Horas Indica se funciona 24 horas.
     * @param numeroFuncionarios Número de funcionários.
     * @return ID da farmácia criada.
     * @throws Exception Se os dados forem inválidos.
     */
    public int criarEmpresa(String tipoEmpresa, int dono, String nome, String endereco, boolean aberto24Horas, int numeroFuncionarios) throws Exception{
        Usuario usuario = this.controladorUsuarios.buscarUsuarioPorId(dono);
        return this.controladorDeEmpresa.criarFarmacia(tipoEmpresa, usuario, nome, endereco, aberto24Horas, numeroFuncionarios);
    }

    /**
     * Altera os horários de funcionamento de um mercado.
     * @param mercado ID do mercado.
     * @param abre Novo horário de abertura.
     * @param fecha Novo horário de fechamento.
     * @throws Exception Se os horários forem inválidos ou a empresa não for um mercado.
     */
    public void alterarFuncionamento(int mercado, String abre, String fecha) throws Exception {
        this.controladorDeEmpresa.alterarFuncionamento(mercado, abre, fecha);
    }

    /**
     * Retorna a lista de empresas de um dono.
     * @param idDono ID do usuário dono.
     * @return String formatada com as empresas do usuário.
     * @throws Exception Se o usuário não for dono de empresa.
     */
    public String getEmpresasDoUsuario(int idDono) throws Exception {
        Usuario u = this.controladorUsuarios.buscarUsuarioPorId(idDono);
        if (!(u instanceof DonoEmpresa)) {
            throw new UsuarioNaoCriaEmpresa();
        }
        return this.controladorDeEmpresa.getEmpresasDoUsuario(idDono);
    }

    /**
     * Retorna o ID de uma empresa pelo nome e posição na lista do dono.
     * @param idDono ID do dono.
     * @param nome Nome da empresa.
     * @param indice Posição na lista (começa em 1).
     * @return ID da empresa.
     * @throws Exception Se a empresa não for encontrada.
     */
    public int getIdEmpresa(int idDono, String nome, int indice) throws Exception {
        return this.controladorDeEmpresa.getIdEmpresa(idDono, nome, indice);
    }

    /**
     * Retorna o valor de um atributo de uma empresa.
     * Atributos disponíveis variam por tipo: restaurante (tipoCozinha), mercado (abre, fecha, tipoMercado),
     * farmácia (aberto24Horas, numeroFuncionarios). Todos aceitam nome, endereco e dono.
     * @param idEmpresa ID da empresa.
     * @param atributo Nome do atributo desejado.
     * @return Valor do atributo como String.
     * @throws Exception Se o atributo não existir ou não se aplicar ao tipo da empresa.
     */
    public String getAtributoEmpresa(int idEmpresa, String atributo) throws Exception {
        Empresa e = this.controladorDeEmpresa.buscarEmpresaPorId(idEmpresa);

        if (atributo == null || atributo.trim().isEmpty()) {
            throw new AtributoInvalido();
        }

        return switch (atributo) {
            case "nome" -> e.getNome();
            case "endereco" -> e.getEndereco();
            case "tipoCozinha" -> {
                if (e instanceof Restaurante) yield ((Restaurante) e).getTipoCozinha();
                throw new AtributoInvalido();
            }
            case "dono" -> this.controladorUsuarios.buscarUsuarioPorId(e.getDono()).getNome();
            case "abre" -> {
                if (e instanceof Mercado) yield ((Mercado) e).getAbre();
                throw new AtributoInvalido();
            }
            case "fecha" -> {
                if (e instanceof Mercado) yield ((Mercado) e).getFecha();
                throw new AtributoInvalido();
            }
            case "tipoMercado" -> {
                if (e instanceof Mercado) yield ((Mercado) e).getTipoMercado();
                throw new AtributoInvalido();
            }
            case "aberto24Horas" -> {
                if (e instanceof Farmacia) yield String.valueOf(((Farmacia) e).getAberto24Horas());
                throw new AtributoInvalido();
            }
            case "numeroFuncionarios" -> {
                if (e instanceof Farmacia) yield String.valueOf(((Farmacia) e).getNumeroFuncionarios());
                throw new AtributoInvalido();
            }
            default -> throw new AtributoInvalido();
        };
    }

    // =========================================================================
    // 4. GERENCIAMENTO DE PRODUTOS
    // =========================================================================

    /**
     * Adiciona um produto ao catálogo de uma empresa.
     * @param empresa ID da empresa.
     * @param nome Nome do produto.
     * @param valor Preço unitário.
     * @param category Categoria do produto.
     * @return ID do produto criado.
     * @throws Exception Se já existir um produto com o mesmo nome na empresa ou o valor for inválido.
     */
    public int criarProduto(int empresa, String nome, float valor, String category) throws Exception {
        return this.controladorDeProduto.criarProduto(empresa, nome, valor, category);
    }

    /**
     * Atualiza os dados de um produto existente.
     * @param produto ID do produto.
     * @param nome Novo nome.
     * @param valor Novo preço.
     * @param categoria Nova categoria.
     * @throws Exception Se o produto não for encontrado ou os valores forem inválidos.
     */
    public void editarProduto(int produto, String nome, float valor, String categoria) throws Exception {
        this.controladorDeProduto.editarProduto(produto, nome, valor, categoria);
    }

    /**
     * Retorna um atributo de um produto buscado pelo nome e empresa.
     * @param nome Nome do produto.
     * @param empresa ID da empresa.
     * @param atributo Atributo desejado: valor, categoria ou empresa.
     * @return Valor do atributo como String.
     * @throws Exception Se o produto não for encontrado.
     */
    public String getProduto(String nome, int empresa, String atributo) throws Exception {
        Produto p = this.controladorDeProduto.buscarProdutoPorNomeEEmpresa(nome, empresa);

        return switch (atributo) {
            case "valor" -> String.format(java.util.Locale.US, "%.2f", p.getValor());
            case "categoria" -> p.getCategoria();
            case "empresa" -> this.controladorDeEmpresa.buscarEmpresaPorId(empresa).getNome();
            default -> throw new AtributoNaoExiste();
        };
    }

    /**
     * Retorna a lista de produtos de uma empresa.
     * @param empresa ID da empresa.
     * @return String formatada com os produtos cadastrados.
     * @throws Exception Se a empresa não for encontrada.
     */
    public String listarProdutos(int empresa) throws Exception {
        try {
            this.controladorDeEmpresa.buscarEmpresaPorId(empresa);
        } catch (Exception e) {
            throw new EmpresaNaoEncontrada();
        }
        return this.controladorDeProduto.listarProdutos(empresa);
    }

    /**
     * Busca um produto pelo nome e pelo nome da empresa a que pertence.
     * @param nomeProduto Nome do produto.
     * @param nomeEmpresa Nome da empresa.
     * @return Objeto Produto encontrado.
     * @throws Exception Se o produto não for encontrado.
     */
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

    // =========================================================================
    // 5. GERENCIAMENTO DE PEDIDOS
    // =========================================================================

    /**
     * Abre um pedido para um cliente em uma empresa.
     * @param cliente ID do cliente.
     * @param empresa ID da empresa.
     * @return Número do pedido criado.
     * @throws Exception Se o cliente for dono de empresa ou já tiver um pedido aberto na mesma empresa.
     */
    public int criarPedido(int cliente, int empresa) throws Exception {
        Usuario u = this.controladorUsuarios.buscarUsuarioPorId(cliente);
        if (u instanceof DonoEmpresa) {
            throw new DonoEmpresaNaoPodePedido();
        }
        Empresa e = this.controladorDeEmpresa.buscarEmpresaPorId(empresa);
        return this.controladorDePedidos.criarPedido(u.getNome(), e.getNome());
    }

    /**
     * Retorna o número de um pedido pelo cliente, empresa e posição na lista.
     * @param cliente ID do cliente.
     * @param empresa ID da empresa.
     * @param indice Posição na lista (começa em 1).
     * @return Número do pedido.
     * @throws Exception Se o pedido não for encontrado.
     */
    public int getNumeroPedido(int cliente, int empresa, int indice) throws Exception {
        Usuario u = this.controladorUsuarios.buscarUsuarioPorId(cliente);
        Empresa e = this.controladorDeEmpresa.buscarEmpresaPorId(empresa);
        return this.controladorDePedidos.getNumeroPedido(u.getNome(), e.getNome(), indice);
    }

    /**
     * Adiciona um produto a um pedido aberto.
     * @param numero Número do pedido.
     * @param produto ID do produto.
     * @throws Exception Se o pedido estiver fechado ou o produto pertencer a outra empresa.
     */
    public void adicionarProduto(int numero, int produto) throws Exception {
        Produto p = this.controladorDeProduto.buscarProdutoPorId(produto);
        Empresa e = this.controladorDeEmpresa.buscarEmpresaPorId(p.getEmpresa());
        this.controladorDePedidos.adicionarProduto(numero, e.getNome(), p.getNome(), p.getValor());
    }

    /**
     * Remove uma unidade de um produto de um pedido aberto.
     * @param pedido Número do pedido.
     * @param produto Nome do produto a remover.
     * @throws Exception Se o pedido estiver fechado ou o produto não estiver no pedido.
     */
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

    /**
     * Fecha um pedido, impedindo novas edições e iniciando a preparação.
     * @param numero Número do pedido.
     * @throws Exception Se o pedido não existir ou já estiver fechado.
     */
    public void fecharPedido(int numero) throws Exception {
        this.controladorDePedidos.fecharPedido(numero);
    }

    /**
     * Marca um pedido em preparação como pronto para entrega.
     * @param numero Número do pedido.
     * @throws Exception Se o pedido não estiver no estado "preparando".
     */
    public void liberarPedido(int numero) throws Exception {
        Pedido p = this.controladorDePedidos.buscarPedidoPorId(numero);
        if (p.getEstado().equals("pronto")) {
            throw new PedidoJaLiberado();
        }
        if (!p.getEstado().equals("preparando")) {
            throw new ProdutoNaoEstaSendoPreparado();
        }
        p.setEstado("pronto");
    }

    /**
     * Retorna o número do pedido mais antigo no estado "pronto" disponível para o entregador.
     * Pedidos de farmácia têm prioridade sobre os demais.
     * @param idEntregador ID do entregador.
     * @return Número do pedido disponível.
     * @throws Exception Se não houver pedidos disponíveis ou o entregador não estiver vinculado a nenhuma empresa.
     */
    public int obterPedido(int idEntregador) throws Exception {
        Usuario u = this.controladorUsuarios.buscarUsuarioPorId(idEntregador);
        if (!(u instanceof Entregador ent)) {
            throw new UsuarioNaoEntregador();
        }

        if (ent.getEmpresas().isEmpty()) {
            throw new EntregadorEmNenhumaEmpresa();
        }

        Pedido pedidoFarmacia = null;
        Pedido pedidoNormal = null;

        for (Pedido p : this.controladorDePedidos.getPedido().values()) {
            if (p.getEstado().equals("pronto")) {
                Empresa emp = this.controladorDeEmpresa.buscarEmpresaPorNome(p.getEmpresa());

                if (ent.getEmpresas().contains(emp.getId())) {
                    if (emp instanceof Farmacia) {
                        if (pedidoFarmacia == null) pedidoFarmacia = p;
                    } else {
                        if (pedidoNormal == null) pedidoNormal = p;
                    }
                }
            }
        }

        if (pedidoFarmacia != null) return pedidoFarmacia.getNumero();
        if (pedidoNormal != null) return pedidoNormal.getNumero();

        throw new NaoExistePedidoEntrega();
    }

    /**
     * Retorna o valor de um atributo de um pedido.
     * @param numero Número do pedido.
     * @param atributo Atributo desejado: cliente, empresa, estado, produtos ou valor.
     * @return Valor do atributo como String.
     * @throws Exception Se o atributo não existir.
     */
    public String getPedidos(int numero, String atributo) throws Exception {
        if (atributo == null || atributo.trim().isEmpty()) throw new AtributoInvalido();

        Pedido p = this.controladorDePedidos.buscarPedidoPorId(numero);

        return switch (atributo) {
            case "cliente" -> p.getCliente();
            case "empresa" -> p.getEmpresa();
            case "estado" -> p.getEstado();
            case "produtos" -> {
                StringBuilder sb = new StringBuilder("{[");
                for (int i = 0; i < p.getProdutos().size(); i++) {
                    sb.append(p.getProdutos().get(i));
                    if (i < p.getProdutos().size() - 1) sb.append(", ");
                }
                sb.append("]}");
                yield sb.toString();
            }
            case "valor" -> String.format(java.util.Locale.US, "%.2f", p.getValor());
            default -> throw new AtributoNaoExiste();
        };
    }

    // =========================================================================
    // 6. LOGÍSTICA DE ENTREGAS E VÍNCULOS
    // =========================================================================

    /**
     * Vincula um entregador a uma empresa, permitindo que ele receba pedidos dela.
     * @param idEmpresa ID da empresa.
     * @param idEntregador ID do entregador.
     * @throws Exception Se o usuário não for entregador ou a empresa não existir.
     */
    public void cadastrarEntregador(int idEmpresa, int idEntregador) throws Exception {
        Usuario u = this.controladorUsuarios.buscarUsuarioPorId(idEntregador);
        if (!(u instanceof Entregador ent)) {
            throw new UsuarioNaoEntregador();
        }

        this.controladorDeEmpresa.buscarEmpresaPorId(idEmpresa);

        if (!ent.getEmpresas().contains(idEmpresa)) {
            ent.getEmpresas().add(idEmpresa);
        }
    }

    /**
     * Registra uma entrega, associando o entregador ao pedido e marcando-o como "entregando".
     * Se o destino for vazio, usa o endereço cadastrado do entregador.
     * @param pedido Número do pedido (deve estar no estado "pronto").
     * @param entregador ID do entregador.
     * @param destino Endereço de entrega.
     * @return ID da entrega criada.
     * @throws Exception Se o pedido não estiver pronto ou o entregador já tiver uma entrega em andamento.
     */
    public int criarEntrega(int pedido, int entregador, String destino) throws Exception {
        Pedido p = this.controladorDePedidos.buscarPedidoPorId(pedido);
        if (!p.getEstado().equals("pronto")) {
            throw new PedidoNaoProntoEntrega();
        }

        Usuario u = this.controladorUsuarios.buscarUsuarioPorId(entregador);
        if (!(u instanceof Entregador)) {
            throw new NaoEentregadorValido();
        }

        for (Entrega ent : this.controladorDeEntregas.getEntrega().values()) {
            if (ent.getEntregador() == entregador) {
                Pedido pedAnterior = this.controladorDePedidos.buscarPedidoPorId(ent.getPedido());
                if (pedAnterior.getEstado().equals("entregando")) {
                    throw new EntregadroEmEntrega();
                }
            }
        }

        String dest = (destino == null || destino.trim().isEmpty()) ? u.getEndereco() : destino;
        int idEntrega = this.controladorDeEntregas.criarEntrega(p, entregador, dest);
        p.setEstado("entregando");

        return idEntrega;
    }

    /**
     * Retorna o valor de um atributo de uma entrega.
     * @param id ID da entrega.
     * @param atributo Atributo desejado: cliente, empresa, pedido, entregador, destino ou produtos.
     * @return Valor do atributo como String.
     * @throws Exception Se o atributo não existir.
     */
    public String getEntrega(int id, String atributo) throws Exception {
        if (atributo == null || atributo.trim().isEmpty()) throw new AtributoInvalido();

        Entrega e = this.controladorDeEntregas.buscarEntregaPorId(id);

        return switch (atributo) {
            case "cliente" -> e.getCliente();
            case "empresa" -> e.getEmpresa();
            case "pedido" -> String.valueOf(e.getPedido());
            case "entregador" -> this.controladorUsuarios.buscarUsuarioPorId(e.getEntregador()).getNome();
            case "destino" -> e.getDestino();
            case "produtos" -> {
                StringBuilder sb = new StringBuilder("{[");
                for (int i = 0; i < e.getProdutos().size(); i++) {
                    sb.append(e.getProdutos().get(i));
                    if (i < e.getProdutos().size() - 1) sb.append(", ");
                }
                sb.append("]}");
                yield sb.toString();
            }
            default -> throw new AtributoNaoExiste();
        };
    }

    /**
     * Retorna o ID da entrega associada a um pedido.
     * @param pedido Número do pedido.
     * @return ID da entrega.
     * @throws Exception Se não houver entrega para o pedido informado.
     */
    public int getIdEntrega(int pedido) throws Exception {
        return this.controladorDeEntregas.getIdEntrega(pedido);
    }

    /**
     * Conclui uma entrega, marcando o pedido como "entregue".
     * @param idEntrega ID da entrega.
     * @throws Exception Se a entrega não for encontrada.
     */
    public void entregar(int idEntrega) throws Exception {
        try {
            Entrega e = this.controladorDeEntregas.buscarEntregaPorId(idEntrega);
            Pedido p = this.controladorDePedidos.buscarPedidoPorId(e.getPedido());
            p.setEstado("entregue");
        } catch (Exception ex) {
            throw new NaoExisteNadaEntregaID();
        }
    }

    /**
     * Retorna os e-mails dos entregadores vinculados a uma empresa.
     * @param idEmpresa ID da empresa.
     * @return String formatada com os e-mails dos entregadores.
     * @throws Exception Se a empresa não for encontrada.
     */
    public String getEntregadores(int idEmpresa) throws Exception {
        this.controladorDeEmpresa.buscarEmpresaPorId(idEmpresa);

        StringBuilder sb = new StringBuilder("{[");
        boolean primeiro = true;

        for (Usuario u : this.controladorUsuarios.getUsuarios().values()) {
            if (u instanceof Entregador ent) {
                if (ent.getEmpresas().contains(idEmpresa)) {
                    if (!primeiro) sb.append(", ");
                    sb.append(ent.getEmail());
                    primeiro = false;
                }
            }
        }
        sb.append("]}");
        return sb.toString();
    }

    /**
     * Retorna as empresas às quais um entregador está vinculado, com nome e endereço de cada uma.
     * @param idEntregador ID do entregador.
     * @return String formatada com as empresas vinculadas.
     * @throws Exception Se o usuário não for entregador.
     */
    public String getEmpresas(int idEntregador) throws Exception {
        Usuario u = this.controladorUsuarios.buscarUsuarioPorId(idEntregador);
        if (!(u instanceof Entregador ent)) {
            throw new UsuarioNaoEntregador();
        }

        StringBuilder sb = new StringBuilder("{[");
        boolean primeiro = true;

        for (Integer idEmp : ent.getEmpresas()) {
            Empresa emp = this.controladorDeEmpresa.buscarEmpresaPorId(idEmp);
            if (!primeiro) sb.append(", ");
            sb.append("[").append(emp.getNome()).append(", ").append(emp.getEndereco()).append("]");
            primeiro = false;
        }
        sb.append("]}");
        return sb.toString();
    }

    // =========================================================================
    // 7. PERSISTÊNCIA DE DADOS (INFRAESTRUTURA INTEGRADA)
    // =========================================================================

    private void salvarDados() {
        try (XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream("banco_myfood.xml")))) {
            encoder.writeObject(this.controladorUsuarios);
            encoder.writeObject(this.controladorDeEmpresa);
            encoder.writeObject(this.controladorDeProduto);
            encoder.writeObject(this.controladorDePedidos);
            encoder.writeObject(this.controladorDeEntregas);
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
            this.controladorDeEntregas = (ControladorDeEntregas) decoder.readObject();
        } catch (Exception e) {
        } finally {
            if (this.controladorUsuarios == null) this.controladorUsuarios = new ControladorDeUsuarios();
            if (this.controladorDeEmpresa == null) this.controladorDeEmpresa = new ControladorDeEmpresa();
            if (this.controladorDeProduto == null) this.controladorDeProduto = new ControladorDeProduto();
            if (this.controladorDePedidos == null) this.controladorDePedidos = new ControladorDePedidos();
            if (this.controladorDeEntregas == null) this.controladorDeEntregas = new ControladorDeEntregas();
        }
    }
}