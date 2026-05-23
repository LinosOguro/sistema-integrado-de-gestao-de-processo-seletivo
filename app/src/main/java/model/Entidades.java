package model;

public class Entidades {

    public static class Disciplina {
        private String codigo, nome, dia, horario, aulaDiaria, codigoCurso, status;

        public Disciplina(String codigo, String nome, String dia, String horario, String aulaDiaria, String codigoCurso, String status) {
            this.codigo = codigo;
            this.nome = nome;
            this.dia = dia;
            this.horario = horario;
            this.aulaDiaria = aulaDiaria;
            this.codigoCurso = codigoCurso;
            this.status = status;
        }

        public String getCodigo() { return codigo; }
        public String getNome() { return nome; }
        public String getDia() { return dia; }
        public String getHorario() { return horario; }
        public String getAulaDiaria() { return aulaDiaria; }
        public String getCodigoCurso() { return codigoCurso; }
        public String getStatus() { return status; }
        public void setNome(String nome) { this.nome = nome; }
        public void setDia(String dia) { this.dia = dia; }
        public void setHorario(String horario) { this.horario = horario; }
        public void setAulaDiaria(String aulaDiaria) { this.aulaDiaria = aulaDiaria; }
        public void setCodigoCurso(String codigoCurso) { this.codigoCurso = codigoCurso; }
        public void setStatus(String status) { this.status = status; }
    }

    public static class Professor {
        private String cpf, nome, area, pontos;

        public Professor(String cpf, String nome, String area, String pontos) {
            this.cpf = cpf;
            this.nome = nome;
            this.area = area;
            this.pontos = pontos;
        }

        public String getCpf() { return cpf; }
        public String getNome() { return nome; }
        public String getArea() { return area; }
        public String getPontos() { return pontos; }
        public void setNome(String nome) { this.nome = nome; }
        public void setArea(String area) { this.area = area; }
        public void setPontos(String pontos) { this.pontos = pontos; }
    }

    public static class Curso {
        private String codigo, nome, area;

        public Curso(String codigo, String nome, String area) {
            this.codigo = codigo;
            this.nome = nome;
            this.area = area;
        }

        public String getCodigo() { return codigo; }
        public String getNome() { return nome; }
        public String getArea() { return area; }
        public void setNome(String nome) { this.nome = nome; }
        public void setArea(String area) { this.area = area; }
    }

    public static class Inscricao {
        private String cpfProfessor, codigoDisciplina, codigoProcesso;

        public Inscricao(String cpfProfessor, String codigoDisciplina, String codigoProcesso) {
            this.cpfProfessor = cpfProfessor;
            this.codigoDisciplina = codigoDisciplina;
            this.codigoProcesso = codigoProcesso;
        }

        public String getCpfProfessor() { return cpfProfessor; }
        public String getCodigoDisciplina() { return codigoDisciplina; }
        public String getCodigoProcesso() { return codigoProcesso; }
        public void setCodigoProcesso(String codigoProcesso) { this.codigoProcesso = codigoProcesso; }
    }

    public static class Processo {
        private String codigo, codigoDisciplina, status;

        public Processo(String codigo, String codigoDisciplina, String status) {
            this.codigo = codigo;
            this.codigoDisciplina = codigoDisciplina;
            this.status = status;
        }

        public String getCodigo() { return codigo; }
        public String getCodigoDisciplina() { return codigoDisciplina; }
        public String getStatus() { return status; }
        public void setCodigoDisciplina(String codigoDisciplina) { this.codigoDisciplina = codigoDisciplina; }
        public void setStatus(String status) { this.status = status; }
    }
}
