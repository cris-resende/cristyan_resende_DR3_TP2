package br.edu.infnet.java.service;

import br.edu.infnet.java.model.Paciente;
import br.edu.infnet.java.model.Consulta;
import java.time.LocalDateTime;

public class CalculadoraReembolso {

    private final HistoricoConsultas historicoConsultas;
    private final Auditoria auditoria;

    // Construtor com injeção de dependência para histórico e auditoria
    public CalculadoraReembolso(HistoricoConsultas historicoConsultas, Auditoria auditoria) {
        this.historicoConsultas = historicoConsultas;
        this.auditoria = auditoria;
    }

    // Construtor somente com histórico (sem auditoria)
    public CalculadoraReembolso(HistoricoConsultas historicoConsultas) {
        this.historicoConsultas = historicoConsultas;
        this.auditoria = null;
    }

    // Método de cálculo usando um plano de saúde e paciente
    public double calcular(double valorConsulta, PlanoSaude planoSaude, Paciente paciente) {
        return calcular(valorConsulta, planoSaude.getPercentualCobertura(), paciente);
    }

    // Método de cálculo usando percentual direto e paciente
    public double calcular(double valorConsulta, double percentualCobertura, Paciente paciente) {
        double valorReembolso = valorConsulta * percentualCobertura;

        // Registrar a consulta no histórico se disponível
        if (historicoConsultas != null) {
            Consulta consulta = new Consulta(valorConsulta, LocalDateTime.now(), paciente,
                    "Consulta de " + paciente.getNome());
            historicoConsultas.salvarConsulta(consulta);

            // Registrar a auditoria se disponível
            if (auditoria != null) {
                auditoria.registrarConsulta(String.valueOf(consulta));
            }
        }

        return valorReembolso;
    }

    // Método de cálculo sem paciente (sem registro no histórico)
    public double calcular(double valorConsulta, double percentualCobertura) {
        return valorConsulta * percentualCobertura;
    }
}