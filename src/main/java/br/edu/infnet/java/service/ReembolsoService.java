package br.edu.infnet.java.service;

/**
  Serviço que gerencia a lógica de negócio para cálculo de reembolso,
  delegando a parte matemática para a CalculadoraReembolso.
 */

public class ReembolsoService {

    private final CalculadoraReembolso calculadora;

    public ReembolsoService() {
        this.calculadora = new CalculadoraReembolso(new HistoricoConsultasFake());
    }

    // Para facilitar testes, pode ser injetado um mock ou outra implementação:
    public ReembolsoService(CalculadoraReembolso calculadora) {
        this.calculadora = calculadora;
    }

    public double calcularReembolso(double valorConsulta, double percentualCobertura) {
        return calculadora.calcular(valorConsulta, percentualCobertura);
    }
}
