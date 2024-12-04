package models;

import java.util.ArrayList;

public class Empresa {
    private String razaoSocial;
    private String cnpj;
    private String creci;
    private Float comissao;
    private Responsavel responsavel;
    private ArrayList<Empreendimento> empreendimentos;

    public Empresa() {}

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getCreci() {
        return creci;
    }

    public void setCreci(String creci) {
        this.creci = creci;
    }

    public Float getComissao() {
        return comissao;
    }

    public void setComissao(Float comissao) {
        this.comissao = comissao;
    }

    public Responsavel getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Responsavel responsavel) {
        this.responsavel = responsavel;
    }

    public ArrayList<Empreendimento> getEmpreendimentos() {
        return empreendimentos;
    }

    public void setEmpreendimentos(ArrayList<Empreendimento> empreendimentos) {
        this.empreendimentos = empreendimentos;
    }
}
