# 🏦 AuraBank — Sistema Bancário Digital

### 💜 Seu dinheiro, seu próximo passo.

O **AuraBank** é um projeto de sistema bancário digital desenvolvido em **Java**, com interface gráfica desktop construída utilizando **Java Swing**.

O objetivo do projeto é simular o funcionamento de uma instituição financeira digital, aplicando conceitos de **Programação Orientada a Objetos (POO), Engenharia de Software, regras de negócio, persistência de dados e arquitetura de aplicações Java**.

O sistema possui funcionalidades como cadastro de clientes, autenticação, gerenciamento de contas, depósitos, saques, transferências e relacionamentos entre usuários.

Atualmente, o projeto está evoluindo para integrar completamente sua interface gráfica ao banco de dados **MySQL**, utilizando **Spring Boot, Spring Data JPA e Hibernate**.

---

## 🚀 Funcionalidades

### 👤 Cadastro e autenticação
- [x] Cadastro de clientes
- [x] Login utilizando CPF e senha
- [x] Validação básica de CPF e e-mail
- [x] Verificação de CPF duplicado
- [x] Acesso individual à conta
- [ ] Autenticação segura com hash de senhas

### 💰 Operações bancárias
- [x] Criação de contas bancárias
- [x] Consulta de saldo
- [x] Ocultar e mostrar saldo
- [x] Realização de depósitos
- [x] Realização de saques
- [x] Validação de saldo insuficiente
- [x] Transferências entre contas autorizadas
- [x] Histórico de movimentações durante a sessão
- [ ] Persistência de todas as transações no MySQL

### 🤝 Relacionamento entre usuários
- [x] Enviar solicitações de amizade
- [x] Aceitar solicitações
- [x] Recusar solicitações
- [x] Consultar lista de amigos
- [x] Transferir valores entre amigos

### 🖥️ Interface gráfica
- [x] Tela de boas-vindas
- [x] Tela de cadastro
- [x] Tela de login
- [x] Dashboard bancário
- [x] Painel de saldo
- [x] Formulários de depósito e saque
- [x] Área de transferências
- [x] Histórico de movimentações
- [x] Interface personalizada com Java Swing

---

## 🛠️ Tecnologias utilizadas

| Tecnologia | Aplicação |
|---|---|
| ☕ Java | Linguagem principal |
| 🖥️ Java Swing | Interface gráfica desktop |
| 🍃 Spring Boot | Estrutura da aplicação |
| 🗄️ MySQL | Banco de dados relacional |
| 🔗 Spring Data JPA | Camada de persistência |
| ⚙️ Hibernate | Mapeamento objeto-relacional |
| 📦 Maven | Gerenciamento de dependências |
| 🔧 Git | Controle de versões |
| 🐙 GitHub | Hospedagem do código-fonte |
| 💻 IntelliJ IDEA | Ambiente de desenvolvimento |

---

## 🏗️ Arquitetura do projeto

O AuraBank utiliza conceitos de Programação Orientada a Objetos e está sendo estruturado para separar as responsabilidades entre interface, entidades, regras de negócio e persistência.

**Principais componentes:**

- `Cliente.java` — Representação dos clientes.
- `Conta.java` — Representação das contas e operações bancárias.
- `ClienteRepository.java` — Persistência e consultas de clientes.
- `ContaRepository.java` — Persistência e consultas de contas.
- `AuraBankUI.java` — Interface gráfica construída com Swing.
- `AuraBankApplication.java` — Inicialização do Spring Boot.
- `Main.java` — Funcionalidades e execução pelo terminal.

### Estrutura de diretórios

```text
AuraBank/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── aurabank/
│       │           ├── AuraBankApplication.java
│       │           ├── AuraBankUI.java
│       │           ├── Cliente.java
│       │           ├── ClienteRepository.java
│       │           ├── Conta.java
│       │           ├── ContaRepository.java
│       │           └── Main.java
│       └── resources/
│           └── application.properties
├── pom.xml
├── .gitignore
└── README.md
```

---

## 🗄️ Banco de dados

O AuraBank utiliza **MySQL 8** e **Spring Data JPA** para implementar a camada de persistência.

O Hibernate já está configurado para realizar o mapeamento das entidades e criar as estruturas necessárias no banco de dados.

### Tabelas existentes

| Tabela | Descrição |
|---|---|
| `clientes` | Dados cadastrais dos clientes |
| `contas` | Informações das contas e saldos |
| `conta_amigos` | Relacionamentos entre contas |
| `solicitacoes_amizade` | Solicitações de amizade |

**Status:** a conexão com o MySQL e a criação das tabelas foram implementadas. A integração das operações da interface Swing com os repositórios JPA ainda está em desenvolvimento.

---

## ⚙️ Como executar o projeto

### Pré-requisitos

- Java JDK 17 ou superior compatível
- Maven
- MySQL Server
- IntelliJ IDEA ou outra IDE Java

### 1. Clonar o repositório

```bash
git clone https://github.com/dilalima/AuraBank.git
```

### 2. Acessar a pasta

```bash
cd AuraBank
```

### 3. Criar o banco de dados

No MySQL Workbench, execute:

```sql
CREATE DATABASE IF NOT EXISTS aurabank;
```

### 4. Configurar a conexão

No arquivo `src/main/resources/application.properties`, configure a conexão com o MySQL:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/aurabank
spring.datasource.username=root
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
```

Defina a variável de ambiente `DB_PASSWORD` com a senha do seu MySQL.

**Importante:** não publique senhas ou outras credenciais no GitHub.

### 5. Executar a aplicação

Abra o projeto no IntelliJ IDEA, aguarde a importação das dependências Maven e execute `AuraBankApplication.java` para inicializar o Spring Boot.

A interface desktop está implementada em `AuraBankUI.java`, por meio do método `abrir()`, e precisa ser iniciada por um ponto de entrada que invoque esse método.

---

## 📚 Conceitos aplicados

Durante o desenvolvimento do AuraBank, foram utilizados:

- Programação Orientada a Objetos (POO)
- Classes e objetos
- Encapsulamento
- Construtores e métodos
- Coleções Java
- Estruturas condicionais e de repetição
- Manipulação de valores monetários com `BigDecimal`
- Interfaces gráficas com Swing
- Tratamento e validação de entradas
- Mapeamento objeto-relacional (ORM)
- Relacionamentos entre entidades
- Consultas com Spring Data JPA
- Versionamento com Git

---

## 📈 Próximas melhorias

- [ ] Integrar o cadastro da interface Swing ao MySQL
- [ ] Implementar autenticação com BCrypt ou Argon2
- [ ] Persistir depósitos, saques e transferências
- [ ] Criar uma camada de serviços para as regras bancárias
- [ ] Implementar histórico completo de transações
- [ ] Melhorar a validação de CPF
- [ ] Implementar tratamento centralizado de erros
- [ ] Desenvolver testes automatizados
- [ ] Criar uma API REST
- [ ] Melhorar a arquitetura e a organização dos pacotes
- [ ] Adicionar capturas de tela da interface gráfica

---

## 🔐 Segurança

O AuraBank é um projeto educacional e utiliza valores fictícios.

Ainda não deve ser utilizado para movimentação de dinheiro real ou armazenamento de credenciais reais de usuários.

O desenvolvimento futuro incluirá melhorias de autenticação, proteção de dados, validações e integridade das transações.

---

## 👨‍💻 Desenvolvedor

**Dileã Lima**

Estudante de Tecnologia da Informação, com foco em Desenvolvimento de Software, Java, Python, Banco de Dados e Inteligência Artificial.

Desenvolvendo projetos práticos para aprofundar conhecimentos em programação, arquitetura de sistemas e soluções tecnológicas.

**GitHub:** https://github.com/dilalima

**Repositório:** https://github.com/dilalima/AuraBank

---

⭐ **AuraBank — Projeto em desenvolvimento contínuo, criado para aprendizado e evolução profissional em Engenharia de Software.**
