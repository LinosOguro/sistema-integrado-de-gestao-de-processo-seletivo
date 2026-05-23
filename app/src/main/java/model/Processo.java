package model;

public class Processo {
    private String codigoProcesso;
    private String codigoDisciplina;
    private boolean ativo;

    public Processo(String codigoProcesso, String codigoDisciplina, boolean ativo) {
        this.codigoProcesso = codigoProcesso;
        this.codigoDisciplina = codigoDisciplina;
        this.ativo = ativo;
    }

    public String getCodigoProcesso() {
        return codigoProcesso;
    }

    public void setCodigoProcesso(String codigoProcesso) {
        this.codigoProcesso = codigoProcesso;
    }

    public String getCodigoDisciplina() {
        return codigoDisciplina;
    }

    public void setCodigoDisciplina(String codigoDisciplina) {
        this.codigoDisciplina = codigoDisciplina;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
