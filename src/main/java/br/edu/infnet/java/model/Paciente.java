package br.edu.infnet.java.model;

public class Paciente {
    private String nome;

    public Paciente(String nome) {
        this.nome = nome != null && !nome.isEmpty() ? nome : "Desconhecido";
    }

    public String getNome() {
        return nome;
    }
}