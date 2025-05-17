package br.edu.infnet.java.service;

import br.edu.infnet.java.model.Paciente;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
  Testes unitários para a classe CalculadoraReembolso,
  focando apenas na lógica do cálculo, isolada de outras dependências.
 */
public class CalculadoraReembolsoTest {

    private final CalculadoraReembolso calculadora = new CalculadoraReembolso();

    @Test
    public void deveCalcularReembolsoCorretamente() {
        double valorConsulta = 200.0;
        double percentualCobertura = 0.70;
        double valorEsperado = 140.0;

        double resultado = calculadora.calcular(valorConsulta, percentualCobertura);

        assertEquals(valorEsperado, resultado, 0.001);
    }

    @Test
    public void deveRetornarZeroQuandoValorConsultaForZero() {
        double valorConsulta = 0.0;
        double percentualCobertura = 0.70;
        double valorEsperado = 0.0;

        double resultado = calculadora.calcular(valorConsulta, percentualCobertura);

        assertEquals(valorEsperado, resultado, 0.001);
    }

    @Test
    public void deveRetornarZeroQuandoPercentualCoberturaForZero() {
        double valorConsulta = 200.0;
        double percentualCobertura = 0.0;
        double valorEsperado = 0.0;

        double resultado = calculadora.calcular(valorConsulta, percentualCobertura);

        assertEquals(valorEsperado, resultado, 0.001);
    }

    @Test
    public void deveCalcularReembolsoQuandoPercentualCoberturaForCemPorcento() {
        double valorConsulta = 200.0;
        double percentualCobertura = 1.0;
        double valorEsperado = 200.0;

        double resultado = calculadora.calcular(valorConsulta, percentualCobertura);

        assertEquals(valorEsperado, resultado, 0.001);
    }

    @Test
    public void deveRetornarZeroQuandoValorConsultaEPercentualCoberturaForemZero() {
        double valorConsulta = 0.0;
        double percentualCobertura = 0.0;
        double valorEsperado = 0.0;

        double resultado = calculadora.calcular(valorConsulta, percentualCobertura);

        assertEquals(valorEsperado, resultado, 0.001);
    }
    @Test
    public void deveCalcularReembolsoComPacienteDummy() {
        double valorConsulta = 200;
        double percentualCobertura = 0.7;
        double valorEsperado = 140;
        Paciente pacienteDummy = new Paciente();  // Dummy sem lógica

        double resultado = calculadora.calcular(valorConsulta, percentualCobertura, pacienteDummy);

        assertEquals(valorEsperado, resultado, 0.001);
    }
}
