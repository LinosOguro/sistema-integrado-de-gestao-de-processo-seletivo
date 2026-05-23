package view;

import br.elinos.FilaGenerica.Fila;
import br.elinos.ListaGenerica.Lista;
import controller.ConsultaInscritosController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Entidades.Disciplina;
import model.Entidades.Professor;

/**
 * Tela de consulta de inscritos em disciplinas.
 * Funcionalidade: Visualizar professores inscritos ordenados por pontuação.
 */
public class TelaConsultaInscritos {

    private final ConsultaInscritosController controller = new ConsultaInscritosController();

    //ComboBoxes
    private ComboBox<String> comboSelecDisciplina = new ComboBox<>();
    
    //Botões
    private Button btnBuscarInscritos;
    
    //TableViews
    private TableView<Professor> tabelaInscritos;

    /**
     * Exibe a tela de consulta de inscritos
     */
    @SuppressWarnings("unchecked")
    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Consulta de Inscritos por Disciplina");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        HBox filterBox = new HBox(10);
        filterBox.setPadding(new Insets(5));

        //Labels
        Label lblDisciplina = new Label("Selecione a Disciplina:");
        comboSelecDisciplina.setPrefWidth(300);
        btnBuscarInscritos = new Button("🔍 Buscar Inscritos");
        
        filterBox.getChildren().addAll(lblDisciplina, comboSelecDisciplina, btnBuscarInscritos);

        btnBuscarInscritos.setOnAction(e -> {
            String codigoDisciplina = comboSelecDisciplina.getValue();
            if (codigoDisciplina == null || codigoDisciplina.isEmpty()) {
                mostrarAviso("Selecione uma disciplina");
                return;
            }
            try {
                Lista<Professor> inscritos = controller.buscarInscritosOrdenados(codigoDisciplina);
                ObservableList<Professor> data = FXCollections.observableArrayList();
                for (int i = 0; i < inscritos.size(); i++) {
                    data.add(inscritos.get(i));
                }
                tabelaInscritos.setItems(data);
                mostrarAviso("Total de inscritos: " + inscritos.size());
            } catch (Exception ex) {
                mostrarErro(ex.getMessage());
            }
        });

        tabelaInscritos = new TableView<>();
        TableColumn<Professor, String> colCPF = new TableColumn<>("CPF");
        colCPF.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        TableColumn<Professor, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        TableColumn<Professor, String> colArea = new TableColumn<>("Área");
        colArea.setCellValueFactory(new PropertyValueFactory<>("area"));
        TableColumn<Professor, String> colPontos = new TableColumn<>("Pontuação");
        colPontos.setCellValueFactory(new PropertyValueFactory<>("pontos"));

        tabelaInscritos.getColumns().addAll(colCPF, colNome, colArea, colPontos);
        tabelaInscritos.setPrefHeight(400);

        vbox.getChildren().addAll(filterBox, new Separator(), tabelaInscritos);

        Scene scene = new Scene(vbox, 900, 600);
        stage.setScene(scene);
        stage.show();

        // Carrega as disciplinas no combo box ao abrir a tela
        carregarDisciplinas();
    }

    /**
     * Carrega todas as disciplinas com processos ativos no combo box
     */
    private void carregarDisciplinas() {
        try {
            Fila<Disciplina> filaDisciplinas = controller.listarDisciplinasComProcessosAtivos();
            ObservableList<String> itens = FXCollections.observableArrayList();
            while (!filaDisciplinas.isEmpty()) {
                itens.add(filaDisciplinas.remove().getCodigo());
            }
            comboSelecDisciplina.setItems(itens);
        } catch (Exception e) {
            mostrarErro(e.getMessage());
        }
    }

    /**
     * Exibe uma caixa de diálogo com aviso/informação
     */
    private void mostrarAviso(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setContentText(msg);
        alert.showAndWait();
    }

    /**
     * Exibe uma caixa de diálogo com mensagem de erro
     */
    private void mostrarErro(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
