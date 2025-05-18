package br.edu.infnet.java.service;

import br.edu.infnet.java.model.Consulta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class HistoricoConsultasFake implements HistoricoConsultas {
    private final List<Consulta> consultas = new ArrayList<>();

    @Override
    public void salvarConsulta(Consulta consulta){
        consultas.add(consulta);
    }

    @Override
    public List<Consulta> listarConsultas(){
        return Collections.unmodifiableList(consultas);
    }
}
