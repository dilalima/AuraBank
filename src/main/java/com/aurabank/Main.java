package com.aurabank;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    private static final NumberFormat MOEDA = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));

    public static void main(String[] args) {
        if (args.length == 0 || !"--console".equals(args[0])) {
            AuraBankUI.abrir();
            return;
        }
        List<Conta> contas = new ArrayList<>();
        try (Scanner scanner = new Scanner(System.in)) {
            boolean executando = true;
            while (executando) {
                System.out.println("\n=== SEJA BEM-VINDO AO AURABANK ===");
                System.out.println("1 - Criar conta\n2 - Acessar conta\n3 - Sair");
                String opcao = ler(scanner, "Escolha uma opção: ");
                if (opcao == null) break;
                switch (opcao) {
                    case "1": cadastrar(scanner, contas); break;
                    case "2": acessar(scanner, contas); break;
                    case "3": executando = false; break;
                    default: System.out.println("Opção inválida!");
                }
            }
            System.out.println("Até logo!");
        }
    }

    private static String ler(Scanner scanner, String mensagem) {
        System.out.print(mensagem);
        return scanner.hasNextLine() ? scanner.nextLine().trim() : null;
    }

    private static String obrigatorio(Scanner scanner, String mensagem) {
        while (true) {
            String valor = ler(scanner, mensagem);
            if (valor == null || !valor.isEmpty()) return valor;
            System.out.println("Este campo é obrigatório.");
        }
    }

    private static String lerCpf(Scanner scanner) {
        while (true) {
            String entrada = obrigatorio(scanner, "Informe seu CPF: ");
            if (entrada == null) return null;
            String cpf = entrada.replace(".", "").replace("-", "");
            if (cpf.matches("[0-9]{11}")) return cpf;
            System.out.println("Informe 11 dígitos, com ou sem pontos e hífen.");
        }
    }

    private static void cadastrar(Scanner scanner, List<Conta> contas) {
        String nome = obrigatorio(scanner, "Digite seu nome: ");
        if (nome == null) return;
        String cpf = lerCpf(scanner);
        if (cpf == null) return;
        if (buscar(contas, cpf) != null) {
            System.out.println("Já existe uma conta cadastrada para este CPF.");
            return;
        }
        String telefone = obrigatorio(scanner, "Informe seu telefone: ");
        if (telefone == null) return;
        String email = obrigatorio(scanner, "Informe seu e-mail: ");
        if (email == null) return;
        String senha = obrigatorio(scanner, "Digite sua senha: ");
        if (senha == null) return;
        Cliente cliente = new Cliente(nome, cpf, email, senha, telefone);
        Conta conta = new Conta(String.format("%04d", contas.size() + 1), cliente);
        contas.add(conta);
        System.out.println("\n=== CONTA CRIADA ===");
        exibirConta(conta);
    }

    private static Conta buscar(List<Conta> contas, String cpf) {
        for (Conta conta : contas) {
            if (conta.getTitular().getCpf().equals(cpf)) return conta;
        }
        return null;
    }

    private static void acessar(Scanner scanner, List<Conta> contas) {
        String cpf = lerCpf(scanner);
        if (cpf == null) return;
        String senha = obrigatorio(scanner, "Digite sua senha: ");
        if (senha == null) return;
        Conta conta = buscar(contas, cpf);
        if (conta == null || !conta.getTitular().autenticar(senha)) {
            System.out.println("CPF ou senha incorretos.");
            return;
        }
        while (true) {
            System.out.println("\n=== Olá, " + conta.getTitular().getNome() + " ===");
            System.out.println("1 - Consultar saldo\n2 - Depositar\n3 - Sacar\n4 - Dados da conta\n5 - Sair da conta\n6 - Enviar solicitação de amizade\n7 - Responder solicitações\n8 - Meus amigos\n9 - Transferir para amigo");
            String opcao = ler(scanner, "Escolha uma opção: ");
            if (opcao == null || opcao.equals("5")) return;
            switch (opcao) {
                case "1": exibirSaldo(conta); break;
                case "2": movimentar(scanner, conta, true); break;
                case "3": movimentar(scanner, conta, false); break;
                case "4": exibirConta(conta); break;
                case "6": solicitarAmizade(scanner, contas, conta); break;
                case "7": responderSolicitacoes(scanner, conta); break;
                case "8": listarAmigos(conta); break;
                case "9": transferir(scanner, conta); break;
                default: System.out.println("Opção inválida!");
            }
        }
    }

    private static void solicitarAmizade(Scanner scanner, List<Conta> contas, Conta conta) {
        String numero = ler(scanner, "Número da conta do usuário: ");
        if (numero == null) return;
        Conta destino = null;
        for (Conta candidata : contas) {
            if (candidata.getNumeroConta().equals(numero)) destino = candidata;
        }
        if (destino == null) { System.out.println("com.aurabank.Conta não encontrada."); return; }
        System.out.println(conta.solicitarAmizade(destino)
                ? "Solicitação enviada com sucesso."
                : "Solicitação recusada: conta própria, amizade existente ou solicitação pendente.");
    }

    private static void responderSolicitacoes(Scanner scanner, Conta conta) {
        if (conta.getSolicitacoes().isEmpty()) System.out.println("Nenhuma solicitação pendente.");
        for (Conta remetente : conta.getSolicitacoes()) {
            String resposta = ler(scanner, remetente + " — 1: aceitar, 2: recusar, 3: deixar pendente: ");
            if (resposta == null) return;
            if ("1".equals(resposta) || "2".equals(resposta)) {
                conta.responderAmizade(remetente, "1".equals(resposta));
                System.out.println("1".equals(resposta) ? "Amizade aceita." : "Solicitação recusada.");
            } else if (!"3".equals(resposta)) System.out.println("Opção inválida; solicitação mantida.");
        }
    }

    private static void listarAmigos(Conta conta) {
        if (conta.getAmigos().isEmpty()) System.out.println("Você ainda não tem amigos.");
        for (Conta amigo : conta.getAmigos()) System.out.println(amigo);
    }

    private static void transferir(Scanner scanner, Conta conta) {
        listarAmigos(conta);
        if (conta.getAmigos().isEmpty()) return;
        String numero = ler(scanner, "Número da conta do amigo: ");
        if (numero == null) return;
        Conta destino = null;
        for (Conta amigo : conta.getAmigos()) {
            if (amigo.getNumeroConta().equals(numero)) destino = amigo;
        }
        if (destino == null) { System.out.println("Selecione a conta de um amigo."); return; }
        String entrada = ler(scanner, "Informe o valor (ex.: 25,50): ");
        if (entrada == null) return;
        if (!entrada.matches("[0-9]+([.,][0-9]{1,2})?")) {
            System.out.println("Valor inválido. Use até duas casas decimais."); return;
        }
        BigDecimal valor = new BigDecimal(entrada.replace(',', '.'));
        if (valor.signum() <= 0) { System.out.println("O valor deve ser maior que zero."); return; }
        System.out.println(conta.transferir(destino, valor)
                ? "Transferência realizada com sucesso para " + destino.getTitular().getNome() + "."
                : "Saldo insuficiente para esta transferência.");
        exibirSaldo(conta);
    }

    private static void movimentar(Scanner scanner, Conta conta, boolean deposito) {
        String entrada = ler(scanner, "Informe o valor (ex.: 25,50): ");
        if (entrada == null) return;
        if (!entrada.matches("[0-9]+([.,][0-9]{1,2})?")) {
            System.out.println("Valor inválido. Use até duas casas decimais e nenhum separador de milhar.");
            return;
        }
        BigDecimal valor = new BigDecimal(entrada.replace(',', '.'));
        if (valor.signum() <= 0) {
            System.out.println("O valor deve ser maior que zero.");
            return;
        }
        boolean sucesso = deposito ? conta.depositar(valor) : conta.sacar(valor);
        System.out.println(sucesso ? "Operação realizada com sucesso." : "Saldo insuficiente!");
        exibirSaldo(conta);
    }

    private static void exibirSaldo(Conta conta) {
        System.out.println("Saldo atual: " + MOEDA.format(conta.getSaldo()));
    }

    private static void exibirConta(Conta conta) {
        Cliente cliente = conta.getTitular();
        System.out.println("Titular: " + cliente.getNome());
        System.out.println("CPF: " + cliente.getCpf());
        System.out.println("Telefone: " + cliente.getTelefone());
        System.out.println("E-mail: " + cliente.getEmail());
        System.out.println("Número da conta: " + conta.getNumeroConta());
        exibirSaldo(conta);
    }
}
