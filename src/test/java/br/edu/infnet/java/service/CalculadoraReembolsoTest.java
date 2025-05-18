package br.edu.infnet.java.service;

import br.edu.infnet.java.model.Paciente;
import br.edu.infnet.java.model.Consulta;
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
        historicoConsultas = new HistoricoConsultasFake(); // Fake para simular armazenamento das consultas
        auditoriaSpy = new AuditoriaSpy(); // Spy para verificar se auditoria foi chamada
        autorizadorMock = mock(AutorizadorReembolso.class); // Mock para controlar autorização de reembolso
        calculadora = new CalculadoraReembolso(historicoConsultas, auditoriaSpy, autorizadorMock);
    }

    private boolean compararComMargem(double valorEsperado, double valorObtido) {
        double margemErro = 0.01;
        return Math.abs(valorEsperado - valorObtido) <= margemErro;
    }

    // Automatizando a criação do objeto consulta
    private Consulta criarConsulta(String nomePaciente, double valor) {
        Paciente paciente = new Paciente(nomePaciente);
        return new Consulta(valor, LocalDateTime.now(), paciente, "Consulta de " + nomePaciente);
    }

    @Test
    public void testeIntegracaoCompletaComDubles() {
        // Stub para percentual fixo de cobertura do plano
        PlanoSaude planoStub = new PlanoSaude() {
            @Override
            public double getPercentualCobertura() {
                return 0.8;
            }
        };

        // Configura mock para autorizar sempre
        when(autorizadorMock.autorizar(any())).thenReturn(true);

        Paciente paciente = new Paciente("Ana");
        double valorReembolso = calculadora.calcular(200, planoStub, paciente);

        // Verifica valor calculado com margem
        assertTrue(compararComMargem(150, valorReembolso));
        // Verifica se auditoria foi chamada via spy
        assertTrue(auditoriaSpy.foiChamado());
        // Verifica se consulta foi registrada no histórico fake
        assertEquals(1, historicoConsultas.listarConsultas().size());
    }

    @Test
    public void deveChamarAuditoriaAoRegistrarConsulta() {
        // Mock para autorizar o reembolso
        when(autorizadorMock.autorizar(any())).thenReturn(true);

        Paciente paciente = new Paciente("Carlos");
        calculadora.calcular(100, 0.5, paciente);

        // Verifica se auditoria foi chamada ao registrar consulta
        assertTrue(auditoriaSpy.foiChamado(), "A auditoria não foi chamada conforme esperado.");
    }

    @Test
    public void naoDeveChamarAuditoriaSeConsultaNaoForRegistrada() {
        // Verifica que auditoria não é chamada sem registrar consulta
        assertFalse(auditoriaSpy.foiChamado());
    }

    @Test
    public void deveLancarExcecaoQuandoConsultaNaoAutorizada() {
        // Mock para negar autorização da consulta
        when(autorizadorMock.autorizar(any())).thenReturn(false);

        Paciente paciente = new Paciente("Paulo");

        // Verifica exceção lançada quando consulta não é autorizada
        RuntimeException excecao = assertThrows(RuntimeException.class, () -> {
            calculadora.calcular(200, 0.5, paciente);
        });

        assertEquals("Consulta não autorizada para reembolso.", excecao.getMessage());
        // Verifica que método autorizar foi chamado exatamente uma vez
        verify(autorizadorMock, times(1)).autorizar(any());
    }

    @Test
    public void deveCalcularReembolsoCorretamenteComPercentual() {
        // Mock para autorizar a consulta
        when(autorizadorMock.autorizar(any())).thenReturn(true);

        Paciente paciente = new Paciente("Teste");
        // Testa cálculo de reembolso com percentual e auditoria
        double resultado = calculadora.calcular(200, 0.7, paciente);

        assertTrue(compararComMargem(140, resultado));
        assertTrue(auditoriaSpy.foiChamado());
    }

    @Test
    public void deveCalcularReembolsoCorretamenteComPlano50() {
        // Mock para autorizar consulta
        when(autorizadorMock.autorizar(any())).thenReturn(true);

        Paciente paciente = new Paciente("João");
        PlanoSaude plano50 = new Plano50();
        // Verifica cálculo de reembolso com plano 50%
        double valorReembolso = calculadora.calcular(200, plano50, paciente);
        assertTrue(compararComMargem(100, valorReembolso));
    }

    @Test
    public void deveCalcularReembolsoCorretamenteComPlano80() {
        // Mock autoriza consulta
        when(autorizadorMock.autorizar(any())).thenReturn(true);

        Paciente paciente = new Paciente("Maria");
        PlanoSaude plano80 = new Plano80();
        // Verifica cálculo com plano 80%
        double valorReembolso = calculadora.calcular(150, plano80, paciente);
        assertTrue(compararComMargem(120, valorReembolso));
    }

    @Test
    public void deveRetornarZeroQuandoValorConsultaForZero() {
        // Mock autoriza consulta
        when(autorizadorMock.autorizar(any())).thenReturn(true);

        // Verifica que reembolso é zero quando valor da consulta é zero
        assertTrue(compararComMargem(0, calculadora.calcular(0, 0.7)));
    }

    @Test
    public void deveRetornarZeroQuandoPercentualCoberturaForZero() {
        // Mock autoriza consulta
        when(autorizadorMock.autorizar(any())).thenReturn(true);

        // Verifica que reembolso é zero quando percentual de cobertura é zero
        assertTrue(compararComMargem(0, calculadora.calcular(200, 0)));
    }

    @Test
    public void deveCalcularReembolsoQuandoPercentualCoberturaForCemPorcento() {
        // Mock autoriza consulta
        when(autorizadorMock.autorizar(any())).thenReturn(true);

        // Verifica cálculo correto com 100% de cobertura
        assertTrue(compararComMargem(150, calculadora.calcular(200, 1)));
    }

    @Test
    public void deveRetornarZeroQuandoValorConsultaEPercentualCoberturaForemZero() {
        // Mock autoriza consulta
        when(autorizadorMock.autorizar(any())).thenReturn(true);

        // Verifica reembolso zero quando ambos valor e percentual são zero
        assertTrue(compararComMargem(0, calculadora.calcular(0, 0)));
    }

    @Test
    void deveRegistrarMultiplasConsultasNoHistorico() {
        // Mock autoriza consultas
        when(autorizadorMock.autorizar(any())).thenReturn(true);

        // Registra múltiplas consultas e verifica histórico
        calculadora.calcular(100, 0.5, new Paciente("João"));
        calculadora.calcular(200, 0.7, new Paciente("Maria"));
        List<Consulta> consultas = historicoConsultas.listarConsultas();
        assertEquals(2, consultas.size());
        assertEquals("João", consultas.get(0).getPaciente().getNome());
        assertEquals("Maria", consultas.get(1).getPaciente().getNome());
    }

    @Test
    public void deveRegistrarConsultaComPlano50() {
        // Mock autoriza consulta
        when(autorizadorMock.autorizar(any())).thenReturn(true);

        Paciente paciente = new Paciente("Lucas");
        PlanoSaude plano50 = new Plano50();
        // Testa registro da consulta com plano 50%
        calculadora.calcular(100, plano50, paciente);

        List<Consulta> consultas = historicoConsultas.listarConsultas();
        assertEquals(1, consultas.size());
        assertEquals("Lucas", consultas.get(0).getPaciente().getNome());
    }

    @Test
    public void deveRegistrarConsultaComPlano80() {
        // Mock autoriza consulta
        when(autorizadorMock.autorizar(any())).thenReturn(true);

        Paciente paciente = new Paciente("Carla");
        PlanoSaude plano80 = new Plano80();
        // Testa registro da consulta com plano 80%
        calculadora.calcular(200, plano80, paciente);

        List<Consulta> consultas = historicoConsultas.listarConsultas();
        assertEquals(1, consultas.size());
        assertEquals("Carla", consultas.get(0).getPaciente().getNome());
    }

    @Test
    public void deveIniciarComHistoricoVazio() {
        // Verifica que histórico inicia vazio (sem registros)
        assertTrue(historicoConsultas.listarConsultas().isEmpty());
    }

    @Test
    void deveRegistrarConsultaComPacienteDummy() {
        // Testa criação de paciente dummy simples
        Paciente paciente = new Paciente("Dummy");
        assertEquals("Dummy", paciente.getNome());
    }

    @Test
    void deveRegistrarConsultaCorretamenteComPlano() {
        // Mock autoriza consulta
        when(autorizadorMock.autorizar(any())).thenReturn(true);

        Paciente paciente = new Paciente("Felipe");
        PlanoSaude plano50 = new Plano50();
        // Testa registro e valores da consulta com plano
        calculadora.calcular(150, plano50, paciente);

        List<Consulta> consultas = historicoConsultas.listarConsultas();
        assertEquals(1, consultas.size());
        assertEquals("Felipe", consultas.get(0).getPaciente().getNome());
        assertEquals(150, consultas.get(0).getValor());
    }

    // --- Novos testes para validar teto de R$150 no reembolso ---

    @Test
    public void deveAplicarTetoDeReembolsoQuandoValorExcederLimite() {
        // Mock autoriza consulta
        when(autorizadorMock.autorizar(any())).thenReturn(true);

        // Valor calculado ultrapassa o teto de R$150, deve ser limitado
        double valorReembolso = calculadora.calcular(200, 0.9);
        assertTrue(compararComMargem(150, valorReembolso), "Reembolso deve ser limitado a R$150");
    }

    @Test
    public void devePermitirValorReembolsoAbaixoDoTeto() {
        // Mock autoriza consulta
        when(autorizadorMock.autorizar(any())).thenReturn(true);

        // Valor abaixo do teto não sofre alteração
        double valorReembolso = calculadora.calcular(100, 0.7);
        assertTrue(compararComMargem(70, valorReembolso), "Reembolso abaixo do teto deve ser permitido sem alteração");
    }
}