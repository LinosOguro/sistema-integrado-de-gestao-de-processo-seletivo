package controller;

import br.elinos.FilaGenerica.Fila;
import br.elinos.ListaGenerica.Lista;
import controller.CSVHandler;
import model.Entidades.Inscricao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller para gerência de Inscrições.
 * Responsabilidades: CRUD de inscrições, validações e persistência em CSV.
 */
@SuppressWarnings("unused")
public class InscricaoController {

    private static final String CSV_FILE = "inscricoes.csv";
    private static final String CSV_HEADER = "cpfProfessor,codigoDisciplina,codigoProcesso";

    // Lista genérica para armazenar inscrições em memória
    private final Lista<Inscricao> lista = new Lista<>();

    /**
     * Construtor que carrega inscrições do arquivo CSV
     */
    public InscricaoController() {
        try {
            CSVHandler.ensureFileExists(CSV_FILE, CSV_HEADER);
            carregarDoCSV();
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível carregar inscrições: " + e.getMessage(), e);
        }
    }

    /**
     * Carrega todas as inscrições do arquivo CSV
     */
    private void carregarDoCSV() {
        try {
            lista.clean();
            for (String linha : CSVHandler.readAllWithoutHeader(CSV_FILE)) {
                if (linha.isBlank()) {
                    continue;
                }
                String[] dados = linha.split(",", -1);
                if (dados.length == 3) {
                    lista.addLast(new Inscricao(dados[0].trim(), dados[1].trim(), dados[2].trim()));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler inscrições: " + e.getMessage(), e);
        }
    }

    /**
     * Inscreve um professor em um processo seletivo
     * Validações: Existência de professor, disciplina e processo ativo
     */
    public void inscreverProfessor(String cpfProfessor, String codigoDisciplina, String codigoProcesso) throws Exception {
        ProfessorController professorController = new ProfessorController();
        DisciplinaController disciplinaController = new DisciplinaController();
        ProcessoController processoController = new ProcessoController();

        if (professorController.buscarProfessor(cpfProfessor) == null) {
            throw new Exception("Professor não encontrado");
        }
        if (disciplinaController.buscarDisciplina(codigoDisciplina) == null) {
            throw new Exception("Disciplina não encontrada");
        }
        if (!processoController.isProcessoAtivo(codigoProcesso)) {
            throw new Exception("Processo seletivo não está ativo");
        }
        if (!processoController.buscarProcesso(codigoProcesso).getCodigoDisciplina().equals(codigoDisciplina)) {
            throw new Exception("Disciplina não corresponde ao processo informado");
        }
        if (buscarInscricao(cpfProfessor, codigoDisciplina) != null) {
            throw new Exception("Professor já inscrito nesta disciplina");
        }
        lista.addLast(new Inscricao(cpfProfessor, codigoDisciplina, codigoProcesso));
        salvarNoCSV();
    }

    /**
     * Busca uma inscrição pelo CPF do professor e código da disciplina
     */
    public Inscricao buscarInscricao(String cpfProfessor, String codigoDisciplina) {
        try {
            for (int i = 0; i < lista.size(); i++) {
                Inscricao inscricao = lista.get(i);
                if (inscricao.getCpfProfessor().equals(cpfProfessor) && inscricao.getCodigoDisciplina().equals(codigoDisciplina)) {
                    return inscricao;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Atualiza um processo seletivo de uma inscrição existente
     */
    public void atualizarInscricao(String cpfProfessor, String codigoDisciplina, String novoCodigoProcesso) throws Exception {
        Inscricao inscricao = buscarInscricao(cpfProfessor, codigoDisciplina);
        if (inscricao == null) {
            throw new Exception("Inscrição não encontrada");
        }
        ProcessoController processoController = new ProcessoController();
        if (!processoController.isProcessoAtivo(novoCodigoProcesso)) {
            throw new Exception("Processo seletivo não está ativo");
        }
        inscricao.setCodigoProcesso(novoCodigoProcesso);
        salvarNoCSV();
    }

    /**
     * Remove uma inscrição de um professor em uma disciplina
     */
    public void removerInscricao(String cpfProfessor, String codigoDisciplina) throws Exception {
        for (int i = 0; i < lista.size(); i++) {
            Inscricao inscricao = lista.get(i);
            if (inscricao.getCpfProfessor().equals(cpfProfessor) && inscricao.getCodigoDisciplina().equals(codigoDisciplina)) {
                lista.remove(i);
                salvarNoCSV();
                return;
            }
        }
        throw new Exception("Inscrição não encontrada");
    }

    public void removerInscricoesPorDisciplina(String codigoDisciplina) throws Exception {
        boolean alterou = false;
        for (int i = 0; i < lista.size(); i++) {
            Inscricao inscricao = lista.get(i);
            if (inscricao.getCodigoDisciplina().equals(codigoDisciplina)) {
                lista.remove(i);
                i--;
                alterou = true;
            }
        }
        if (alterou) {
            salvarNoCSV();
        }
    }

    public Fila<Inscricao> listarInscricoesEmFila() throws Exception {
        Fila<Inscricao> fila = new Fila<>();
        for (int i = 0; i < lista.size(); i++) {
            try {
                fila.insert(lista.get(i));
            } catch (Exception e) {
                throw new RuntimeException("Erro ao acessar lista de inscrições: " + e.getMessage(), e);
            }
        }
        return fila;
    }

    public List<Inscricao> listarTodos() {
        try {
            List<Inscricao> resultado = new ArrayList<>();
            for (int i = 0; i < lista.size(); i++) {
                resultado.add(lista.get(i));
            }
            return resultado;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void salvarNoCSV() {
        try {
            List<String> linhas = new ArrayList<>();
            for (int i = 0; i < lista.size(); i++) {
                try {
                    Inscricao inscricao = lista.get(i);
                    linhas.add(String.join(",",
                        inscricao.getCpfProfessor(),
                        inscricao.getCodigoDisciplina(),
                        inscricao.getCodigoProcesso()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            CSVHandler.writeAll(CSV_FILE, linhas, CSV_HEADER);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível salvar inscrições: " + e.getMessage(), e);
        }
    }
}
