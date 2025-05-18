package br.edu.infnet.java.service;

import br.edu.infnet.java.model.Consulta;

public interface AutorizadorReembolso {
    boolean autorizar(Consulta consulta);

}