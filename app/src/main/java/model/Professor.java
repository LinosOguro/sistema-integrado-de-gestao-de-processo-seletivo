package model;

public class Professor {
    private String cpf;
    private String nome;
    private AreaConhecimento areaConhecimento;
    private int pontos;

    public Professor(String cpf, String nome, AreaConhecimento areaConhecimento, int pontos) {
        this.cpf = cpf;
        this.nome = nome;
        this.areaConhecimento = areaConhecimento;
        this.pontos = pontos;
    }

    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
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

    public int getPontos() {
        return pontos;
    }
    public void setPontos(int pontos) {
        this.pontos = pontos;
    }
}
