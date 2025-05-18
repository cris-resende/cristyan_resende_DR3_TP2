package br.edu.infnet.java.service;

import br.edu.infnet.java.model.Paciente;
import br.edu.infnet.java.model.Consulta;
import java.time.LocalDateTime;

public class CalculadoraReembolso {

    private final HistoricoConsultas historicoConsultas;

    // Construtor com injeção de dependência para o histórico
    public CalculadoraReembolso(HistoricoConsultas historicoConsultas) {
        this.historicoConsultas = historicoConsultas;
    }

    // Construtor sem histórico (para cenários onde o registro não é necessário)
    public CalculadoraReembolso() {
        this.historicoConsultas = null;
    }

    // Método de cálculo usando um plano de saúde
    public double calcular(double valorConsulta, PlanoSaude planoSaude, Paciente paciente) {
        return calcular(valorConsulta, planoSaude.getPercentualCobertura(), paciente);
    }

    // Método de cálculo usando percentual direto e paciente
    public double calcular(double valorConsulta, double percentualCobertura, Paciente paciente) {
        double valorReembolso = valorConsulta * percentualCobertura;

        // Registrar a consulta no histórico se ele estiver disponível
        if (historicoConsultas != null) {
            Consulta consulta = new Consulta(valorConsulta, LocalDateTime.now(), paciente,
                    "Consulta de " + paciente.getNome());
            historicoConsultas.salvarConsulta(consulta);
        }

        return valorReembolso;
    }

    // Método de cálculo sem paciente (sem registro no histórico)
    public double calcular(double valorConsulta, double percentualCobertura) {
        return valorConsulta * percentualCobertura;
    }
}
