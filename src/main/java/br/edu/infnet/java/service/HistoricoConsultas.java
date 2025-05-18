package br.edu.infnet.java.service;

import br.edu.infnet.java.model.Consulta;

import java.util.List;

public interface HistoricoConsultas {
    void salvarConsulta(Consulta consulta);
    List<Consulta> listarConsultas();
}
// Interface para armazenar e consultar histórico de consultas
