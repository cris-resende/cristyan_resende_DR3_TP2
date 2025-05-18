package br.edu.infnet.java.service;

import br.edu.infnet.java.model.Paciente;
import br.edu.infnet.java.model.Consulta;
import java.time.LocalDateTime;

public class CalculadoraReembolso {

    private final HistoricoConsultas historicoConsultas;
    private final Auditoria auditoria;
    private final AutorizadorReembolso autorizador;

    // Construtor com todas as dependências
    public CalculadoraReembolso(HistoricoConsultas historicoConsultas, Auditoria auditoria, AutorizadorReembolso autorizador) {
        this.historicoConsultas = historicoConsultas;
        this.auditoria = auditoria;
        this.autorizador = autorizador;
    }

    // Construtor com apenas o histórico (para cenários sem auditoria e autorização)
    public CalculadoraReembolso(HistoricoConsultas historicoConsultas) {
        this(historicoConsultas, null, null);
    }

    // Método de cálculo usando um plano de saúde e paciente
    public double calcular(double valorConsulta, PlanoSaude planoSaude, Paciente paciente) {
        return calcular(valorConsulta, planoSaude.getPercentualCobertura(), paciente);
    }

    // Método de cálculo usando percentual direto e paciente
    public double calcular(double valorConsulta, double percentualCobertura, Paciente paciente) {
        Consulta consulta = new Consulta(valorConsulta, LocalDateTime.now(), paciente,
                "Consulta de " + paciente.getNome());

        // Verificação de autorização
        if (autorizador != null && !autorizador.autorizar(consulta)) {
            throw new RuntimeException("Consulta não autorizada para reembolso.");
        }

        double valorReembolso = valorConsulta * percentualCobertura;

        // Registrar a consulta no histórico se disponível
        if (historicoConsultas != null) {
            historicoConsultas.salvarConsulta(consulta);

            // Registrar a auditoria se disponível
            if (auditoria != null) {
                auditoria.registrarConsulta(consulta.toString());
            }
        }

        return valorReembolso;
    }

    // Método de cálculo sem paciente (sem registro no histórico)
    public double calcular(double valorConsulta, double percentualCobertura) {
        return valorConsulta * percentualCobertura;
    }
}
