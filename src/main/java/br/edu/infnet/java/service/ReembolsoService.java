package br.edu.infnet.java.service;

//Classe responsável por calcular o valor do reembolso com base no valor da consulta e no percentual de cobertura.
public class ReembolsoService {

    public static double calcularReembolso(double valorConsulta, double percentualCobertura) {
        return valorConsulta * percentualCobertura;
    }
}