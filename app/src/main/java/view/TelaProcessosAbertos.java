package view;

import controller.ProcessosAbertosController;
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

/**
 * Tela de visualização de processos seletivos abertos.
 * Funcionalidade: Listar disciplinas com processos abertos usando Hash Table.
 */
public class TelaProcessosAbertos {

    private final ProcessosAbertosController controller = new ProcessosAbertosController();

    //TableViews
    private TableView<Disciplina> tabelaDisciplinasAbertos;
    
    //Botões
    private Button btnCarregarProcessos;

    /**
     * Exibe a tela de processos abertos
     */
    @SuppressWarnings("unchecked")
    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Disciplinas com Processos Seletivos Abertos");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        HBox headerBox = new HBox(10);
        //Labels
        Label lblTitulo = new Label("Disciplinas com Processos Abertos");
        lblTitulo.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
        btnCarregarProcessos = new Button("🔄 Recarregar");
        headerBox.getChildren().addAll(lblTitulo, btnCarregarProcessos);

        btnCarregarProcessos.setOnAction(e -> carregarProcessos());

        tabelaDisciplinasAbertos = new TableView<>();
        TableColumn<Disciplina, String> colCodigo = new TableColumn<>("Código");
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        TableColumn<Disciplina, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        TableColumn<Disciplina, String> colDia = new TableColumn<>("Dia");
        colDia.setCellValueFactory(new PropertyValueFactory<>("dia"));
        TableColumn<Disciplina, String> colHorario = new TableColumn<>("Horário");
        colHorario.setCellValueFactory(new PropertyValueFactory<>("horario"));
        TableColumn<Disciplina, String> colAulaDiaria = new TableColumn<>("Aula diária");
        colAulaDiaria.setCellValueFactory(new PropertyValueFactory<>("aulaDiaria"));
        TableColumn<Disciplina, String> colCurso = new TableColumn<>("Código Curso");
        colCurso.setCellValueFactory(new PropertyValueFactory<>("codigoCurso"));
        TableColumn<Disciplina, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        tabelaDisciplinasAbertos.getColumns().addAll(colCodigo, colNome, colDia, colHorario, colAulaDiaria, colCurso, colStatus);
        tabelaDisciplinasAbertos.setPrefHeight(400);

        //Teste
        //Label lblInfoHashTable = new Label("Informação: Dados recuperados usando Tabela de Espalhamento (Hash Table) com função hash customizada");
        //lblInfoHashTable.setStyle("-fx-font-size: 10; -fx-text-fill: #666666;");

        vbox.getChildren().addAll(headerBox, new Separator(), tabelaDisciplinasAbertos/*, lblInfoHashTable*/);

        Scene scene = new Scene(vbox, 1000, 600);
        stage.setScene(scene);
        stage.show();

        // Carrega os dados da tabela ao abrir a tela
        carregarProcessos();
    }

    /**
     * Carrega disciplinas com processos abertos e exibe na tabela
     */
    private void carregarProcessos() {
        try {
            java.util.List<Disciplina> disciplinasAbertas = controller.carregarProcessosAbertos();
            ObservableList<Disciplina> data = FXCollections.observableArrayList(disciplinasAbertas);
            tabelaDisciplinasAbertos.setItems(data);
        } catch (Exception e) {
            mostrarErro(e.getMessage());
        }
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
