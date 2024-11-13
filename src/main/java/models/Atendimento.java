package models;

import java.sql.Time;
import java.util.Date;

public class Atendimento {
    private Cliente cliente;
    private Empresa agenteEmpresa;
    private Funcionario agenteFuncionario;
    private Date dataVisita;
    private Time horarioVisita;
    private String locarEncontro;
    private String situacao;
    private String observacoesAtendimento;
}
