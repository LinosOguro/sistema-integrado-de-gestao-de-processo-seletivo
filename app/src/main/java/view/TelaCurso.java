package view;

import br.elinos.FilaGenerica.Fila;
import controller.CursoController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Entidades.Curso;

/**
 * Tela de CRUD para gerenciamento de Cursos.
 * Funcionalidades: Cadastro, busca, edição e exclusão de cursos.
 */
public class TelaCurso {

    private final CursoController controller = new CursoController();

    //TextFields
    private TextField txtCursoCodigo = new TextField();
    private TextField txtCursoNome = new TextField();
    
    //ComboBoxes
    private ComboBox<String> comboCursoArea = new ComboBox<>();
    
    //TableViews
    private TableView<Curso> tabelaCurso;

    /**
     * Exibe a tela de gerenciamento de cursos
     */
    @SuppressWarnings("unchecked")
    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Curso");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        //Labels e entrada de dados
        grid.add(new Label("Código do curso:"), 0, 0);
        grid.add(txtCursoCodigo, 1, 0);
        Button btnBuscarCurso = new Button("🔎");
        grid.add(btnBuscarCurso, 2, 0);

        grid.add(new Label("Nome:"), 0, 1);
        grid.add(txtCursoNome, 1, 1);

        grid.add(new Label("Área:"), 3, 0);
        comboCursoArea.getItems().addAll("Matemática", "Português", "História", "Geografia", "Ciências");
        grid.add(comboCursoArea, 4, 0);

        //Botões
        HBox buttonsCurso = new HBox(10);
        Button btnCadastrarCurso = new Button("Cadastrar");
        Button btnLimparCurso = new Button("Limpar tela");
        Button btnEditarCurso = new Button("Editar");
        Button btnExcluirCurso = new Button("Excluir");
        buttonsCurso.getChildren().addAll(btnCadastrarCurso, btnLimparCurso, btnEditarCurso, btnExcluirCurso);

        btnCadastrarCurso.setOnAction(e -> {
            if (validarCampos()) {
                try {
                    controller.cadastrarCurso(
                            txtCursoCodigo.getText().trim(),
                            txtCursoNome.getText().trim(),
                            comboCursoArea.getValue());
                    carregarTabela();
                    limparCampos();
                    mostrarAviso("Curso cadastrado com sucesso");
                } catch (Exception ex) {
                    mostrarErro(ex.getMessage());
                }
            }
        });

        btnLimparCurso.setOnAction(e -> limparCampos());

        btnEditarCurso.setOnAction(e -> {
            Curso selecionado = tabelaCurso.getSelectionModel().getSelectedItem();
            if (selecionado != null) {
                try {
                    controller.atualizarCurso(
                            selecionado.getCodigo(),
                            txtCursoNome.getText().trim(),
                            comboCursoArea.getValue());
                    carregarTabela();
                    mostrarAviso("Curso atualizado com sucesso");
                } catch (Exception ex) {
                    mostrarErro(ex.getMessage());
                }
            } else {
                mostrarAviso("Selecione um curso para editar");
            }
        });

        btnExcluirCurso.setOnAction(e -> {
            Curso selecionado = tabelaCurso.getSelectionModel().getSelectedItem();
            if (selecionado != null) {
                Alert confirmar = new Alert(Alert.AlertType.CONFIRMATION, "Deseja realmente excluir o curso selecionado?", ButtonType.YES, ButtonType.NO);
                confirmar.setTitle("Confirmação");
                confirmar.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.YES) {
                        try {
                            controller.excluirCurso(selecionado.getCodigo());
                            carregarTabela();
                            limparCampos();
                            mostrarAviso("Curso excluído com sucesso");
                        } catch (Exception ex) {
                            mostrarErro(ex.getMessage());
                        }
                    }
                });
            } else {
                mostrarAviso("Selecione um curso para excluir");
            }
        });

        btnBuscarCurso.setOnAction(e -> {
            String codigo = txtCursoCodigo.getText().trim();
            if (codigo.isEmpty()) {
                mostrarAviso("Informe o código do curso para buscar");
                return;
            }
            Curso curso = controller.buscarCurso(codigo);
            if (curso != null) {
                txtCursoNome.setText(curso.getNome());
                comboCursoArea.setValue(curso.getArea());
            } else {
                mostrarAviso("Curso não encontrado");
            }
        });

        tabelaCurso = new TableView<>();
        TableColumn<Curso, String> colCodCurso = new TableColumn<>("Código");
        colCodCurso.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        TableColumn<Curso, String> colNomeCurso = new TableColumn<>("Nome");
        colNomeCurso.setCellValueFactory(new PropertyValueFactory<>("nome"));
        TableColumn<Curso, String> colAreaCurso = new TableColumn<>("Área");
        colAreaCurso.setCellValueFactory(new PropertyValueFactory<>("area"));

        tabelaCurso.getColumns().addAll(colCodCurso, colNomeCurso, colAreaCurso);
        tabelaCurso.setPrefHeight(200);

        tabelaCurso.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtCursoCodigo.setText(newVal.getCodigo());
                txtCursoNome.setText(newVal.getNome());
                comboCursoArea.setValue(newVal.getArea());
            }
        });

        vbox.getChildren().addAll(grid, buttonsCurso, tabelaCurso);

        Scene scene = new Scene(vbox, 900, 600);
        stage.setScene(scene);
        stage.show();

        // Carrega os dados da tabela ao abrir a tela
        carregarTabela();
    }

    /**
     * Carrega todos os cursos da base de dados e exibe na tabela
     */
    private void carregarTabela() {
        try {
            Fila<Curso> fila = controller.listarCursosEmFila();
            ObservableList<Curso> data = FXCollections.observableArrayList();
            while (!fila.isEmpty()) {
                data.add(fila.remove());
            }
            tabelaCurso.setItems(data);
        } catch (Exception e) {
            mostrarErro(e.getMessage());
        }
    }

    /**
     * Limpa todos os campos de entrada da tela
     */
    private void limparCampos() {
        txtCursoCodigo.clear();
        txtCursoNome.clear();
        comboCursoArea.setValue(null);
    }

    /**
     * Valida se todos os campos obrigatórios estão preenchidos
     * e retorna true se todos os campos são válidos, false caso contrário
     */
    private boolean validarCampos() {
        if (txtCursoCodigo.getText().trim().isEmpty()) {
            mostrarAviso("Código é obrigatório");
            return false;
        }
        if (txtCursoNome.getText().trim().isEmpty()) {
            mostrarAviso("Nome é obrigatório");
            return false;
        }
        if (comboCursoArea.getValue() == null) {
            mostrarAviso("Área é obrigatória");
            return false;
        }
        return true;
    }

    /**
     * Exibe uma caixa de diálogo com aviso/informação
     */
    private void mostrarAviso(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
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
