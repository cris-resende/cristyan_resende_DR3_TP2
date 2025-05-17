package br.edu.infnet.java.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReembolsoServiceTest {

    @Test
    public void deveCalcularReembolsoCorretamente() {
        double valorConsulta = 200.0;
        double percentualCobertura = 0.70;
        double valorEsperado = 140.0;

        double resultado = ReembolsoService.calcularReembolso(valorConsulta, percentualCobertura);

        assertEquals(valorEsperado, resultado, 0.001); // delta para precisão de ponto flutuante
    }
}