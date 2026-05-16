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
 * Classe de Fachada (Facade) que centraliza os pontos de entrada do sistema MyFood.
 * Orquestra as interações entre os diferentes controladores de domínio sem gerenciar regras de negócio diretamente.
 */
public class Facade {

    private ControladorDeUsuarios controladorUsuarios;
    private ControladorDeEmpresa controladorDeEmpresa;
    private ControladorDeProduto controladorDeProduto;
    private ControladorDePedidos controladorDePedidos;
    private ControladorDeEntregas controladorDeEntregas;

    /**
     * Construtor da Facade. Inicializa o sistema carregando os dados persistidos.
     */
    public Facade() {
        carregarDados();
    }

    // =========================================================================
    // 1. MÉTODOS DE CICLO DE VIDA E SISTEMA
    // =========================================================================

    /**
     * Reseta completamente o estado de todos os controladores do sistema.
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
     * Finaliza a execução do sistema garantindo a persistência correta dos dados em disco.
     */
    public void encerrarSistema() {
        salvarDados();
    }

    // =========================================================================
    // 2. GERENCIAMENTO DE USUÁRIOS
    // =========================================================================

    /**
     * Cria um usuário do tipo Cliente.
     * @param nome Nome do cliente.
     * @param email E-mail único do cliente.
     * @param senha Senha de acesso.
     * @param endereco Endereço de entrega padrão.
     * @throws Exception Caso os dados sejam inválidos ou o e-mail já esteja em uso.
     */
    public void criarUsuario(String nome, String email, String senha, String endereco) throws Exception {
        this.controladorUsuarios.criarCliente(nome, email, senha, endereco);
    }

    /**
     * Cria um usuário do tipo Dono de Empresa.
     * @param nome Nome do proprietário.
     * @param email E-mail único de acesso.
     * @param senha Senha de acesso.
     * @param endereco Endereço residencial/comercial.
     * @param cpf CPF do proprietário.
     * @throws Exception Caso os dados base ou o CPF sejam inválidos.
     */
    public void criarUsuario(String nome, String email, String senha, String endereco, String cpf) throws Exception {
        this.controladorUsuarios.criarDono(nome, email, senha, endereco, cpf);
    }

    /**
     * Cria um usuário do tipo Entregador.
     * @param nome Nome do entregador.
     * @param email E-mail único de acesso.
     * @param senha Senha de acesso.
     * @param endereco Endereço do entregador.
     * @param veiculo Tipo de veículo utilizado.
     * @param placa Placa de identificação do veículo.
     * @throws Exception Caso o veículo, a placa ou os dados genéricos sejam inválidos.
     */
    public void criarUsuario(String nome, String email, String senha, String endereco, String veiculo, String placa) throws Exception {
        this.controladorUsuarios.criarEntregador(nome, email, senha, endereco, veiculo, placa);
    }

    /**
     * Autentica um usuário no sistema com base nas credenciais informadas.
     * @param email E-mail registrado.
     * @param senha Senha correspondente.
     * @return O identificador único do usuário logado.
     * @throws Exception Se as credenciais estiverem incorretas ou em branco.
     */
    public int login(String email, String senha) throws Exception {
        return this.controladorUsuarios.login(email, senha);
    }

    /**
     * Recupera o valor de um determinado atributo de um usuário cadastrado.
     * @param id Identificador do usuário.
     * @param atributo Nome do atributo desejado (ex: nome, email, cpf, placa, veiculo).
     * @return Uma representação em string do valor do atributo solicitado.
     * @throws Exception Caso o usuário ou o atributo não existam.
     */
    public String getAtributoUsuario(int id, String atributo) throws Exception {
        return this.controladorUsuarios.getAtributoUsuario(id, atributo);
    }

    // =========================================================================
    // 3. GERENCIAMENTO DE EMPRESAS
    // =========================================================================

    /**
     * Cadastra uma nova Empresa do tipo Restaurante.
     * @param tipoEmpresa Tipo da empresa ("restaurante").
     * @param dono ID do usuário proprietário.
     * @param nome Nome do estabelecimento.
     * @param endereco Endereço de localização do restaurante.
     * @param tipoCozinha Especialidade gastronômica culinária do local.
     * @return O ID único da empresa gerada.
     * @throws Exception Se o usuário informado não for um dono corporativo ou se o local já existir.
     */
    public int criarEmpresa(String tipoEmpresa, int dono, String nome, String endereco, String tipoCozinha) throws Exception {
        Usuario usuario = this.controladorUsuarios.buscarUsuarioPorId(dono);
        return this.controladorDeEmpresa.criarEmpresa(tipoEmpresa, usuario, nome, endereco, tipoCozinha);
    }

    /**
     * Cadastra uma nova Empresa do tipo Mercado.
     * @param tipoEmpresa Tipo da empresa ("mercado").
     * @param dono ID do usuário proprietário.
     * @param nome Nome do mercado.
     * @param endereco Endereço de localização.
     * @param abre Horário de abertura (HH:MM).
     * @param fecha Horário de encerramento das atividades (HH:MM).
     * @param tipoMercado Segmentação de mercado (supermercado, minimercado, atacadista).
     * @return O ID único do mercado gerado.
     * @throws Exception Se os horários ou o tipo de mercado forem inválidos.
     */
    public int criarEmpresa(String tipoEmpresa, int dono, String nome, String endereco, String abre, String fecha, String tipoMercado) throws Exception {
        Usuario usuario = this.controladorUsuarios.buscarUsuarioPorId(dono);
        return this.controladorDeEmpresa.criarMercado(tipoEmpresa, usuario, nome, endereco, abre, fecha, tipoMercado);
    }

    /**
     * Cadastra uma nova Empresa do tipo Farmácia.
     * @param tipoEmpresa Tipo da empresa ("farmacia").
     * @param dono ID do usuário proprietário.
     * @param nome Nome da farmácia.
     * @param endereco Endereço do estabelecimento físico.
     * @param aberto24Horas Estado indicativo se funciona continuamente.
     * @param numeroFuncionarios Quantidade total de colaboradores ativos.
     * @return O ID único da farmácia gerada.
     * @throws Exception Se os dados corporativos forem inconsistentes.
     */
    public int criarEmpresa(String tipoEmpresa, int dono, String nome, String endereco, boolean aberto24Horas, int numeroFuncionarios) throws Exception{
        Usuario usuario = this.controladorUsuarios.buscarUsuarioPorId(dono);
        return this.controladorDeEmpresa.criarFarmacia(tipoEmpresa, usuario, nome, endereco, aberto24Horas, numeroFuncionarios);
    }

    /**
     * Altera os horários padrão de funcionamento de um determinado mercado.
     * @param mercado ID identificador do mercado alvo.
     * @param abre Novo horário de abertura.
     * @param fecha Novo horário de fechamento.
     * @throws Exception Se os formatos temporais forem inadequados ou se a empresa não for um mercado.
     */
    public void alterarFuncionamento(int mercado, String abre, String fecha) throws Exception {
        this.controladorDeEmpresa.alterarFuncionamento(mercado, abre, fecha);
    }

    /**
     * Retorna a lista indexada de estabelecimentos comerciais pertencentes a um proprietário.
     * @param idDono ID do usuário do tipo Proprietário.
     * @return String formatada contendo a listagem das empresas.
     * @throws Exception Se o usuário informado não possuir papéis administrativos corporativos.
     */
    public String getEmpresasDoUsuario(int idDono) throws Exception {
        Usuario u = this.controladorUsuarios.buscarUsuarioPorId(idDono);
        if (!(u instanceof DonoEmpresa)) {
            throw new UsuarioNaoCriaEmpresa();
        }
        return this.controladorDeEmpresa.getEmpresasDoUsuario(idDono);
    }

    /**
     * Obtém o ID único de uma empresa associada a um proprietário com base no seu nome e índice de aparição.
     * @param idDono ID do dono.
     * @param nome Nome da empresa.
     * @param indice Posição na lista de busca ordenada.
     * @return ID numérico correspondente da empresa filtrada.
     * @throws Exception Caso os critérios de filtragem ou índices falhem.
     */
    public int getIdEmpresa(int idDono, String nome, int indice) throws Exception {
        return this.controladorDeEmpresa.getIdEmpresa(idDono, nome, indice);
    }

    /**
     * Recupera o valor textual associado a uma propriedade da empresa informada.
     * @param idEmpresa ID único da empresa.
     * @param atributo Nome da propriedade (nome, endereco, tipoCozinha, dono, abre, fecha, tipoMercado, etc.).
     * @return Valor do atributo solicitado.
     * @throws Exception Se o atributo for inválido para o tipo específico de estabelecimento.
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
     * Cria um novo produto no cardápio/catálogo de um estabelecimento comercial.
     * @param empresa ID único da empresa proprietária do produto.
     * @param nome Nome descritivo do item.
     * @param valor Preço unitário mercantil.
     * @param category Classificação de nicho alimentar ou comercial.
     * @return ID gerado para o produto recém-cadastrado.
     * @throws Exception Se já houver um produto homônimo na mesma empresa ou se o valor for negativo.
     */
    public int criarProduto(int empresa, String nome, float valor, String category) throws Exception {
        return this.controladorDeProduto.criarProduto(empresa, nome, valor, category);
    }

    /**
     * Altera todas as propriedades cadastrais de um produto específico.
     * @param produto ID numérico do produto.
     * @param nome Novo nome.
     * @param valor Novo preço ajustado.
     * @param categoria Nova categoria.
     * @throws Exception Caso o produto não seja encontrado ou os novos valores sejam inconsistentes.
     */
    public void editarProduto(int produto, String nome, float valor, String categoria) throws Exception {
        this.controladorDeProduto.editarProduto(produto, nome, valor, categoria);
    }

    /**
     * Obtém uma informação detalhada de um produto com base no nome e ID da empresa detentora.
     * @param nome Nome exato do produto.
     * @param empresa ID único da empresa.
     * @param atributo Nome do campo desejado (valor, categoria, empresa).
     * @return Valor da propriedade pesquisada.
     * @throws Exception Se o produto não for localizado.
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
     * Gera uma listagem formatada de todos os produtos ativos de um estabelecimento.
     * @param empresa ID identificador da empresa.
     * @return Representação textual estruturada da lista de produtos.
     * @throws Exception Caso o ID da empresa fornecido seja inválido ou inexistente.
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
     * Helper privado interno para mapear um produto avaliando o nome do item e o nome corporativo da empresa de origem.
     * @param nomeProduto Nome do produto a ser pesquisado.
     * @param nomeEmpresa Nome corporativo da empresa a qual o produto pertence.
     * @return O objeto Produto correspondente.
     * @throws Exception Caso o produto não seja encontrado.
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
     * Inicia a abertura de um Pedido de entrega mercantil.
     * @param cliente ID numérico do usuário solicitante.
     * @param empresa ID numérico do estabelecimento comercial.
     * @return O número de rastreio único gerado para o pedido.
     * @throws Exception Caso o usuário seja um dono de empresa ou se já houver outro pedido em aberto para o mesmo local.
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
     * Recupera o número único do pedido baseado nos dados nominais do par envolvido e na ordem cronológica.
     * @param cliente ID único do cliente.
     * @param empresa ID único da empresa.
     * @param indice Posição sequencial do pedido na listagem histórica.
     * @return O identificador numérico interno correspondente.
     * @throws Exception Se os parâmetros de busca falharem no mapeamento.
     */
    public int getNumeroPedido(int cliente, int empresa, int indice) throws Exception {
        Usuario u = this.controladorUsuarios.buscarUsuarioPorId(cliente);
        Empresa e = this.controladorDeEmpresa.buscarEmpresaPorId(empresa);
        return this.controladorDePedidos.getNumeroPedido(u.getNome(), e.getNome(), indice);
    }

    /**
     * Insere uma unidade de um produto ao escopo interno do pedido aberto informado.
     * @param numero Número único do pedido.
     * @param produto ID único do produto a ser adicionado.
     * @throws Exception Caso o pedido esteja fechado ou se o produto pertencer a outra empresa diferente da selecionada no pedido.
     */
    public void adicionarProduto(int numero, int produto) throws Exception {
        Produto p = this.controladorDeProduto.buscarProdutoPorId(produto);
        Empresa e = this.controladorDeEmpresa.buscarEmpresaPorId(p.getEmpresa());
        this.controladorDePedidos.adicionarProduto(numero, e.getNome(), p.getNome(), p.getValor());
    }

    /**
     * Remove uma unidade de um determinado produto do escopo de um pedido ativo em aberto.
     * @param pedido Número do pedido.
     * @param produto Nome descritivo do produto a ser subtraído.
     * @throws Exception Se o pedido já estiver fechado ou se o item não compor a lista.
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
     * Altera o estado do pedido fechando-o para edições e movendo-o para a esteira de preparação logística.
     * @param numero Identificador numérico único do pedido.
     * @throws Exception Se o pedido não existir ou já estiver encerrado.
     */
    public void fecharPedido(int numero) throws Exception {
        this.controladorDePedidos.fecharPedido(numero);
    }

    /**
     * Altera o status do fluxo do pedido de preparando para pronto para despacho.
     * @param numero Identificador numérico do pedido.
     * @throws Exception Se o pedido não estiver em fase de preparação corporativa prévia.
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
     * Busca o pedido elegível mais antigo no estado de pronto que pertença à rede de atuação corporativa do entregador logado.
     * @param idEntregador ID numérico único do entregador.
     * @return O número identificador do pedido pronto localizado.
     * @throws Exception Se não houver pedidos disponíveis ou se o entregador não estiver vinculado a nenhuma empresa do MyFood.
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
     * Recupera as propriedades inerentes a um pedido processado pelo sistema.
     * @param numero Código de rastreio numérico do pedido.
     * @param atributo Campo solicitado (cliente, empresa, estado, produtos, valor).
     * @return String contendo os dados do atributo.
     * @throws Exception Caso o campo mapeado não pertença à estrutura mercantil.
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
     * Vincula contratualmente um entregador profissional à malha logística de uma empresa parceira.
     * @param idEmpresa ID corporativo da empresa.
     * @param idEntregador ID numérico do entregador.
     * @throws Exception Se o usuário informado não for habilitado legalmente como entregador no sistema.
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
     * Cria um manifesto operacional de Entrega, vinculando o entregador ao pedido e alterando seu status logístico.
     * @param pedido ID numérico do pedido pronto.
     * @param entregador ID numérico do entregador encarregado do despacho.
     * @param destino Endereço físico final detalhado de entrega.
     * @return O ID único do manifesto de entrega gerado pelo sistema.
     * @throws Exception Se o entregador estiver em trânsito ativo realizando outra entrega simultaneamente.
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
                    throw new EntregadroEmEntrega(); // Make sure this exception name is spelled correctly in your project!
                }
            }
        }

        String dest = (destino == null || destino.trim().isEmpty()) ? u.getEndereco() : destino;
        int idEntrega = this.controladorDeEntregas.criarEntrega(p, entregador, dest);
        p.setEstado("entregando");

        return idEntrega;
    }

    /**
     * Obtém uma informação mapeada de uma entrega ativa com base no seu manifesto logístico.
     * @param id Código único da entrega.
     * @param atributo Atributo pretendido (cliente, empresa, pedido, entregador, destino, produtos).
     * @return String traduzida do atributo da entrega.
     * @throws Exception Se os IDs ou propriedades forem inválidos.
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
     * Puxa o ID único de rastreio logístico da entrega à qual pertence o pedido informado.
     * @param pedido ID do pedido de entrega.
     * @return Código numérico identificador único da entrega mapeada.
     * @throws Exception Se não houver entregas ativas geradas para o pedido informado.
     */
    public int getIdEntrega(int pedido) throws Exception {
        return this.controladorDeEntregas.getIdEntrega(pedido);
    }

    /**
     * Finaliza o fluxo logístico concluindo a entrega com sucesso no endereço de destino, liberando o entregador.
     * @param idEntrega Identificador numérico do manifesto de entrega.
     * @throws Exception Se o código de entrega não corresponder a nenhuma operação em trânsito.
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
     * Lista todos os e-mails corporativos dos entregadores associados a uma determinada empresa do MyFood.
     * @param idEmpresa ID da empresa parceira.
     * @return String formatada da lista de e-mails dos funcionários da malha.
     * @throws Exception Caso o ID da empresa não seja localizado.
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
     * Lista todas as empresas e endereços físicos nas quais um entregador profissional possui vínculo operacional de frete.
     * @param idEntregador ID único do entregador profissional.
     * @return String contendo os dados estruturados das empresas parceiras.
     * @throws Exception Se o usuário informado não pertencer à classe de entregadores.
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