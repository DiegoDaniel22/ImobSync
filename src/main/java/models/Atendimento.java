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

    public Atendimento() {}

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Empresa getAgenteEmpresa() {
        return agenteEmpresa;
    }

    public void setAgenteEmpresa(Empresa agenteEmpresa) {
        this.agenteEmpresa = agenteEmpresa;
    }

    public Funcionario getAgenteFuncionario() {
        return agenteFuncionario;
    }

    public void setAgenteFuncionario(Funcionario agenteFuncionario) {
        this.agenteFuncionario = agenteFuncionario;
    }

    public Date getDataVisita() {
        return dataVisita;
    }

    public void setDataVisita(Date dataVisita) {
        this.dataVisita = dataVisita;
    }

    public Time getHorarioVisita() {
        return horarioVisita;
    }

    public void setHorarioVisita(Time horarioVisita) {
        this.horarioVisita = horarioVisita;
    }

    public String getLocarEncontro() {
        return locarEncontro;
    }

    public void setLocarEncontro(String locarEncontro) {
        this.locarEncontro = locarEncontro;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getObservacoesAtendimento() {
        return observacoesAtendimento;
    }

    public void setObservacoesAtendimento(String observacoesAtendimento) {
        this.observacoesAtendimento = observacoesAtendimento;
    }
}
