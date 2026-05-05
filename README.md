# MyFood

**Projeto da disciplina de Programação 2 - Universidade Federal de Alagoas (UFAL)**  
**Desenvolvedor:** João Victor Duarte do Nascimento

---

## Sobre o Projeto
O **MyFood** é um sistema backend desenvolvido em **Java** aplicando os conceitos de Programação Orientada a Objetos (POO) e Test-Driven Development (TDD) simulado através da biblioteca **EasyAccept**.

O projeto simula a lógica de um aplicativo de delivery de comida, contemplando a criação de usuários, gestão de restaurantes, cadastro de produtos e o fluxo completo de pedidos.

### Persistência de Dados e Arquitetura
- **Banco de Dados em Memória:** O sistema não utiliza um banco relacional tradicional. Durante a execução, os dados são armazenados na memória RAM utilizando Coleções do Java (como `Map` e `LinkedHashMap`).
- **Persistência em XML:** Ao encerrar o programa, os dados são convertidos e salvos em um arquivo XML usando `java.beans.XMLEncoder`. Na inicialização, os dados são recarregados com `java.beans.XMLDecoder`.
- **Design Pattern:** A comunicação com a biblioteca de testes (EasyAccept) é intermediada exclusivamente pelo padrão **Facade** (`Facade.java`), garantindo que a fachada apenas orquestre as chamadas sem concentrar as regras de negócio.

---

## Estrutura de Diretórios

O projeto está organizado visando a separação de responsabilidades:

- `/` (Raiz): Contém as portas de entrada da aplicação (`Main.java` e `Facade.java`).
- `/models`: Entidades e dados puros do sistema (ex: `Usuario`, `Restaurante`, `Produto`, `Pedido`).
- `/controllers`: Inteligência e regras de negócio de cada domínio.
- `/exceptions`: Classes para o tratamento de exceções personalizadas (divididas por domínio).
- `/tests`: Arquivos de teste `.txt` executados pelo EasyAccept.
- `/docs`: Documentação técnica do projeto.

---

## Fases de Desenvolvimento

O sistema foi construído de forma incremental ao longo de oito fases:

### Fase 1 - Usuários
- Implementação de um `ControladorDeUsuarios` para geração de IDs únicos e validações (CPF, E-mail).
- Modelagem utilizando classe abstrata `Usuario` e suas especializações (`Cliente` e `DonoEmpresa`).
- Implementação dos fluxos de login e recuperação de atributos.

### Fase 2 - Empresas
- Modelagem de estabelecimentos através da classe abstrata `Empresa` e da herança `Restaurante`.
- `ControladorDeEmpresa` gerencia criação e relacionamento de empresas pertencentes aos usuários do tipo `DonoEmpresa`.
- Tratamento de exceções específicas, como tentativa de cadastro duplicado (mesmo nome e local).

### Fase 3 - Produtos
- Criação da classe concreta `Produto` atrelada a uma empresa específica.
- `ControladorDeProduto` gerencia a listagem, edição e criação de itens no cardápio de cada restaurante.
- A `Facade` faz a integração entre os IDs da Empresa e do Produto para exibir os dados corretamente.

### Fase 4 - Pedidos
- Implementação da entidade `Pedido` conectando Cliente, Empresa e Produtos.
- Fluxo completo gerenciado pelo `ControladorDePedidos`: criar pedido (estado "aberto"), adicionar produtos, remover produtos e fechar pedido (estado "preparando").
- Cálculos dinâmicos de valor total e validação estrita de estados (ex: não é possível editar pedidos já fechados).

### Fase 5 - Mercados
- Criação da classe `Mercado`, herdando de `Empresa`, com atributos específicos de horário de funcionamento.
- Reestruturação das exceções para reaproveitamento de regras de negócio entre Restaurantes e Mercados.
- Expansão do `ControladorDeEmpresa` e da `Facade` para lidar com múltiplas requisições e lógicas de funcionamento específicas de mercados.

### Fase 6 - Farmácias
- Introdução da classe `Farmacia`, também herdando de `Empresa`, aproveitando o polimorfismo estabelecido nas fases anteriores.
- Inclusão do método de criação de farmácia na `Facade`, reutilizando toda a estrutura de métodos e exceções já consolidadas.

### Fase 7 - Entregadores
- Modelagem da classe `Entregador` (herdeira de `Usuario`), utilizando coleções (`List` e `ArrayList`) para armazenar as empresas vinculadas ao profissional.
- Atualização do `ControladorDeUsuarios` com novos métodos para criação de entregadores e recuperação de seus atributos específicos.
- Modificações na `Facade` para permitir o cadastro e o vínculo entre Entregadores e Empresas.

### Fase 8 - Sistema de Entregas
- Fase mais complexa do projeto, introduzindo a classe abstrata `Entrega` para orquestrar os dados do pedido, entregador, cliente e empresa.
- Implementação do `ControladorDeEntregas` com métodos focados no ciclo de vida da entrega (`zerar`, `criarEntrega`, `buscarEntregaPorId`, etc.).
- A `Facade` consolida a lógica final, cruzando dados de todos os controladores para liberar pedidos, calcular prioridades (pedidos de Farmácia têm precedência) e concluir entregas.

---

## Tecnologias Utilizadas
- **Java SE**
- **EasyAccept** (Testes de aceitação)
- **java.beans.XMLEncoder / XMLDecoder** (Serialização de objetos)