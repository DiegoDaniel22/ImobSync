package models;

public class Empreendimento {
    private String matricula;
    private String tipoEmpreendimento;
    private Empresa empresaResponsavel;
    private Float comissaoMaxima;
    private String descricao;
    private Float valor;
    private String pontoVenda;
    private Float margemNegociacao;
    private String projetoUrbanistico;
    private RegistroCompra registroCompra;

    public Empreendimento() {}

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getTipoEmpreendimento() {
        return tipoEmpreendimento;
    }

    public void setTipoEmpreendimento(String tipoEmpreendimento) {
        this.tipoEmpreendimento = tipoEmpreendimento;
    }

    public Empresa getEmpresaResponsavel() {
        return empresaResponsavel;
    }

    public void setEmpresaResponsavel(Empresa empresaResponsavel) {
        this.empresaResponsavel = empresaResponsavel;
    }

    public Float getComissaoMaxima() {
        return comissaoMaxima;
    }

    public void setComissaoMaxima(Float comissaoMaxima) {
        this.comissaoMaxima = comissaoMaxima;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Float getValor() {
        return valor;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

    public String getPontoVenda() {
        return pontoVenda;
    }

    public void setPontoVenda(String pontoVenda) {
        this.pontoVenda = pontoVenda;
    }

    public Float getMargemNegociacao() {
        return margemNegociacao;
    }

    public void setMargemNegociacao(Float margemNegociacao) {
        this.margemNegociacao = margemNegociacao;
    }

    public String getProjetoUrbanistico() {
        return projetoUrbanistico;
    }

    public void setProjetoUrbanistico(String projetoUrbanistico) {
        this.projetoUrbanistico = projetoUrbanistico;
    }

    public RegistroCompra getRegistroCompra() {
        return registroCompra;
    }

    public void setRegistroCompra(RegistroCompra registroCompra) {
        this.registroCompra = registroCompra;
    }
}
