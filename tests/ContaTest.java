import java.math.BigDecimal;

public class ContaTest {
    public static void main(String[] args) {
        Cliente cliente = new Cliente("Ana", "12345678901", "ana@example.com", "segredo", "11999999999");
        Conta conta = new Conta("0001", cliente);
        verificar(cliente.autenticar("segredo"), "Senha correta");
        verificar(!cliente.autenticar("errada"), "Senha incorreta");
        verificar(conta.depositar(new BigDecimal("0.10")), "Depósito de dez centavos");
        verificar(conta.depositar(new BigDecimal("0.20")), "Depósito de vinte centavos");
        verificar(conta.getSaldo().equals(new BigDecimal("0.30")), "Precisão decimal");
        verificar(!conta.sacar(new BigDecimal("0.31")), "Saque acima do saldo");
        verificar(!conta.sacar(new BigDecimal("-1")), "Saque negativo");
        verificar(!conta.depositar(BigDecimal.ZERO), "Depósito zero");
        verificar(!conta.depositar(new BigDecimal("-1")), "Depósito negativo");
        verificar(!conta.depositar(new BigDecimal("0.001")), "Fração de centavo");
        verificar(!conta.sacar(BigDecimal.ZERO), "Saque zero");
        verificar(!conta.sacar(new BigDecimal("0.001")), "Saque com fração de centavo");
        verificar(!conta.depositar(null) && !conta.sacar(null), "Valores nulos");
        verificar(conta.getSaldo().equals(new BigDecimal("0.30")), "Recusas preservam saldo");
        verificar(conta.sacar(new BigDecimal("0.30")), "Saque de todo o saldo");
        verificar(conta.getSaldo().equals(new BigDecimal("0.00")), "Saldo final zero");
        System.out.println("Todos os testes passaram.");
    }

    private static void verificar(boolean resultado, String caso) {
        if (!resultado) throw new AssertionError(caso);
    }
}
