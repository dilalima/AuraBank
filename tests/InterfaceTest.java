import java.awt.Component;
import java.awt.Container;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/** Teste de fluxos pelos componentes reais, sem abrir janelas. */
public class InterfaceTest {
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            AuraBankUI ui = new AuraBankUI();
            clicar(ui, "alternar");
            preencher(ui, "nome", "Ana Silva");
            preencher(ui, "cpf", "123.456.789-01");
            preencher(ui, "telefone", "11999999999");
            preencher(ui, "email", "ana@example.com");
            preencher(ui, "senha", "segredo");
            clicar(ui, "enviar");
            exigir(contem(ui, "Olá, Ana!"), "Cadastro abre o painel");
            preencher(ui, "valorDeposito", "100,50");
            clicar(ui, "depositar");
            exigir(contem(ui, "100,50"), "Depósito atualiza saldo");
            preencher(ui, "valorSaque", "101");
            clicar(ui, "sacar");
            exigir(contem(ui, "Saldo insuficiente"), "Saque acima do saldo recusado");
            preencher(ui, "valorSaque", "20.25");
            clicar(ui, "sacar");
            exigir(contem(ui, "80,25"), "Saque atualiza saldo");
            preencher(ui, "valorDeposito", "-5");
            clicar(ui, "depositar");
            exigir(contem(ui, "Use um valor"), "Valor inválido recusado");
            clicar(ui, "ocultar");
            exigir(!contem(ui, "80,25") && !contem(ui, "100,50"), "Oculta saldo e histórico");
            clicar(ui, "sair");
            preencher(ui, "cpf", "12345678901");
            preencher(ui, "senha", "errada");
            clicar(ui, "enviar");
            exigir(contem(ui, "CPF ou senha incorretos"), "Login incorreto recusado");
            preencher(ui, "senha", "segredo");
            clicar(ui, "enviar");
            exigir(contem(ui, "80,25"), "Login preserva saldo");
            clicar(ui, "sair");
            clicar(ui, "alternar");
            preencher(ui, "nome", "Outra Ana");
            preencher(ui, "cpf", "12345678901");
            preencher(ui, "telefone", "11999999999");
            preencher(ui, "email", "ana@example.com");
            preencher(ui, "senha", "segredo");
            clicar(ui, "enviar");
            exigir(contem(ui, "Este CPF já possui"), "CPF duplicado recusado");
            preencher(ui, "nome", "Bia Silva");
            preencher(ui, "cpf", "98765432100");
            preencher(ui, "email", "bia@example.com");
            clicar(ui, "enviar");
            exigir(!((JButton) encontrar(ui, "transferir")).isEnabled(), "Sem amigos não transfere");
            preencher(ui, "contaAmigo", "9999");
            clicar(ui, "solicitarAmizade");
            exigir(contem(ui, "Conta não encontrada"), "Conta inexistente recusada");
            preencher(ui, "contaAmigo", "0001");
            clicar(ui, "solicitarAmizade");
            exigir(contem(ui, "Solicitação enviada"), "Solicitação enviada pela interface");
            clicar(ui, "sair");
            preencher(ui, "cpf", "12345678901");
            preencher(ui, "senha", "segredo");
            clicar(ui, "enviar");
            clicar(ui, "recusarAmizade0002");
            exigir(contem(ui, "Solicitação recusada"), "Recusa pela interface");
            preencher(ui, "contaAmigo", "0002");
            clicar(ui, "solicitarAmizade");
            clicar(ui, "sair");
            preencher(ui, "cpf", "98765432100");
            preencher(ui, "senha", "segredo");
            clicar(ui, "enviar");
            clicar(ui, "aceitarAmizade0001");
            exigir(((JButton) encontrar(ui, "transferir")).isEnabled(), "Aceite habilita transferência");
            clicar(ui, "sair");
            preencher(ui, "cpf", "12345678901");
            preencher(ui, "senha", "segredo");
            clicar(ui, "enviar");
            preencher(ui, "valorTransferencia", "81");
            clicar(ui, "transferir");
            exigir(contem(ui, "saldo insuficiente"), "Transferência acima do saldo recusada");
            preencher(ui, "valorTransferencia", "25,25");
            clicar(ui, "transferir");
            exigir(contem(ui, "55,00") && contem(ui, "Transferência para Bia Silva"), "Débito e histórico do remetente");
            clicar(ui, "ocultar");
            exigir(!contem(ui, "25,25") && !contem(ui, "55,00"), "Oculta valores transferidos");
            clicar(ui, "sair");
            preencher(ui, "cpf", "98765432100");
            preencher(ui, "senha", "segredo");
            clicar(ui, "enviar");
            exigir(contem(ui, "25,25") && contem(ui, "Transferência de Ana Silva"), "Crédito e histórico do destinatário");
        });
        System.out.println("Todos os testes da interface passaram.");
    }

    static Component encontrar(Container raiz, String nome) {
        for (Component componente : raiz.getComponents()) {
            if (nome.equals(componente.getName())) return componente;
            if (componente instanceof Container) {
                Component resultado = encontrar((Container) componente, nome);
                if (resultado != null) return resultado;
            }
        }
        return null;
    }
    static void preencher(Container ui, String nome, String valor) {
        ((JTextField) encontrar(ui, nome)).setText(valor);
    }
    static void clicar(Container ui, String nome) {
        ((JButton) encontrar(ui, nome)).doClick();
    }
    static boolean contem(Container raiz, String texto) {
        for (Component componente : raiz.getComponents()) {
            if (componente instanceof JLabel && ((JLabel) componente).getText().contains(texto)) return true;
            if (componente instanceof Container && contem((Container) componente, texto)) return true;
        }
        return false;
    }
    static void exigir(boolean condicao, String caso) {
        if (!condicao) throw new AssertionError(caso);
    }
}
