package model;

public class Curso {
    private int codigo;
    private String nome;
    private AreaConhecimento areaConhecimento;

    public Curso(int codigo, String nome, String areaConhecimento) {
        this.codigo = codigo;
        this.nome = nome;
        this.areaConhecimento = AreaConhecimento.valueOf(areaConhecimento);
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public AreaConhecimento getAreaConhecimento() {
        return areaConhecimento;
    }
    public void setAreaConhecimento(AreaConhecimento areaConhecimento) {
        this.areaConhecimento = areaConhecimento;
    }
}
