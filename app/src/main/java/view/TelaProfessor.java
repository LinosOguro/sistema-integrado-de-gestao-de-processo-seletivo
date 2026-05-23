package view;

import br.elinos.FilaGenerica.Fila;
import controller.ProfessorController;
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
import model.Entidades.Professor;

/**
 * Tela de CRUD para gerenciamento de Professores.
 * Funcionalidades: Cadastro, busca, edição e exclusão de professores.
 */
public class TelaProfessor {

    private final ProfessorController controller = new ProfessorController();

    //TextFields
    private TextField txtProfessorCPF = new TextField();
    private TextField txtProfessorNome = new TextField();
    private TextField txtProfessorQntdPontos = new TextField();
    
    //ComboBoxes
    private ComboBox<String> comboProfessorArea = new ComboBox<>();
    
    //TableViews
    private TableView<Professor> tabelaProfessor;

    /**
     * Exibe a tela de gerenciamento de professores
     */
    @SuppressWarnings("unchecked")
    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Professor");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        //Labels e entrada de dados
        grid.add(new Label("CPF:"), 0, 0);
        grid.add(txtProfessorCPF, 1, 0);
        Button btnBuscarProfessor = new Button("🔎");
        grid.add(btnBuscarProfessor, 2, 0);

        grid.add(new Label("Nome:"), 0, 1);
        grid.add(txtProfessorNome, 1, 1);

        grid.add(new Label("Área:"), 3, 0);
        comboProfessorArea.getItems().addAll("Matemática", "Português", "História", "Geografia", "Ciências");
        grid.add(comboProfessorArea, 4, 0);

        grid.add(new Label("Quantidade de pontos:"), 3, 1);
        grid.add(txtProfessorQntdPontos, 4, 1);

        //Botões
        HBox buttonsProfessor = new HBox(10);
        Button btnCadastrarProfessor = new Button("Cadastrar");
        Button btnLimparProfessor = new Button("Limpar tela");
        Button btnEditarProfessor = new Button("Editar");
        Button btnExcluirProfessor = new Button("Excluir");
        buttonsProfessor.getChildren().addAll(btnCadastrarProfessor, btnLimparProfessor, btnEditarProfessor, btnExcluirProfessor);

        btnCadastrarProfessor.setOnAction(e -> {
            if (validarCampos()) {
                try {
                    controller.cadastrarProfessor(
                            txtProfessorCPF.getText().trim(),
                            txtProfessorNome.getText().trim(),
                            comboProfessorArea.getValue(),
                            txtProfessorQntdPontos.getText().trim());
                    carregarTabela();
                    limparCampos();
                    mostrarAviso("Professor cadastrado com sucesso");
                } catch (Exception ex) {
                    mostrarErro(ex.getMessage());
                }
            }
        });

        btnLimparProfessor.setOnAction(e -> limparCampos());

        btnEditarProfessor.setOnAction(e -> {
            Professor selecionado = tabelaProfessor.getSelectionModel().getSelectedItem();
            if (selecionado != null) {
                try {
                    controller.atualizarProfessor(
                            selecionado.getCpf(),
                            txtProfessorNome.getText().trim(),
                            comboProfessorArea.getValue(),
                            txtProfessorQntdPontos.getText().trim());
                    carregarTabela();
                    mostrarAviso("Professor atualizado com sucesso");
                } catch (Exception ex) {
                    mostrarErro(ex.getMessage());
                }
            } else {
                mostrarAviso("Selecione um professor para editar");
            }
        });

        btnExcluirProfessor.setOnAction(e -> {
            Professor selecionado = tabelaProfessor.getSelectionModel().getSelectedItem();
            if (selecionado != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Deseja realmente excluir o professor selecionado?", ButtonType.YES, ButtonType.NO);
                confirm.setTitle("Confirmação");
                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.YES) {
                        try {
                            controller.excluirProfessor(selecionado.getCpf());
                            carregarTabela();
                            limparCampos();
                            mostrarAviso("Professor excluído com sucesso");
                        } catch (Exception ex) {
                            mostrarErro(ex.getMessage());
                        }
                    }
                });
            } else {
                mostrarAviso("Selecione um professor para excluir");
            }
        });

        btnBuscarProfessor.setOnAction(e -> {
            String cpf = txtProfessorCPF.getText().trim();
            if (cpf.isEmpty()) {
                mostrarAviso("Informe o CPF para buscar");
                return;
            }
            Professor professor = controller.buscarProfessor(cpf);
            if (professor != null) {
                txtProfessorNome.setText(professor.getNome());
                comboProfessorArea.setValue(professor.getArea());
                txtProfessorQntdPontos.setText(professor.getPontos());
            } else {
                mostrarAviso("Professor não encontrado");
            }
        });

        tabelaProfessor = new TableView<>();
        TableColumn<Professor, String> colCPF = new TableColumn<>("CPF");
        colCPF.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        TableColumn<Professor, String> colNomeProf = new TableColumn<>("Nome");
        colNomeProf.setCellValueFactory(new PropertyValueFactory<>("nome"));
        TableColumn<Professor, String> colArea = new TableColumn<>("Área");
        colArea.setCellValueFactory(new PropertyValueFactory<>("area"));
        TableColumn<Professor, String> colPontos = new TableColumn<>("Pontos");
        colPontos.setCellValueFactory(new PropertyValueFactory<>("pontos"));

        tabelaProfessor.getColumns().addAll(colCPF, colNomeProf, colArea, colPontos);
        tabelaProfessor.setPrefHeight(200);

        tabelaProfessor.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtProfessorCPF.setText(newVal.getCpf());
                txtProfessorNome.setText(newVal.getNome());
                comboProfessorArea.setValue(newVal.getArea());
                txtProfessorQntdPontos.setText(newVal.getPontos());
            }
        });

        vbox.getChildren().addAll(grid, buttonsProfessor, tabelaProfessor);

        Scene scene = new Scene(vbox, 900, 600);
        stage.setScene(scene);
        stage.show();

        // Carrega os dados da tabela ao abrir a tela
        carregarTabela();
    }

    /**
     * Carrega todos os professores da base de dados e exibe na tabela
     */
    private void carregarTabela() {
        try {
            Fila<Professor> fila = controller.listarProfessoresEmFila();
            ObservableList<Professor> data = FXCollections.observableArrayList();
            while (!fila.isEmpty()) {
                data.add(fila.remove());
            }
            tabelaProfessor.setItems(data);
        } catch (Exception e) {
            mostrarErro(e.getMessage());
        }
    }

    /**
     * Limpa todos os campos de entrada da tela
     */
    private void limparCampos() {
        txtProfessorCPF.clear();
        txtProfessorNome.clear();
        comboProfessorArea.setValue(null);
        txtProfessorQntdPontos.clear();
    }

    /**
     * Valida se todos os campos obrigatórios estão preenchidos
     * e retorna true se todos os campos são válidos, false caso contrário
     */
    private boolean validarCampos() {
        if (txtProfessorCPF.getText().trim().isEmpty()) {
            mostrarAviso("CPF é obrigatório");
            return false;
        }
        if (txtProfessorNome.getText().trim().isEmpty()) {
            mostrarAviso("Nome é obrigatório");
            return false;
        }
        if (comboProfessorArea.getValue() == null) {
            mostrarAviso("Área é obrigatória");
            return false;
        }
        if (txtProfessorQntdPontos.getText().trim().isEmpty()) {
            mostrarAviso("Pontos são obrigatórios");
            return false;
        }
        try {
            Integer.parseInt(txtProfessorQntdPontos.getText().trim());
        } catch (NumberFormatException e) {
            mostrarAviso("Quantidade de pontos deve ser numérica");
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
