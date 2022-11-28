package br.com.uniq.database.dbos;

import java.util.Objects;

public class Patient implements Cloneable {
    private String nome;
    private String cpf;
    private int idade;
    private String senha;

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public int getIdade() {
        return idade;
    }

    public String getSenha() {
        return senha;
    }

    public Patient(String nome, String cpf, int idade, String senha) {
        this.nome = nome;
        this.cpf = cpf;
        this.idade = idade;
        this.senha = senha;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpf, nome, idade, senha);
    }

    @Override
    public String toString() {
        return "Patient{" +
                "nome='" + nome + '\'' +
                ", cpf='" + cpf + '\'' +
                ", idade=" + idade +
                ", senha='" + senha + '\'' +
                '}';
    }

}

