import java.math.BigDecimal;

public class AmizadeTransferenciaTest {
    public static void main(String[] args) {
        Conta ana = conta("0001", "Ana");
        Conta bia = conta("0002", "Bia");
        Conta caio = conta("0003", "Caio");
        ana.depositar(new BigDecimal("100.50"));
        exigir(!ana.transferir(bia, BigDecimal.ONE), "Bloqueia não amigos");
        exigir(!ana.solicitarAmizade(ana) && !ana.solicitarAmizade(null), "Bloqueia destino inválido");
        exigir(ana.solicitarAmizade(bia), "Envia solicitação");
        exigir(!ana.solicitarAmizade(bia), "Bloqueia duplicata");
        exigir(!bia.solicitarAmizade(ana), "Solicitação cruzada deve ser respondida");
        exigir(!ana.responderAmizade(bia, true), "Somente destinatário responde");
        exigir(!ana.transferir(bia, BigDecimal.ONE), "Pendente não permite transferência");
        exigir(bia.responderAmizade(ana, false), "Recusa solicitação");
        exigir(bia.getSolicitacoes().isEmpty() && ana.getAmigos().isEmpty(), "Recusa não cria amizade");
        exigir(ana.solicitarAmizade(bia) && bia.responderAmizade(ana, true), "Reenvio e aceite");
        exigir(ana.getAmigos().contains(bia) && bia.getAmigos().contains(ana), "Amizade bilateral");
        exigir(!ana.solicitarAmizade(bia) && !bia.responderAmizade(ana, true), "Bloqueia repetição");
        exigir(!ana.transferir(bia, new BigDecimal("100.51")), "Saldo insuficiente");
        exigir(!ana.transferir(bia, BigDecimal.ZERO), "Valor zero");
        exigir(!ana.transferir(bia, new BigDecimal("-1")), "Valor negativo");
        exigir(!ana.transferir(bia, new BigDecimal("0.001")), "Fração de centavo");
        exigir(!ana.transferir(bia, null) && !ana.transferir(null, BigDecimal.ONE), "Valores nulos");
        exigir(!ana.transferir(ana, BigDecimal.ONE) && !ana.transferir(caio, BigDecimal.ONE), "Destinos proibidos");
        exigir(ana.getSaldo().equals(new BigDecimal("100.50")) && bia.getSaldo().signum() == 0,
                "Recusas preservam ambos os saldos");
        exigir(ana.transferir(bia, new BigDecimal("25.25")), "Transferência válida");
        exigir(ana.getSaldo().equals(new BigDecimal("75.25")) && bia.getSaldo().equals(new BigDecimal("25.25")),
                "Débito e crédito exatos");
        exigir(bia.transferir(ana, new BigDecimal("25.25")), "Transferência no sentido inverso");
        exigir(ana.transferir(bia, new BigDecimal("100.50")) && ana.getSaldo().signum() == 0,
                "Transferência de todo o saldo");
        System.out.println("Todos os testes de amizade e transferência passaram.");
    }

    private static Conta conta(String numero, String nome) {
        return new Conta(numero, new Cliente(nome, numero, nome + "@example.com", "senha", "11999999999"));
    }

    private static void exigir(boolean condicao, String caso) {
        if (!condicao) throw new AssertionError(caso);
    }
}
