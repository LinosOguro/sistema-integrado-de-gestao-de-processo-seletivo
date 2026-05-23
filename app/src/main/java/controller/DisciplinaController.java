package controller;

import br.elinos.FilaGenerica.Fila;
import br.elinos.ListaGenerica.Lista;
import controller.CSVHandler;
import model.Entidades.Disciplina;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller para gerência de Disciplinas.
 * Responsabilidades: CRUD de disciplinas, persistência em CSV e consultas.
 */
@SuppressWarnings("unused")
public class DisciplinaController {

    private static final String CSV_FILE = "disciplinas.csv";
    private static final String CSV_HEADER = "codigo,nome,dia,horario,aulaDiaria,codigoCurso,status";

    // Lista genérica para armazenar disciplinas em memória
    private final Lista<Disciplina> lista = new Lista<>();

    /**
     * Construtor que carrega disciplinas do arquivo CSV
     */
    public DisciplinaController() {
        try {
            CSVHandler.ensureFileExists(CSV_FILE, CSV_HEADER);
            carregarDoCSV();
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível carregar disciplinas: " + e.getMessage(), e);
        }
    }

    /**
     * Carrega todas as disciplinas do arquivo CSV
     */
    private void carregarDoCSV() {
        try {
            lista.clean();
            for (String linha : CSVHandler.readAllWithoutHeader(CSV_FILE)) {
                if (linha.isBlank()) {
                    continue;
                }
                String[] dados = linha.split(",", -1);
                if (dados.length == 7) {
                    lista.addLast(new Disciplina(
                            dados[0].trim(),
                            dados[1].trim(),
                            dados[2].trim(),
                            dados[3].trim(),
                            dados[4].trim(),
                            dados[5].trim(),
                            dados[6].trim()));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler disciplinas: " + e.getMessage(), e);
        }
    }

    /**
     * Cadastra uma nova disciplina
     */
    public void cadastrarDisciplina(String codigo, String nome, String dia, String horario, String aulaDiaria, String codigoCurso, String status) throws Exception {
        if (buscarDisciplina(codigo) != null) {
            throw new Exception("Código de disciplina já existe");
        }
        lista.addLast(new Disciplina(codigo, nome, dia, horario, aulaDiaria, codigoCurso, status));
        salvarNoCSV();
    }

    /**
     * Busca uma disciplina pelo código
     * e retorna a disciplina encontrada ou null
     */
    public Disciplina buscarDisciplina(String codigo) {
        try {
            for (int i = 0; i < lista.size(); i++) {
                Disciplina disciplina = lista.get(i);
                if (disciplina.getCodigo().equals(codigo)) {
                    return disciplina;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Lista todas as disciplinas em uma fila
     * e retorna a fila contendo todas as disciplinas
     */
    public Fila<Disciplina> listarDisciplinasEmFila() throws Exception {
        Fila<Disciplina> fila = new Fila<>();
        for (int i = 0; i < lista.size(); i++) {
            fila.insert(lista.get(i));
        }
        return fila;
    }

    /**
     * Atualiza os dados de uma disciplina existente
     * e retorna uma fila contendo todas as disciplinas
     */
    public void atualizarDisciplina(String codigo, String nome, String dia, String horario, String aulaDiaria, String codigoCurso, String status) throws Exception {
        Disciplina disciplina = buscarDisciplina(codigo);
        if (disciplina == null) {
            throw new Exception("Disciplina não encontrada");
        }
        disciplina.setNome(nome);
        disciplina.setDia(dia);
        disciplina.setHorario(horario);
        disciplina.setAulaDiaria(aulaDiaria);
        disciplina.setCodigoCurso(codigoCurso);
        disciplina.setStatus(status);
        salvarNoCSV();
    }

    /**
     * Remove uma disciplina da lista
     */
    public void excluirDisciplina(String codigo) throws Exception {
        for (int i = 0; i < lista.size(); i++) {
            Disciplina disciplina = lista.get(i);
            if (disciplina.getCodigo().equals(codigo)) {
                lista.remove(i);
                salvarNoCSV();
                new InscricaoController().removerInscricoesPorDisciplina(codigo);
                return;
            }
        }
        throw new Exception("Disciplina não encontrada");
    }

    /**
     * Salva todas as disciplinas no arquivo CSV
     */
    private void salvarNoCSV() {
        try {
            List<String> linhas = new ArrayList<>();
            for (int i = 0; i < lista.size(); i++) {
                try {
                    Disciplina d = lista.get(i);
                    linhas.add(String.join(",",
                        d.getCodigo(),
                        d.getNome(),
                        d.getDia(),
                        d.getHorario(),
                        d.getAulaDiaria(),
                        d.getCodigoCurso(),
                        d.getStatus()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            CSVHandler.writeAll(CSV_FILE, linhas, CSV_HEADER);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível salvar disciplinas: " + e.getMessage(), e);
        }
    }
}
