package br.edu.infnet.java.service;

import br.edu.infnet.java.model.Paciente;

/**
 Classe responsável por encapsular a lógica do cálculo do reembolso.
 Mantém a lógica isolada para facilitar testes unitários específicos.
 */

public class CalculadoraReembolso {
    public double calcular(double valorConsulta, double percentualCobertura, Paciente paciente)
    {
        return valorConsulta * percentualCobertura;
    }
    public double calcular(double valorConsulta, double percentualCobertura){
        return valorConsulta * percentualCobertura;
    }
}
