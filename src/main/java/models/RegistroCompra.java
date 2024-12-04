package models;

public class RegistroCompra {
    private Cliente cliente;
    private String cpf;
    private String rg;
    private String estadoCivil;
    private String regimeEstadoCivil;
    private String condicoesPagamento;

    public RegistroCompra() {}

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getRegimeEstadoCivil() {
        return regimeEstadoCivil;
    }

    public void setRegimeEstadoCivil(String regimeEstadoCivil) {
        this.regimeEstadoCivil = regimeEstadoCivil;
    }

    public String getCondicoesPagamento() {
        return condicoesPagamento;
    }

    public void setCondicoesPagamento(String condicoesPagamento) {
        this.condicoesPagamento = condicoesPagamento;
    }
}
