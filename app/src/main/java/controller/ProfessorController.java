package controller;

import br.elinos.FilaGenerica.Fila;
import br.elinos.ListaGenerica.Lista;
import controller.CSVHandler;
import model.Entidades.Professor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller para gerência de Professores.
 * Responsabilidades: CRUD de professores, persistência em CSV e consultas.
 */
@SuppressWarnings("unused")
public class ProfessorController {

    private static final String CSV_FILE = "professor.csv";
    private static final String CSV_HEADER = "cpf,nome,area,pontos";

    // Lista genérica para armazenar professores em memória
    private final Lista<Professor> lista = new Lista<>();

    /**
     * Construtor que carrega professores do arquivo CSV
     */
    public ProfessorController() {
        try {
            CSVHandler.ensureFileExists(CSV_FILE, CSV_HEADER);
            carregarDoCSV();
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível carregar professores: " + e.getMessage(), e);
        }
    }

    /**
     * Carrega todos os professores do arquivo CSV
     */
    private void carregarDoCSV() {
        try {
            lista.clean();
            for (String linha : CSVHandler.readAllWithoutHeader(CSV_FILE)) {
                if (linha.isBlank()) {
                    continue;
                }
                String[] dados = linha.split(",", -1);
                if (dados.length == 4) {
                    lista.addLast(new Professor(dados[0].trim(), dados[1].trim(), dados[2].trim(), dados[3].trim()));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler professores: " + e.getMessage(), e);
        }
    }

    /**
     * Cadastra um novo professor
     */
    public void cadastrarProfessor(String cpf, String nome, String area, String pontos) throws Exception {
        if (buscarProfessor(cpf) != null) {
            throw new Exception("CPF já cadastrado");
        }
        lista.addLast(new Professor(cpf, nome, area, pontos));
        salvarNoCSV();
    }

    /**
     * Busca um professor pelo CPF
     */
    public Professor buscarProfessor(String cpf) {
        try {
            for (int i = 0; i < lista.size(); i++) {
                Professor professor = lista.get(i);
                if (professor.getCpf().equals(cpf)) {
                    return professor;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Lista todos os professores em uma fila
     * e retorna uma Fila contendo todos os professores
     */
    public Fila<Professor> listarProfessoresEmFila() throws Exception {
        Fila<Professor> fila = new Fila<>();
        for (int i = 0; i < lista.size(); i++) {
            fila.insert(lista.get(i));
        }
        return fila;
    }

    /**
     * Atualiza os dados de um professor existente
     */
    public void atualizarProfessor(String cpf, String nome, String area, String pontos) throws Exception {
        Professor professor = buscarProfessor(cpf);
        if (professor == null) {
            throw new Exception("Professor não encontrado");
        }
        professor.setNome(nome);
        professor.setArea(area);
        professor.setPontos(pontos);
        salvarNoCSV();
    }

    /**
     * Remove um professor da lista
     */
    public void excluirProfessor(String cpf) throws Exception {
        for (int i = 0; i < lista.size(); i++) {
            Professor professor = lista.get(i);
            if (professor.getCpf().equals(cpf)) {
                lista.remove(i);
                salvarNoCSV();
                return;
            }
        }
        throw new Exception("Professor não encontrado");
    }

    /**
     * Retorna lista de todos os professores
     * e retorna uma lista com todos os professores
     */
    public List<Professor> listarTodos() {
        try {
            List<Professor> resultado = new ArrayList<>();
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
            List<String> lines = new ArrayList<>();
            for (int i = 0; i < lista.size(); i++) {
                Professor p = lista.get(i);
                lines.add(String.join(",", p.getCpf(), p.getNome(), p.getArea(), String.valueOf(p.getPontos())));
            }
            CSVHandler.writeAll(CSV_FILE, lines, CSV_HEADER);
        } catch (IOException | RuntimeException e) {
            throw new RuntimeException("Não foi possível salvar professores: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao acessar lista de professores: " + e.getMessage(), e);
        }
    }
}
