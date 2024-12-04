package models;

public class Cliente {
    private String idCliente;
    private Contato contato;
    private String observações;
    private String tipoCliente;

    public Cliente() {}

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public Contato getContato() {
        return contato;
    }

    public void setContato(Contato contato) {
        this.contato = contato;
    }

    public String getObservações() {
        return observações;
    }

    public void setObservações(String observações) {
        this.observações = observações;
    }

    public String getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }
}