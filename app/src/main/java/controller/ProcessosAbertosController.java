package controller;

import br.elinos.FilaGenerica.Fila;
import controller.HashTableProcessos;
import model.Entidades.Disciplina;
import model.Entidades.Processo;

import java.util.List;

/**
 * Controller para gerenciamento de processos seletivos abertos.
 * Responsabilidades: Listar disciplinas com processos abertos usando Hash Table.
 */
@SuppressWarnings("unused")
public class ProcessosAbertosController {

    private final ProcessoController processoController = new ProcessoController();
    private final DisciplinaController disciplinaController = new DisciplinaController();

    /**
     * Carrega disciplinas com processos seletivos abertos usando Hash Table
     * retorna uma lista de disciplinas com processos abertos
     */
    public List<Disciplina> carregarProcessosAbertos() throws Exception {
        HashTableProcessos tabela = new HashTableProcessos(17);
        for (Processo processo : processoController.listarProcessosAtivos()) {
            Disciplina disciplina = disciplinaController.buscarDisciplina(processo.getCodigoDisciplina());
            if (disciplina != null) {
                tabela.inserir(processo.getCodigo(), disciplina);
            }
        }
        return tabela.listarTodos();
    }

    /**
     * Retorna estatísticas da Hash Table utilizada
     * e retorna uma String com informações da Hash Table
     */
    public String obterEstatisticasHashTable() {
        HashTableProcessos tabela = new HashTableProcessos(17);
        try {
            for (Processo processo : processoController.listarProcessosAtivos()) {
                tabela.inserir(processo.getCodigo(), disciplinaController.buscarDisciplina(processo.getCodigoDisciplina()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tabela.obterEstatisticas();
    }

    /**
     * Lista disciplinas com processos abertos em uma fila
     * e retorna uma Fila com disciplinas
     */
    public Fila<Disciplina> listarDisciplinasEmFila() throws Exception {
        Fila<Disciplina> fila = new Fila<>();
        for (Disciplina disciplina : carregarProcessosAbertos()) {
            fila.insert(disciplina);
        }
        return fila;
    }
}
