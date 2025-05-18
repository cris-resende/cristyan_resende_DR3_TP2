package br.edu.infnet.java.service;

import br.edu.infnet.java.model.Paciente;
import br.edu.infnet.java.model.Consulta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CalculadoraReembolsoTest {

    private CalculadoraReembolso calculadora;
    private HistoricoConsultasFake historicoConsultas;

    @BeforeEach
    public void setup() {
        historicoConsultas = new HistoricoConsultasFake();
        calculadora = new CalculadoraReembolso(historicoConsultas);
    }

    @Test
    public void deveCalcularReembolsoCorretamente() {
        assertEquals(140, calculadora.calcular(200, 0.7), 0.001);
    }

    @Test
    public void deveRetornarZeroQuandoValorConsultaForZero() {
        assertEquals(0, calculadora.calcular(0, 0.7), 0.001);
    }

    @Test
    public void deveRetornarZeroQuandoPercentualCoberturaForZero() {
        assertEquals(0, calculadora.calcular(200, 0), 0.001);
    }

    @Test
    public void deveCalcularReembolsoQuandoPercentualCoberturaForCemPorcento() {
        assertEquals(200, calculadora.calcular(200, 1), 0.001);
    }

    @Test
    public void deveRetornarZeroQuandoValorConsultaEPercentualCoberturaForemZero() {
        assertEquals(0, calculadora.calcular(0, 0), 0.001);
    }

    @Test
    void deveRegistrarMultiplasConsultasNoHistorico() {
        calculadora.calcular(100, 0.5, new Paciente("João"));
        calculadora.calcular(200, 0.7, new Paciente("Maria"));
        List<Consulta> consultas = historicoConsultas.listarConsultas();
        assertEquals(2, consultas.size());
    }

    @Test
    public void deveIniciarComHistoricoVazio() {
        assertTrue(historicoConsultas.listarConsultas().isEmpty());
    }

    @Test
    void deveRegistrarConsultaComPacienteDummy() {
        Paciente paciente = new Paciente("Dummy");
        assertEquals("Dummy", paciente.getNome());
    }
}

