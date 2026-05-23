package model;

public class Disciplina {
    private boolean status;
    private String codigo;
    private String nome;
    private String diaSemana;
    private String horario;
    private int cargaHoraria;
    private int codigoCurso;

    public Disciplina() {
        super();
    }

    public Disciplina(String codigo, String nome, String diaSemana, String horario, int cargaHoraria, int codigoCurso) {
        this.status = true;
        this.codigo = codigo;
        this.nome = nome;
        this.diaSemana = diaSemana;
        this.horario = horario;
        this.cargaHoraria = cargaHoraria;
        this.codigoCurso = codigoCurso;
    }

    public boolean isStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDiaSemana() {
        return diaSemana;
    }
    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getHorario() {
        return horario;
    }
    public void setHorario(String horario) {
        this.horario = horario;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }
    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public int getCodigoCurso() {
        return codigoCurso;
    }
    public void setCodigoCurso(int codigoCurso) {
        this.codigoCurso = codigoCurso;
    }
}
