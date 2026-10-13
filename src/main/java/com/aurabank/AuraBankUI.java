package com.aurabank;

import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/** Interface desktop do AuraBank. Todas as alterações acontecem na EDT. */
public class AuraBankUI extends JPanel {
    private static final Color BG = new Color(245, 246, 251);
    private static final Color NAVY = new Color(23, 25, 46);
    private static final Color INK = new Color(34, 37, 60);
    private static final Color MUTED = new Color(112, 117, 139);
    private static final Color PURPLE = new Color(112, 75, 235);
    private static final Color GREEN = new Color(25, 135, 99);
    private static final NumberFormat MOEDA = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));
    private final List<Conta> contas = new ArrayList<>();
    private final List<Movimento> movimentos = new ArrayList<>();
    private Conta atual;
    private boolean saldoVisivel = true;
    private JLabel mensagem;

    public AuraBankUI() {
        super(new BorderLayout());
        setBackground(BG);
        mostrarAcesso(false);
    }

    public static void abrir() {
        SwingUtilities.invokeLater(() -> {
            UIManager.put("Label.font", fonte(14, false));
            UIManager.put("Button.font", fonte(14, true));
            JFrame janela = new JFrame("AuraBank | Seu dinheiro, sua liberdade");
            janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            janela.setContentPane(new AuraBankUI());
            janela.setMinimumSize(new Dimension(940, 720));
            janela.setSize(1120, 800);
            janela.setLocationRelativeTo(null);
            janela.setVisible(true);
        });
    }

    private void mostrarAcesso(boolean cadastro) {
        removeAll();
        JPanel marca = new Marca();
        marca.setPreferredSize(new Dimension(350, 700));
        marca.setBorder(new EmptyBorder(48, 36, 48, 36));
        marca.setLayout(new BoxLayout(marca, BoxLayout.Y_AXIS));
        marca.add(texto("Aura", 42, true, Color.WHITE));
        marca.add(texto("B a n k", 13, true, new Color(190, 174, 255)));
        marca.add(Box.createVerticalGlue());
        marca.add(texto("Seu dinheiro.", 30, true, Color.WHITE));
        marca.add(texto("Seu próximo passo.", 27, true, Color.WHITE));
        marca.add(Box.createVerticalStrut(20));
        marca.add(texto("Uma experiência simples", 16, false, new Color(208, 203, 229)));
        marca.add(texto("para cuidar do seu saldo.", 16, false, new Color(208, 203, 229)));
        marca.add(Box.createVerticalGlue());
        marca.add(texto("FEITO PARA O SEU DIA A DIA", 11, true, new Color(190, 174, 255)));
        add(marca, BorderLayout.WEST);

        JPanel centro = new JPanel(new GridBagLayout());
        centro.setBackground(BG);
        JPanel form = coluna();
        form.setPreferredSize(new Dimension(420, cadastro ? 655 : 450));
        form.add(texto(cadastro ? "COMECE POR AQUI" : "BEM-VINDO DE VOLTA", 11, true, PURPLE));
        form.add(Box.createVerticalStrut(10));
        form.add(texto(cadastro ? "Abra sua conta" : "Entre na sua conta", 30, true, INK));
        form.add(Box.createVerticalStrut(8));
        form.add(texto(cadastro ? "Preencha seus dados para começar." : "Tudo o que você precisa, em um só lugar.", 14, false, MUTED));
        form.add(Box.createVerticalStrut(24));
        JTextField nome = new JTextField();
        JTextField cpf = new JTextField();
        JTextField telefone = new JTextField();
        JTextField email = new JTextField();
        JPasswordField senha = new JPasswordField();
        if (cadastro) campo(form, "Nome completo", nome, "nome");
        campo(form, "CPF", cpf, "cpf");
        if (cadastro) {
            campo(form, "Telefone", telefone, "telefone");
            campo(form, "E-mail", email, "email");
        }
        campo(form, "Senha", senha, "senha");
        mensagem = texto(" ", 12, false, MUTED);
        form.add(mensagem);
        form.add(Box.createVerticalStrut(12));
        JButton enviar = botao(cadastro ? "Criar minha conta  →" : "Entrar  →", true);
        enviar.setName("enviar");
        enviar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        Runnable acao = () -> {
            String documento = cpf.getText().trim().replace(".", "").replace("-", "");
            String segredo = new String(senha.getPassword());
            if (!documento.matches("[0-9]{11}")) { aviso("Informe um CPF com 11 dígitos.", false); return; }
            if (segredo.trim().isEmpty()) { aviso("Preencha sua senha.", false); return; }
            Conta encontrada = buscar(documento);
            if (cadastro) {
                if (nome.getText().trim().isEmpty() || telefone.getText().trim().isEmpty() || email.getText().trim().isEmpty()) {
                    aviso("Preencha todos os campos para continuar.", false); return;
                }
                if (!email.getText().trim().matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+")) {
                    aviso("Informe um e-mail válido.", false); return;
                }
                if (encontrada != null) { aviso("Este CPF já possui uma conta.", false); return; }
                Cliente cliente = new Cliente(nome.getText().trim(), documento, email.getText().trim(), segredo, telefone.getText().trim());
                atual = new Conta(String.format("%04d", contas.size() + 1), cliente);
                contas.add(atual);
            } else {
                if (encontrada == null || !encontrada.getTitular().autenticar(segredo)) {
                    aviso("CPF ou senha incorretos.", false); return;
                }
                atual = encontrada;
            }
            senha.setText("");
            saldoVisivel = true;
            mostrarPainel();
        };
        enviar.addActionListener(e -> acao.run());
        senha.addActionListener(e -> acao.run());
        form.add(enviar);
        form.add(Box.createVerticalStrut(12));
        JButton alternar = botao(cadastro ? "Já tenho conta. Entrar" : "Ainda não tenho conta. Criar conta", false);
        alternar.setName("alternar");
        alternar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        alternar.addActionListener(e -> mostrarAcesso(!cadastro));
        form.add(alternar);
        centro.add(form);
        add(centro, BorderLayout.CENTER);
        atualizar();
        SwingUtilities.invokeLater(() -> (cadastro ? nome : cpf).requestFocusInWindow());
    }

    private void mostrarPainel() {
        removeAll();
        JPanel menu = coluna();
        menu.setOpaque(true);
        menu.setBackground(NAVY);
        menu.setBorder(new EmptyBorder(34, 22, 28, 22));
        menu.setPreferredSize(new Dimension(220, 700));
        menu.add(texto("aura", 36, true, Color.WHITE));
        menu.add(texto("B A N K", 11, true, new Color(176, 164, 222)));
        menu.add(Box.createVerticalStrut(54));
        JLabel pagina = texto("   Visão geral", 15, true, Color.WHITE);
        pagina.setOpaque(true);
        pagina.setBackground(new Color(54, 43, 88));
        pagina.setBorder(new EmptyBorder(14, 6, 14, 6));
        pagina.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        menu.add(pagina);
        menu.add(Box.createVerticalGlue());
        menu.add(texto("CONTA " + atual.getNumeroConta(), 11, true, new Color(176, 164, 222)));
        menu.add(Box.createVerticalStrut(12));
        JButton sair = botao("Sair da conta", false);
        sair.setName("sair");
        sair.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        sair.addActionListener(e -> { atual = null; mostrarAcesso(false); });
        menu.add(sair);
        add(menu, BorderLayout.WEST);

        JPanel conteudo = coluna();
        conteudo.setOpaque(true);
        conteudo.setBackground(BG);
        conteudo.setBorder(new EmptyBorder(30, 32, 24, 32));
        JPanel cabecalho = linha();
        JPanel titulo = coluna();
        titulo.add(texto("SEU ESPAÇO FINANCEIRO", 11, true, PURPLE));
        titulo.add(Box.createVerticalStrut(8));
        titulo.add(texto("Olá, " + atual.getTitular().getNome().split("\\s+")[0] + "!", 29, true, INK));
        titulo.add(Box.createVerticalStrut(6));
        titulo.add(texto("Que bom ter você por aqui.", 14, false, MUTED));
        cabecalho.add(titulo, BorderLayout.CENTER);
        JLabel avatar = texto(atual.getTitular().getNome().substring(0, 1).toUpperCase(Locale.ROOT), 22, true, PURPLE);
        avatar.setOpaque(true);
        avatar.setBackground(new Color(231, 225, 252));
        avatar.setBorder(new EmptyBorder(12, 20, 12, 20));
        cabecalho.add(avatar, BorderLayout.EAST);
        cabecalho.setMaximumSize(new Dimension(Integer.MAX_VALUE, 92));
        conteudo.add(cabecalho);
        conteudo.add(Box.createVerticalStrut(24));

        Card saldo = new Card(PURPLE);
        saldo.setLayout(new BorderLayout(16, 16));
        saldo.setBorder(new EmptyBorder(26, 28, 26, 28));
        JPanel saldoTexto = coluna();
        saldoTexto.add(texto("Saldo disponível", 14, false, new Color(228, 220, 255)));
        saldoTexto.add(Box.createVerticalStrut(12));
        saldoTexto.add(texto(saldoVisivel ? MOEDA.format(atual.getSaldo()) : "R$ ••••••", 38, true, Color.WHITE));
        saldoTexto.add(Box.createVerticalStrut(10));
        saldoTexto.add(texto("com.aurabank.Conta " + atual.getNumeroConta() + "   •   AuraBank", 12, false, new Color(228, 220, 255)));
        saldo.add(saldoTexto, BorderLayout.CENTER);
        JButton ocultar = botao(saldoVisivel ? "Ocultar saldo" : "Mostrar saldo", false);
        ocultar.setName("ocultar");
        ocultar.addActionListener(e -> { saldoVisivel = !saldoVisivel; mostrarPainel(); });
        JPanel canto = coluna();
        canto.add(ocultar);
        saldo.add(canto, BorderLayout.EAST);
        saldo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 185));
        conteudo.add(saldo);
        conteudo.add(Box.createVerticalStrut(20));

        JPanel operacoes = new JPanel(new GridLayout(1, 2, 18, 0));
        operacoes.setAlignmentX(Component.LEFT_ALIGNMENT);
        operacoes.setOpaque(false);
        operacoes.add(operacao(true));
        operacoes.add(operacao(false));
        operacoes.setMaximumSize(new Dimension(Integer.MAX_VALUE, 210));
        conteudo.add(operacoes);
        mensagem = texto(" ", 12, false, MUTED);
        mensagem.setBorder(new EmptyBorder(10, 0, 10, 0));
        conteudo.add(mensagem);
        conteudo.add(amizades());
        conteudo.add(Box.createVerticalStrut(18));
        conteudo.add(transferencia());
        conteudo.add(Box.createVerticalStrut(18));

        JPanel rodape = new JPanel(new GridLayout(1, 2, 18, 0));
        rodape.setAlignmentX(Component.LEFT_ALIGNMENT);
        rodape.setOpaque(false);
        Card historico = cartao();
        historico.add(texto("Últimas movimentações", 16, true, INK));
        historico.add(Box.createVerticalStrut(14));
        int quantidade = 0;
        for (int i = movimentos.size() - 1; i >= 0 && quantidade < 4; i--) {
            Movimento movimento = movimentos.get(i);
            if (movimento.conta != atual) continue;
            JPanel item = linha();
            JPanel descricao = coluna();
            descricao.add(texto(movimento.descricao, 13, true, INK));
            descricao.add(texto(movimento.data, 11, false, MUTED));
            item.add(descricao, BorderLayout.CENTER);
            item.add(texto(saldoVisivel ? (movimento.deposito ? "+ " : "− ") + MOEDA.format(movimento.valor) : "••••", 13, true, movimento.deposito ? GREEN : INK), BorderLayout.EAST);
            historico.add(item);
            historico.add(Box.createVerticalStrut(10));
            quantidade++;
        }
        if (quantidade == 0) {
            historico.add(texto("Seu próximo passo começa aqui.", 13, false, MUTED));
            historico.add(Box.createVerticalStrut(8));
            historico.add(texto("Faça um depósito para começar.", 12, false, MUTED));
        }
        rodape.add(historico);
        Card dados = cartao();
        dados.add(texto("Minha conta", 16, true, INK));
        dados.add(Box.createVerticalStrut(14));
        Cliente cliente = atual.getTitular();
        dados.add(texto(cliente.getNome(), 14, true, INK));
        dados.add(Box.createVerticalStrut(8));
        dados.add(texto("CPF  " + cliente.getCpf(), 12, false, MUTED));
        dados.add(texto(cliente.getEmail(), 12, false, MUTED));
        dados.add(texto(cliente.getTelefone(), 12, false, MUTED));
        rodape.add(dados);
        rodape.setMaximumSize(new Dimension(Integer.MAX_VALUE, 225));
        conteudo.add(rodape);
        conteudo.add(Box.createVerticalStrut(18));
        conteudo.add(texto("AuraBank • Um novo jeito de cuidar do seu dinheiro", 11, false, MUTED));
        JScrollPane scroll = new JScrollPane(conteudo);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scroll, BorderLayout.CENTER);
        atualizar();
    }

    private Card operacao(boolean deposito) {
        Card card = cartao();
        card.add(texto(deposito ? "↙  Depositar" : "↗  Sacar", 18, true, INK));
        card.add(Box.createVerticalStrut(8));
        card.add(texto(deposito ? "Adicione um valor à sua conta." : "Retire um valor do seu saldo.", 12, false, MUTED));
        card.add(Box.createVerticalStrut(14));
        JTextField valor = new JTextField();
        valor.setName(deposito ? "valorDeposito" : "valorSaque");
        estilizarCampo(valor);
        valor.getAccessibleContext().setAccessibleName(deposito ? "Valor do depósito" : "Valor do saque");
        valor.setToolTipText("Ex.: 25,50. Sem separador de milhar.");
        card.add(valor);
        card.add(Box.createVerticalStrut(12));
        JButton confirmar = botao(deposito ? "Confirmar depósito" : "Confirmar saque", deposito);
        confirmar.setName(deposito ? "depositar" : "sacar");
        confirmar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        Runnable acao = () -> {
            String entrada = valor.getText().trim();
            if (!entrada.matches("[0-9]+([.,][0-9]{1,2})?")) {
                aviso("Use um valor como 25,50, com até duas casas decimais.", false); return;
            }
            BigDecimal quantia = new BigDecimal(entrada.replace(',', '.'));
            if (quantia.signum() <= 0) { aviso("O valor deve ser maior que zero.", false); return; }
            if (!(deposito ? atual.depositar(quantia) : atual.sacar(quantia))) {
                aviso("Saldo insuficiente para este saque.", false); return;
            }
            movimentos.add(new Movimento(atual, quantia, deposito));
            mostrarPainel();
            aviso(deposito ? "Depósito realizado com sucesso." : "Saque realizado com sucesso.", true);
        };
        confirmar.addActionListener(e -> acao.run());
        valor.addActionListener(e -> acao.run());
        card.add(confirmar);
        return card;
    }

    private Card amizades() {
        Card card = cartao();
        card.add(texto("Amigos", 18, true, INK));
        card.add(Box.createVerticalStrut(10));
        JTextField destino = new JTextField();
        campo(card, "Número da conta do usuário", destino, "contaAmigo");
        JButton solicitar = botao("Enviar solicitação de amizade", true);
        solicitar.setName("solicitarAmizade");
        Runnable enviar = () -> {
            Conta pessoa = buscarNumero(destino.getText().trim());
            if (pessoa == null) { aviso("com.aurabank.Conta não encontrada.", false); return; }
            if (!atual.solicitarAmizade(pessoa)) {
                aviso("Solicitação não enviada: conta própria, amizade existente ou solicitação pendente.", false);
                return;
            }
            mostrarPainel();
            aviso("Solicitação enviada para " + pessoa.getTitular().getNome() + ".", true);
        };
        solicitar.addActionListener(e -> enviar.run());
        destino.addActionListener(e -> enviar.run());
        card.add(solicitar);
        card.add(Box.createVerticalStrut(14));
        card.add(texto("Solicitações recebidas", 14, true, INK));
        if (atual.getSolicitacoes().isEmpty()) card.add(texto("Nenhuma solicitação pendente.", 12, false, MUTED));
        for (Conta pessoa : atual.getSolicitacoes()) {
            card.add(texto(pessoa.toString(), 13, false, INK));
            JPanel acoes = linha();
            JButton aceitar = botao("Aceitar", true);
            aceitar.setName("aceitarAmizade" + pessoa.getNumeroConta());
            JButton recusar = botao("Recusar", false);
            recusar.setName("recusarAmizade" + pessoa.getNumeroConta());
            aceitar.addActionListener(e -> {
                atual.responderAmizade(pessoa, true);
                mostrarPainel();
                aviso("Agora você e " + pessoa.getTitular().getNome() + " são amigos.", true);
            });
            recusar.addActionListener(e -> {
                atual.responderAmizade(pessoa, false);
                mostrarPainel();
                aviso("Solicitação recusada.", true);
            });
            acoes.add(aceitar, BorderLayout.WEST);
            acoes.add(recusar, BorderLayout.EAST);
            card.add(acoes);
        }
        card.add(Box.createVerticalStrut(14));
        card.add(texto("Meus amigos", 14, true, INK));
        if (atual.getAmigos().isEmpty()) card.add(texto("Aceite uma solicitação para fazer transferências.", 12, false, MUTED));
        for (Conta amigo : atual.getAmigos()) card.add(texto(amigo.toString(), 13, false, INK));
        return card;
    }

    private Card transferencia() {
        Card card = cartao();
        card.add(texto("Transferir para um amigo", 18, true, INK));
        card.add(Box.createVerticalStrut(12));
        JComboBox<Conta> amigos = new JComboBox<>(atual.getAmigos().toArray(new Conta[0]));
        amigos.setName("destinatarioTransferencia");
        amigos.getAccessibleContext().setAccessibleName("Amigo destinatário");
        amigos.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        card.add(amigos);
        JTextField valor = new JTextField();
        campo(card, "Valor da transferência (R$)", valor, "valorTransferencia");
        JButton confirmar = botao("Confirmar transferência", true);
        confirmar.setName("transferir");
        confirmar.setEnabled(amigos.getItemCount() > 0);
        Runnable transferir = () -> {
            Conta destino = (Conta) amigos.getSelectedItem();
            String entrada = valor.getText().trim();
            if (!entrada.matches("[0-9]+([.,][0-9]{1,2})?")) {
                aviso("Use um valor como 25,50, com até duas casas decimais.", false); return;
            }
            BigDecimal quantia = new BigDecimal(entrada.replace(',', '.'));
            if (quantia.signum() <= 0) { aviso("O valor deve ser maior que zero.", false); return; }
            if (!atual.transferir(destino, quantia)) {
                aviso("Transferência recusada: saldo insuficiente ou destinatário não é amigo.", false); return;
            }
            movimentos.add(new Movimento(atual, quantia, false, "Transferência para " + destino.getTitular().getNome()));
            movimentos.add(new Movimento(destino, quantia, true, "Transferência de " + atual.getTitular().getNome()));
            mostrarPainel();
            aviso("Transferência realizada para " + destino.getTitular().getNome() + ".", true);
        };
        confirmar.addActionListener(e -> transferir.run());
        valor.addActionListener(e -> transferir.run());
        card.add(confirmar);
        return card;
    }

    private Conta buscarNumero(String numero) {
        for (Conta conta : contas) if (conta.getNumeroConta().equals(numero)) return conta;
        return null;
    }

    private Conta buscar(String cpf) {
        for (Conta conta : contas) if (conta.getTitular().getCpf().equals(cpf)) return conta;
        return null;
    }

    private void aviso(String texto, boolean sucesso) {
        mensagem.setText(texto);
        mensagem.setForeground(sucesso ? GREEN : new Color(184, 50, 71));
    }

    private void atualizar() { revalidate(); repaint(); }
    private static Font fonte(int tamanho, boolean forte) { return new Font("SansSerif", forte ? Font.BOLD : Font.PLAIN, tamanho); }
    private static JLabel texto(String valor, int tamanho, boolean forte, Color cor) {
        JLabel label = new JLabel(valor);
        label.setFont(fonte(tamanho, forte));
        label.setForeground(cor);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
    private static JPanel coluna() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }
    private static JPanel linha() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }
    private static Card cartao() {
        Card card = new Card(Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(20, 22, 20, 22));
        return card;
    }
    private static void campo(JPanel form, String nome, JTextField campo, String id) {
        form.add(texto(nome, 12, true, INK));
        form.add(Box.createVerticalStrut(6));
        campo.setName(id);
        campo.getAccessibleContext().setAccessibleName(nome);
        estilizarCampo(campo);
        form.add(campo);
        form.add(Box.createVerticalStrut(12));
    }
    private static void estilizarCampo(JTextField campo) {
        campo.setFont(fonte(14, false));
        campo.setForeground(INK);
        campo.setCaretColor(PURPLE);
        campo.setBackground(Color.WHITE);
        campo.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(218, 221, 234)), new EmptyBorder(9, 12, 9, 12)));
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        campo.setPreferredSize(new Dimension(200, 42));
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
    }
    private static JButton botao(String titulo, boolean primario) {
        JButton button = new JButton(titulo) {
            @Override protected void paintComponent(Graphics graphics) {
                Graphics2D g = (Graphics2D) graphics.create();
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setColor(getModel().isRollover() ? (primario ? new Color(93, 58, 211) : new Color(229, 223, 248)) : getBackground());
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                if (isFocusOwner()) {
                    g.setColor(primario ? Color.WHITE : PURPLE);
                    g.drawRoundRect(3, 3, getWidth() - 7, getHeight() - 7, 10, 10);
                }
                g.dispose();
                super.paintComponent(graphics);
            }
        };
        button.setFont(fonte(13, true));
        button.setBackground(primario ? PURPLE : new Color(241, 237, 252));
        button.setForeground(primario ? Color.WHITE : PURPLE);
        button.setContentAreaFilled(false);
        button.setBorder(new EmptyBorder(11, 16, 11, 16));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        return button;
    }

    private static class Card extends JPanel {
        private final Color cor;
        Card(Color cor) { this.cor = cor; setOpaque(false); setAlignmentX(Component.LEFT_ALIGNMENT); }
        @Override protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(cor);
            g.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
            g.dispose();
            super.paintComponent(graphics);
        }
    }
    private static class Marca extends JPanel {
        @Override protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setPaint(new GradientPaint(0, 0, NAVY, getWidth(), getHeight(), new Color(68, 42, 121)));
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(new Color(255, 255, 255, 12));
            g.setStroke(new BasicStroke(1.5f));
            for (int i = 0; i < 5; i++) g.drawOval(80 + i * 34, 70 + i * 34, 350, 350);
            g.dispose();
        }
    }
    private static class Movimento {
        final Conta conta;
        final BigDecimal valor;
        final boolean deposito;
        final String descricao;
        final String data = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM • HH:mm"));
        Movimento(Conta conta, BigDecimal valor, boolean deposito) {
            this(conta, valor, deposito, deposito ? "Depósito" : "Saque");
        }
        Movimento(Conta conta, BigDecimal valor, boolean deposito, String descricao) {
            this.conta = conta; this.valor = valor; this.deposito = deposito;
            this.descricao = descricao;
        }
    }
}
