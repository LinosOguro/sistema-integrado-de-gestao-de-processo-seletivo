package controller;

import br.elinos.FilaGenerica.Fila;
import br.elinos.ListaGenerica.Lista;
import br.elinos.sort.Ordenar;
import model.Entidades.Disciplina;
import model.Entidades.Inscricao;
import model.Entidades.Professor;
import model.Entidades.Processo;

/**
 * Controller para consultas de inscritos em disciplinas.
 * Responsabilidades: Buscar inscritos ordenados e listar disciplinas com processos ativos.
 */
@SuppressWarnings("unused")
public class ConsultaInscritosController {

    private final ProfessorController professorController = new ProfessorController();
    private final InscricaoController inscricaoController = new InscricaoController();
    private final DisciplinaController disciplinaController = new DisciplinaController();
    private final ProcessoController processoController = new ProcessoController();

    /**
     * Busca professores inscritos em uma disciplina e os ordena por pontuação (decrescente)
     * e retorna a lista de professores inscritos ordenados por pontuação
     */
    public Lista<Professor> buscarInscritosOrdenados(String codigoDisciplina) throws Exception {
        Lista<Professor> inscritos = new Lista<>();
        for (Inscricao inscricao : inscricaoController.listarTodos()) {
            if (!inscricao.getCodigoDisciplina().equals(codigoDisciplina)) {
                continue;
            }
            if (!processoController.isProcessoAtivo(inscricao.getCodigoProcesso())) {
                continue;
            }
            Professor professor = professorController.buscarProfessor(inscricao.getCpfProfessor());
            if (professor != null) {
                inscritos.addLast(professor);
            }
        }

        return ordenarPorPontosDecrescente(inscritos);
    }

    /**
     * Lista disciplinas que possuem processos seletivos ativos ou abertos
     * e retorna Fila com disciplinas com processos ativos
     */
    public Fila<Disciplina> listarDisciplinasComProcessosAtivos() throws Exception {
        Fila<Disciplina> fila = new Fila<>();
        for (Processo processo : processoController.listarProcessosAtivos()) {
            Disciplina disciplina = disciplinaController.buscarDisciplina(processo.getCodigoDisciplina());
            if (disciplina != null) {
                fila.insert(disciplina);
            }
        }
        return fila;
    }

    /**
     * Ordena professores por pontuação em ordem decrescente usando bubble sort
     * e retorna uma lista ordenada por pontuação
     */
    private Lista<Professor> ordenarPorPontosDecrescente(Lista<Professor> professores) {
        if (professores.isEmpty()) {
            return professores;
        }
        int n = professores.size();
        int[] pontos = new int[n];
        boolean[] usado = new boolean[n];
        for (int i = 0; i < n; i++) {
            try {
                pontos[i] = Integer.parseInt(professores.get(i).getPontos());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        Ordenar ordenar = new Ordenar();
        int[] ordenado = ordenar.bubbleSort(pontos.clone());
        Lista<Professor> resultado = new Lista<>();
        for (int i = n - 1; i >= 0; i--) {
            int valor = ordenado[i];
            for (int j = 0; j < n; j++) {
                if (!usado[j] && pontos[j] == valor) {
                    try {
                        resultado.addLast(professores.get(j));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    usado[j] = true;
                    break;
                }
            }
        }
        return resultado;
    }
}
