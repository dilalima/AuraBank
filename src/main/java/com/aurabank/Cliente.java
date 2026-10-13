package com.aurabank;

import jakarta.persistence.*;

@Entity
@Table(name = "clientes")
public class  Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true, nullable = false, length = 11)
    private String cpf;

    private String email;
    private String senha;
    private String telefone;

    // O JPA precisa de um construtor vazio
    public Cliente() {
    }

    // Mantemos seu construtor atual
    public Cliente(String nome, String cpf, String email,
                   String senha, String telefone) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public boolean autenticar(String senha) {
        return this.senha.equals(senha);
    }
}