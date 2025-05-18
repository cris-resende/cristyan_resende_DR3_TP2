package br.edu.infnet.java.service;

import br.edu.infnet.java.model.Paciente;
import br.edu.infnet.java.model.Consulta;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CalculadoraReembolsoTest {

    private CalculadoraReembolso calculadora;
    private HistoricoConsultasFake historicoConsultas;
    private AuditoriaSpy auditoriaSpy;
    private AutorizadorReembolso autorizadorMock;

    @BeforeEach
    public void setup() {
        historicoConsultas = new HistoricoConsultasFake();
        auditoriaSpy = new AuditoriaSpy();
        autorizadorMock = mock(AutorizadorReembolso.class);
        calculadora = new CalculadoraReembolso(historicoConsultas, auditoriaSpy, autorizadorMock);
    }
    //Automatizando a criação do objeto consulta
    private Consulta criarConsulta(String nomePaciente, double valor) {
        Paciente paciente = new Paciente(nomePaciente);
        return new Consulta(valor, LocalDateTime.now(), paciente, "Consulta de " + nomePaciente);
    }

    @Test
    public void deveChamarAuditoriaAoRegistrarConsulta() {
        when(autorizadorMock.autorizar(any())).thenReturn(true);

        Paciente paciente = new Paciente("Carlos");
        calculadora.calcular(100, 0.5, paciente);

        assertTrue(auditoriaSpy.foiChamado(), "A auditoria não foi chamada conforme esperado.");
    }

    @Test
    public void naoDeveChamarAuditoriaSeConsultaNaoForRegistrada() {
        assertFalse(auditoriaSpy.foiChamado());
    }

    @Test
    public void deveLancarExcecaoQuandoConsultaNaoAutorizada() {
        when(autorizadorMock.autorizar(any())).thenReturn(false);

        Paciente paciente = new Paciente("Paulo");

        RuntimeException excecao = assertThrows(RuntimeException.class, () -> {
            calculadora.calcular(200, 0.5, paciente);
        });

        assertEquals("Consulta não autorizada para reembolso.", excecao.getMessage());
        verify(autorizadorMock, times(1)).autorizar(any());
    }

    @Test
    public void deveCalcularReembolsoCorretamenteComPercentual() {
        when(autorizadorMock.autorizar(any())).thenReturn(true);

        assertEquals(140, calculadora.calcular(200, 0.7), 0.001);
    }

    @Test
    public void deveCalcularReembolsoCorretamenteComPlano50() {
        when(autorizadorMock.autorizar(any())).thenReturn(true);

        Paciente paciente = new Paciente("João");
        PlanoSaude plano50 = new Plano50();
        double valorReembolso = calculadora.calcular(200, plano50, paciente);
        assertEquals(100, valorReembolso, 0.001);
    }

    @Test
    public void deveCalcularReembolsoCorretamenteComPlano80() {
        when(autorizadorMock.autorizar(any())).thenReturn(true);

        Paciente paciente = new Paciente("Maria");
        PlanoSaude plano80 = new Plano80();
        double valorReembolso = calculadora.calcular(200, plano80, paciente);
        assertEquals(160, valorReembolso, 0.001);
    }

    @Test
    public void deveRetornarZeroQuandoValorConsultaForZero() {
        when(autorizadorMock.autorizar(any())).thenReturn(true);

        assertEquals(0, calculadora.calcular(0, 0.7), 0.001);
    }

    @Test
    public void deveRetornarZeroQuandoPercentualCoberturaForZero() {
        when(autorizadorMock.autorizar(any())).thenReturn(true);

        assertEquals(0, calculadora.calcular(200, 0), 0.001);
    }

    @Test
    public void deveCalcularReembolsoQuandoPercentualCoberturaForCemPorcento() {
        when(autorizadorMock.autorizar(any())).thenReturn(true);

        assertEquals(200, calculadora.calcular(200, 1), 0.001);
    }

    @Test
    public void deveRetornarZeroQuandoValorConsultaEPercentualCoberturaForemZero() {
        when(autorizadorMock.autorizar(any())).thenReturn(true);

        assertEquals(0, calculadora.calcular(0, 0), 0.001);
    }

    @Test
    void deveRegistrarMultiplasConsultasNoHistorico() {
        when(autorizadorMock.autorizar(any())).thenReturn(true);

        calculadora.calcular(100, 0.5, new Paciente("João"));
        calculadora.calcular(200, 0.7, new Paciente("Maria"));
        List<Consulta> consultas = historicoConsultas.listarConsultas();
        assertEquals(2, consultas.size());
        assertEquals("João", consultas.get(0).getPaciente().getNome());
        assertEquals("Maria", consultas.get(1).getPaciente().getNome());
    }

    @Test
    public void deveRegistrarConsultaComPlano50() {
        when(autorizadorMock.autorizar(any())).thenReturn(true);

        Paciente paciente = new Paciente("Lucas");
        PlanoSaude plano50 = new Plano50();
        calculadora.calcular(100, plano50, paciente);

        List<Consulta> consultas = historicoConsultas.listarConsultas();
        assertEquals(1, consultas.size());
        assertEquals("Lucas", consultas.get(0).getPaciente().getNome());
    }

    @Test
    public void deveRegistrarConsultaComPlano80() {
        when(autorizadorMock.autorizar(any())).thenReturn(true);

        Paciente paciente = new Paciente("Carla");
        PlanoSaude plano80 = new Plano80();
        calculadora.calcular(200, plano80, paciente);

        List<Consulta> consultas = historicoConsultas.listarConsultas();
        assertEquals(1, consultas.size());
        assertEquals("Carla", consultas.get(0).getPaciente().getNome());
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

    @Test
    void deveRegistrarConsultaCorretamenteComPlano() {
        when(autorizadorMock.autorizar(any())).thenReturn(true);

        Paciente paciente = new Paciente("Felipe");
        PlanoSaude plano50 = new Plano50();
        calculadora.calcular(150, plano50, paciente);

        List<Consulta> consultas = historicoConsultas.listarConsultas();
        assertEquals(1, consultas.size());
        assertEquals("Felipe", consultas.get(0).getPaciente().getNome());
        assertEquals(150, consultas.get(0).getValor(), 0.001);
    }
}
