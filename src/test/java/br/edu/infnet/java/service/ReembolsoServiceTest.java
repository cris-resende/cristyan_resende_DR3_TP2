package br.edu.infnet.java.service;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
  Testes unitários para ReembolsoService, validando a integração com a CalculadoraReembolso
  através do uso de mocks para garantir isolamento.
 */
public class ReembolsoServiceTest {

    @Test
    public void deveDelegarCalculoParaCalculadoraReembolso() {
        // Cria um mock da CalculadoraReembolso
        CalculadoraReembolso mockCalculadora = mock(CalculadoraReembolso.class);

        // Serviço recebe o mock
        ReembolsoService service = new ReembolsoService(mockCalculadora);

        double valorConsulta = 200.0;
        double percentualCobertura = 0.7;
        double valorEsperado = 140.0;

        // Define comportamento do mock
        when(mockCalculadora.calcular(valorConsulta, percentualCobertura)).thenReturn(valorEsperado);

        // Executa o método do serviço
        double resultado = service.calcularReembolso(valorConsulta, percentualCobertura);

        // Verifica se o resultado é o esperado
        assertEquals(valorEsperado, resultado, 0.001);

        // Verifica se o método calcular da calculadora foi chamado corretamente
        verify(mockCalculadora).calcular(valorConsulta, percentualCobertura);
    }
}
