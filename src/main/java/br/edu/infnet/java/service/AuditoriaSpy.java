package br.edu.infnet.java.service;

public class AuditoriaSpy implements Auditoria {
    private boolean chamado = false;

    @Override
    public void registrarConsulta(String descricao) {
        chamado = true;
    }

    public boolean foiChamado() {
        return chamado;
    }
}
