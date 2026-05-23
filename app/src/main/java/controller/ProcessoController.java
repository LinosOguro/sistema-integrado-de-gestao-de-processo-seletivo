package controller;

import br.elinos.FilaGenerica.Fila;
import br.elinos.ListaGenerica.Lista;
import controller.CSVHandler;
import model.Entidades.Processo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller para gerência de Processos Seletivos.
 * Responsabilidades: CRUD de processos, verificação de status e persistência em CSV.
 */
@SuppressWarnings("unused")
public class ProcessoController {

    private static final String CSV_FILE = "processos.csv";
    private static final String CSV_HEADER = "codigo,codigoDisciplina,status";

    // Lista genérica para armazenar processos em memória
    private final Lista<Processo> lista = new Lista<>();

    /**
     * Construtor que carrega processos do arquivo CSV
     */
    public ProcessoController() {
        try {
            CSVHandler.ensureFileExists(CSV_FILE, CSV_HEADER);
            carregarDoCSV();
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível carregar processos: " + e.getMessage(), e);
        }
    }

    /**
     * Carrega todos os processos do arquivo CSV
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
                    lista.addLast(new Processo(dados[0].trim(), dados[1].trim(), dados[2].trim()));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler processos: " + e.getMessage(), e);
        }
    }

    /**
     * Busca um processo pelo código
     * e retorna o processo encontrado ou null
     */
    public Processo buscarProcesso(String codigo) {
        try {
            for (int i = 0; i < lista.size(); i++) {
                Processo processo = lista.get(i);
                if (processo.getCodigo().equals(codigo)) {
                    return processo;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Cadastra um novo processo seletivo
     */
    public void cadastrarProcesso(String codigo, String codigoDisciplina, String status) throws Exception {
        if (buscarProcesso(codigo) != null) {
            throw new Exception("Código de processo já existe");
        }
        lista.addLast(new Processo(codigo, codigoDisciplina, status));
        salvarNoCSV();
    }

    /**
     * Atualiza os dados de um processo existente
     */
    public void atualizarProcesso(String codigo, String codigoDisciplina, String status) throws Exception {
        Processo processo = buscarProcesso(codigo);
        if (processo == null) {
            throw new Exception("Processo não encontrado");
        }
        processo.setCodigoDisciplina(codigoDisciplina);
        processo.setStatus(status);
        salvarNoCSV();
    }

    /**
     * Remove um processo da lista
     */
    public void excluirProcesso(String codigo) throws Exception {
        for (int i = 0; i < lista.size(); i++) {
            Processo processo = lista.get(i);
            if (processo.getCodigo().equals(codigo)) {
                lista.remove(i);
                salvarNoCSV();
                return;
            }
        }
        throw new Exception("Processo não encontrado");
    }

    /**
     * Lista todos os processos em uma fila
     */
    public Fila<Processo> listarProcessosEmFila() throws Exception {
        Fila<Processo> fila = new Fila<>();
        for (int i = 0; i < lista.size(); i++) {
            fila.insert(lista.get(i));
        }
        return fila;
    }

    /**
     * Verifica se um processo está ativo ou aberto
     * e retorna true se o processo está ativo ou aberto, false caso contrário
     */
    public boolean isProcessoAtivo(String codigoProcesso) {
        Processo processo = buscarProcesso(codigoProcesso);
        if (processo == null) {
            return false;
        }
        String status = processo.getStatus();
        return "Ativo".equalsIgnoreCase(status) || "Aberto".equalsIgnoreCase(status);
    }

    /**
     * Lista todos os processos ativos ou abertos
     * e retorna uma lista com processos ativos
     */
    public List<Processo> listarProcessosAtivos() {
        try {
            List<Processo> ativos = new ArrayList<>();
            for (int i = 0; i < lista.size(); i++) {
                Processo processo = lista.get(i);
                if (isProcessoAtivo(processo.getCodigo())) {
                    ativos.add(processo);
                }
            }
            return ativos;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Salva todos os processos no arquivo CSV
     */
    private void salvarNoCSV() {
        try {
            List<String> linhas = new ArrayList<>();
            for (int i = 0; i < lista.size(); i++) {
                try {
                    Processo processo = lista.get(i);
                    linhas.add(String.join(",", processo.getCodigo(), processo.getCodigoDisciplina(), processo.getStatus()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            CSVHandler.writeAll(CSV_FILE, linhas, CSV_HEADER);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível salvar processos: " + e.getMessage(), e);
        }
    }
}
