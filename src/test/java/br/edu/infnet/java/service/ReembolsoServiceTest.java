package br.edu.infnet.java.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReembolsoServiceTest {

    @Test
    public void deveCalcularReembolsoCorretamente() {
        double valorConsulta = 200;
        double percentualCobertura = 0.7;
        double valorEsperado = 140;

        double resultado = ReembolsoService.calcularReembolso(valorConsulta, percentualCobertura);

        assertEquals(valorEsperado, resultado, 0.001); // delta para precisão de ponto flutuante
    }

    @Test
    public void deveRetornarZeroQuandoValorConsultaForZero() {
        double valorConsulta = 0.0;
        double percentualCobertura = 0.7;
        double valorEsperado = 0.0;

        double resultado = ReembolsoService.calcularReembolso(valorConsulta, percentualCobertura);

        assertEquals(valorEsperado, resultado, 0.001);
    }

    @Test
    public void deveRetornarZeroQuandoPercentualCoberturaForZero() {
        double valorConsulta = 200;
        double percentualCobertura = 0.0;
        double valorEsperado = 0.0;

        double resultado = ReembolsoService.calcularReembolso(valorConsulta, percentualCobertura);

        assertEquals(valorEsperado, resultado, 0.001);
    }

    @Test
    public void deveCalcularReembolsoQuandoPercentualCoberturaForCemPorcento() {
        double valorConsulta = 200;
        double percentualCobertura = 1;
        double valorEsperado = 200;

        double resultado = ReembolsoService.calcularReembolso(valorConsulta, percentualCobertura);

        assertEquals(valorEsperado, resultado, 0.001);
    }

    @Test
    public void deveRetornarZeroQuandoValorConsultaEPercentualCoberturaForemZero() {
        double valorConsulta = 0.0;
        double percentualCobertura = 0.0;
        double valorEsperado = 0.0;

        double resultado = ReembolsoService.calcularReembolso(valorConsulta, percentualCobertura);

        assertEquals(valorEsperado, resultado, 0.001);
    }

/* Neste serviço simples, não há dependências externas ou estado complexo, então o uso de mocks não é necessário.
    Em cenários futuros, onde o cálculo dependa de serviços externos,
    bancos de dados ou configurações dinâmicas, o Mockito pode ser usado
    para isolar essas dependências durante os testes. */

}