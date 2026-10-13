# 🏦 AuraBank

### Sistema Bancário desenvolvido em Java

O **AuraBank** é um projeto de sistema bancário desenvolvido com Java, com foco na aplicação prática de Programação Orientada a Objetos (POO), regras de negócio, persistência de dados e desenvolvimento de software.

O projeto simula operações bancárias e está em evolução para integrar suas funcionalidades ao banco de dados MySQL utilizando Spring Boot e Spring Data JPA.

## 🚀 Funcionalidades

- Cadastro de clientes com nome, CPF, telefone, e-mail e senha
- Acesso à conta utilizando CPF e senha
- Consulta de saldo
- Depósitos e saques com validação de valores
- Consulta de informações da conta
- Solicitações de amizade entre clientes
- Aceitação de solicitações e gerenciamento de amigos
- Transferências entre contas
- Integração com MySQL em desenvolvimento

## 🛠️ Tecnologias utilizadas

- **Java** — Linguagem principal
- **Spring Boot** — Framework para desenvolvimento da aplicação
- **Spring Data JPA** — Persistência de entidades
- **Hibernate** — Mapeamento objeto-relacional
- **MySQL** — Banco de dados relacional
- **Maven** — Gerenciamento de dependências
- **Git e GitHub** — Versionamento do projeto
- **IntelliJ IDEA** — Ambiente de desenvolvimento

## 📂 Estrutura do projeto

```text
src/
└── main/
    ├── java/
    │   └── com/aurabank/
    │       ├── AuraBankApplication.java
    │       ├── AuraBankUI.java
    │       ├── Cliente.java
    │       ├── ClienteRepository.java
    │       ├── Conta.java
    │       ├── ContaRepository.java
    │       └── Main.java
    └── resources/
        └── application.properties

pom.xml
README.md
```

## 🗄️ Banco de dados

O projeto utiliza MySQL e possui entidades relacionadas a clientes, contas e relacionamentos entre usuários.

O Spring Data JPA é utilizado para implementar a camada de persistência.

## ⚙️ Configuração

**Pré-requisitos:**
- JDK compatível com o projeto
- Maven
- MySQL Server
- IntelliJ IDEA ou outra IDE Java

Crie o banco de dados:

```sql
CREATE DATABASE IF NOT EXISTS aurabank;
```

Configure a conexão em `application.properties`, utilizando variáveis de ambiente para proteger informações sensíveis.

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/aurabank
spring.datasource.username=root
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=update
```

Configure `DB_PASSWORD` com a senha do seu MySQL antes de iniciar a aplicação.

## 📈 Próximas melhorias

- [ ] Persistir cadastros e operações bancárias no MySQL
- [ ] Implementar autenticação segura com hash de senhas
- [ ] Criar interface gráfica para o sistema
- [ ] Desenvolver APIs REST
- [ ] Implementar histórico de transações
- [ ] Adicionar testes automatizados

## 👨‍💻 Desenvolvedor

**Dileã Lima**

Estudante de Tecnologia da Informação, com foco em desenvolvimento de software, Java, Python e bancos de dados.

GitHub: [github.com/dilalima](https://github.com/dilalima)

---

⭐ Projeto desenvolvido para fins educacionais e evolução contínua dos conhecimentos em desenvolvimento de software.
