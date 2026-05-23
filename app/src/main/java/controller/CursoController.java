package controller;

import br.elinos.FilaGenerica.Fila;
import br.elinos.ListaGenerica.Lista;
import controller.CSVHandler;
import model.Entidades.Curso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller para gerência de Cursos.
 * Responsabilidades: CRUD de cursos, persistência em CSV e consultas.
 */
@SuppressWarnings("unused")
public class CursoController {

    private static final String CSV_FILE = "cursos.csv";
    private static final String CSV_HEADER = "codigo,nome,area";

    // Lista genérica para armazenar cursos em memória
    private final Lista<Curso> lista = new Lista<>();

    /**
     * Construtor que carrega cursos do arquivo CSV
     */
    public CursoController() {
        try {
            CSVHandler.ensureFileExists(CSV_FILE, CSV_HEADER);
            carregarDoCSV();
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível carregar cursos: " + e.getMessage(), e);
        }
    }

    /**
     * Carrega todos os cursos do arquivo CSV
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
                    lista.addLast(new Curso(dados[0].trim(), dados[1].trim(), dados[2].trim()));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler cursos: " + e.getMessage(), e);
        }
    }

    /**
     * Cadastra um novo curso
     */
    public void cadastrarCurso(String codigo, String nome, String area) throws Exception {
        if (buscarCurso(codigo) != null) {
            throw new Exception("Código de curso já existe");
        }
        lista.addLast(new Curso(codigo, nome, area));
        salvarNoCSV();
    }

    /**
     * Busca um curso pelo código
     */
    public Curso buscarCurso(String codigo) {
        try {
            for (int i = 0; i < lista.size(); i++) {
                Curso curso = lista.get(i);
                if (curso.getCodigo().equals(codigo)) {
                    return curso;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Lista todos os cursos em uma fila
     */
    public Fila<Curso> listarCursosEmFila() throws Exception {
        Fila<Curso> fila = new Fila<>();
        for (int i = 0; i < lista.size(); i++) {
            fila.insert(lista.get(i));
        }
        return fila;
    }

    /**
     * Atualiza os dados de um curso existente
     */
    public void atualizarCurso(String codigo, String nome, String area) throws Exception {
        Curso curso = buscarCurso(codigo);
        if (curso == null) {
            throw new Exception("Curso não encontrado");
        }
        curso.setNome(nome);
        curso.setArea(area);
        salvarNoCSV();
    }

    /**
     * Remove um curso da lista
     */
    public void excluirCurso(String codigo) throws Exception {
        for (int i = 0; i < lista.size(); i++) {
            Curso curso = lista.get(i);
            if (curso.getCodigo().equals(codigo)) {
                lista.remove(i);
                salvarNoCSV();
                return;
            }
        }
        throw new Exception("Curso não encontrado");
    }

    /**
     * Salva todos os cursos no arquivo CSV
     */
    private void salvarNoCSV() {
        try {
            List<String> lines = new ArrayList<>();
            for (int i = 0; i < lista.size(); i++) {
                try {
                    Curso curso = lista.get(i);
                    lines.add(String.join(",", curso.getCodigo(), curso.getNome(), curso.getArea()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            CSVHandler.writeAll(CSV_FILE, lines, CSV_HEADER);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível salvar cursos: " + e.getMessage(), e);
        }
    }
}
