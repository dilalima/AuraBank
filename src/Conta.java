import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Conta {
    private final String numeroConta;
    private final Cliente titular;
    private BigDecimal saldo = new BigDecimal("0.00");
    private final Set<Conta> amigos = new LinkedHashSet<>();
    private final Set<Conta> solicitacoes = new LinkedHashSet<>();

    public List<Conta> getAmigos() {
        return Collections.unmodifiableList(new ArrayList<>(amigos));
    }

    public List<Conta> getSolicitacoes() {
        return Collections.unmodifiableList(new ArrayList<>(solicitacoes));
    }

    public boolean solicitarAmizade(Conta destino) {
        if (destino == null || destino == this || amigos.contains(destino)
                || solicitacoes.contains(destino)) return false;
        return destino.solicitacoes.add(this);
    }

    public boolean responderAmizade(Conta remetente, boolean aceitar) {
        if (!solicitacoes.remove(remetente)) return false;
        if (aceitar) {
            amigos.add(remetente);
            remetente.amigos.add(this);
        }
        return true;
    }

    public boolean transferir(Conta destino, BigDecimal valor) {
        if (destino == null || destino == this || !amigos.contains(destino)
                || !valorValido(valor) || valor.compareTo(saldo) > 0) return false;
        BigDecimal novoSaldo = saldo.subtract(valor).setScale(2, RoundingMode.UNNECESSARY);
        BigDecimal saldoDestino = destino.saldo.add(valor).setScale(2, RoundingMode.UNNECESSARY);
        saldo = novoSaldo;
        destino.saldo = saldoDestino;
        return true;
    }

    @Override public String toString() {
        return titular.getNome() + " (conta " + numeroConta + ")";
    }

    public Conta(String numeroConta, Cliente titular) {
        this.numeroConta = numeroConta;
        this.titular = titular;
    }

    public String getNumeroConta() { return numeroConta; }
    public Cliente getTitular() { return titular; }
    public BigDecimal getSaldo() { return saldo; }

    public boolean depositar(BigDecimal valor) {
        if (!valorValido(valor)) return false;
        saldo = saldo.add(valor).setScale(2, RoundingMode.UNNECESSARY);
        return true;
    }

    public boolean sacar(BigDecimal valor) {
        if (!valorValido(valor) || valor.compareTo(saldo) > 0) return false;
        saldo = saldo.subtract(valor).setScale(2, RoundingMode.UNNECESSARY);
        return true;
    }

    private boolean valorValido(BigDecimal valor) {
        return valor != null && valor.signum() > 0
                && valor.stripTrailingZeros().scale() <= 2;
    }
}
