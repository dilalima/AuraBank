package com.aurabank;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "contas")
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_conta", unique = true, nullable = false)
    private String numeroConta;

    @OneToOne
    @JoinColumn(name = "cliente_id", nullable = false, unique = true)
    private Cliente titular;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldo = new BigDecimal("0.00");

    @ManyToMany
    @JoinTable(
            name = "conta_amigos",
            joinColumns = @JoinColumn(name = "conta_id"),
            inverseJoinColumns = @JoinColumn(name = "amigo_id")
    )
    private Set<Conta> amigos = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(
            name = "solicitacoes_amizade",
            joinColumns = @JoinColumn(name = "destinatario_id"),
            inverseJoinColumns = @JoinColumn(name = "remetente_id")
    )
    private Set<Conta> solicitacoes = new LinkedHashSet<>();

    // Necessário para o JPA/Hibernate
    protected Conta() {
    }

    public Conta(String numeroConta, Cliente titular) {
        this.numeroConta = numeroConta;
        this.titular = titular;
    }

    public Long getId() {
        return id;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public Cliente getTitular() {
        return titular;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public List<Conta> getAmigos() {
        return Collections.unmodifiableList(new ArrayList<>(amigos));
    }

    public List<Conta> getSolicitacoes() {
        return Collections.unmodifiableList(new ArrayList<>(solicitacoes));
    }

    public boolean solicitarAmizade(Conta destino) {
        if (destino == null
                || destino == this
                || amigos.contains(destino)
                || solicitacoes.contains(destino)) {
            return false;
        }

        return destino.solicitacoes.add(this);
    }

    public boolean responderAmizade(Conta remetente, boolean aceitar) {
        if (!solicitacoes.remove(remetente)) {
            return false;
        }

        if (aceitar) {
            amigos.add(remetente);
            remetente.amigos.add(this);
        }

        return true;
    }

    public boolean transferir(Conta destino, BigDecimal valor) {
        if (destino == null
                || destino == this
                || !amigos.contains(destino)
                || !valorValido(valor)
                || valor.compareTo(saldo) > 0) {
            return false;
        }

        BigDecimal novoSaldo =
                saldo.subtract(valor).setScale(2, RoundingMode.UNNECESSARY);

        BigDecimal saldoDestino =
                destino.saldo.add(valor).setScale(2, RoundingMode.UNNECESSARY);

        saldo = novoSaldo;
        destino.saldo = saldoDestino;

        return true;
    }

    public boolean depositar(BigDecimal valor) {
        if (!valorValido(valor)) {
            return false;
        }

        saldo = saldo.add(valor)
                .setScale(2, RoundingMode.UNNECESSARY);

        return true;
    }

    public boolean sacar(BigDecimal valor) {
        if (!valorValido(valor) || valor.compareTo(saldo) > 0) {
            return false;
        }

        saldo = saldo.subtract(valor)
                .setScale(2, RoundingMode.UNNECESSARY);

        return true;
    }

    private boolean valorValido(BigDecimal valor) {
        return valor != null
                && valor.signum() > 0
                && valor.stripTrailingZeros().scale() <= 2;
    }

    @Override
    public String toString() {
        return titular.getNome() + " (conta " + numeroConta + ")";
    }
}