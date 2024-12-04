package models;

public class Responsavel {
    private Contato contato;
    private String cpf;
    private String contratoSocial;

    public Responsavel() {}

    public Contato getContato() {
        return contato;
    }

    public void setContato(Contato contato) {
        this.contato = contato;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getContratoSocial() {
        return contratoSocial;
    }

    public void setContratoSocial(String contratoSocial) {
        this.contratoSocial = contratoSocial;
    }
}
