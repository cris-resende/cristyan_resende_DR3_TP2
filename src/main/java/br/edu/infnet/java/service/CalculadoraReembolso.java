package br.edu.infnet.java.service;

import br.edu.infnet.java.model.Paciente;
import br.edu.infnet.java.model.Consulta;
import java.time.LocalDateTime;

/**
 * Classe responsável por encapsular a lógica do cálculo do reembolso.
 * Mantém a lógica isolada para facilitar testes unitários específicos.
 * Agora utiliza um histórico para armazenar as consultas realizadas.
 */
public class CalculadoraReembolso {

    private final HistoricoConsultas historicoConsultas;

    // Construtor com injeção de dependência para o histórico
    public CalculadoraReembolso(HistoricoConsultas historicoConsultas) {
        this.historicoConsultas = historicoConsultas;
    }
    public CalculadoraReembolso() {
        this.historicoConsultas = null;
    }

    /**
     * Calcula o valor do reembolso com base no valor da consulta e percentual de cobertura.
     * O paciente e a consulta são registrados no histórico.
     */
    public double calcular(double valorConsulta, double percentualCobertura, Paciente paciente) {
        double valorReembolso = valorConsulta * percentualCobertura;

        // Criar uma consulta para registrar no histórico
        if (historicoConsultas != null) {
            Consulta consulta = new Consulta(valorConsulta, LocalDateTime.now(), paciente, "Consulta de " + paciente.getNome());
            historicoConsultas.salvarConsulta(consulta);
        }

        return valorReembolso;
    }

    /**
     * Sobrecarga para cálculo sem registro no histórico (caso o histórico não seja necessário).
     */
    public double calcular(double valorConsulta, double percentualCobertura) {
        return valorConsulta * percentualCobertura;
    }
}