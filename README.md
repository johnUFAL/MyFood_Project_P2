**Projeto da disciplina de Programação 2 - Universidade Federal de Alagoas (UFAL)** **Desenvolvedor:** João Victor Duarte do Nascimento

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

O sistema foi construído em quatro fases incrementais:

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

---

## Tecnologias Utilizadas
- **Java SE**
- **EasyAccept** (Testes de aceitação)
- **java.beans.XMLEncoder / XMLDecoder** (Serialização de objetos)