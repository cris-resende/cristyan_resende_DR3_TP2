package br.edu.infnet.java.model;

import java.time.LocalDateTime;

public class Consulta {
    private double valor;
    private LocalDateTime data;
    private Paciente paciente;
    private String descricao;

    public Consulta(double valor, LocalDateTime data, Paciente paciente, String descricao) {
        this.valor = valor;
        this.data = data != null ? data : LocalDateTime.now();
        this.paciente = paciente != null ? paciente : new Paciente("Desconhecido");
        this.descricao = descricao != null ? descricao : "Consulta sem descrição";
    }
    public Consulta(double valor, LocalDateTime data, String descricao) {
        this(valor, data, new Paciente("Desconhecido"), descricao);
    }

    public double getValor() {
        return valor;
    }

    public LocalDateTime getData() {
        return data;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public String getDescricao() {
        return descricao;
    }
}
