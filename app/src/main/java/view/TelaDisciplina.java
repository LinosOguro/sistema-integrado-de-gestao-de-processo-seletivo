package view;

import br.elinos.FilaGenerica.Fila;
import controller.DisciplinaController;
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
import model.Entidades.Disciplina;

/**
 * Tela de CRUD para gerenciamento de Disciplinas.
 * Funcionalidades: Cadastro, busca, edição e exclusão de disciplinas.
 */
public class TelaDisciplina {

    private final DisciplinaController controller = new DisciplinaController();

    //TextFields
    private TextField txtDisciplinaNome = new TextField();
    private TextField txtDisciplinaCodigo = new TextField();
    private TextField txtDisciplinaCodigoCurso = new TextField();
    
    //ComboBoxes
    private ComboBox<String> comboDisciplinaDia = new ComboBox<>();
    private ComboBox<String> comboDisciplinaHorario = new ComboBox<>();
    private ComboBox<String> comboDisciplinaAulaDiaria = new ComboBox<>();
    private ComboBox<String> comboStatusDisciplina = new ComboBox<>();
    
    //TableViews
    private TableView<Disciplina> tabelaDisciplina;

    /**
     * Exibe a tela de gerenciamento de disciplinas
     */
    @SuppressWarnings("unchecked")
    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Disciplina");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        //Labels e entrada de dados
        grid.add(new Label("Código da disciplina:"), 0, 0);
        grid.add(txtDisciplinaCodigo, 1, 0);
        Button btnBuscarDisciplina = new Button("🔎");
        grid.add(btnBuscarDisciplina, 2, 0);

        grid.add(new Label("Nome:"), 0, 1);
        grid.add(txtDisciplinaNome, 1, 1);

        grid.add(new Label("Dia da disciplina:"), 0, 2);
        comboDisciplinaDia.getItems().addAll("Segunda-feira", "Terça-feira", "Quarta-feira", "Quinta-feira", "Sexta-feira", "Sábado");
        grid.add(comboDisciplinaDia, 1, 2);

        grid.add(new Label("Horário:"), 0, 3);
        comboDisciplinaHorario.getItems().addAll("08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "00:00");
        grid.add(comboDisciplinaHorario, 1, 3);

        grid.add(new Label("Aula diária:"), 3, 0);
        comboDisciplinaAulaDiaria.getItems().addAll("2", "4");
        grid.add(comboDisciplinaAulaDiaria, 4, 0);

        grid.add(new Label("Código do curso:"), 3, 1);
        grid.add(txtDisciplinaCodigoCurso, 4, 1);

        grid.add(new Label("Status:"), 3, 2);
        comboStatusDisciplina.getItems().addAll("Ativa", "Inativa");
        grid.add(comboStatusDisciplina, 4, 2);

        //Botões
        HBox buttonsDisciplina = new HBox(10);
        Button btnCadastrarDisciplina = new Button("Cadastrar");
        Button btnLimparDisciplina = new Button("Limpar tela");
        Button btnEditarDisciplina = new Button("Editar");
        Button btnExcluirDisciplina = new Button("Excluir");
        buttonsDisciplina.getChildren().addAll(btnCadastrarDisciplina, btnLimparDisciplina, btnEditarDisciplina, btnExcluirDisciplina);

        btnCadastrarDisciplina.setOnAction(e -> {
            if (validarCampos()) {
                try {
                    controller.cadastrarDisciplina(
                            txtDisciplinaCodigo.getText().trim(),
                            txtDisciplinaNome.getText().trim(),
                            comboDisciplinaDia.getValue(),
                            comboDisciplinaHorario.getValue(),
                            comboDisciplinaAulaDiaria.getValue(),
                            txtDisciplinaCodigoCurso.getText().trim(),
                            comboStatusDisciplina.getValue());
                    carregarTabela();
                    limparCampos();
                    mostrarAviso("Disciplina cadastrada com sucesso");
                } catch (Exception ex) {
                    mostrarErro(ex.getMessage());
                }
            }
        });

        btnLimparDisciplina.setOnAction(e -> limparCampos());

        btnEditarDisciplina.setOnAction(e -> {
            Disciplina selecionada = tabelaDisciplina.getSelectionModel().getSelectedItem();
            if (selecionada != null) {
                try {
                    controller.atualizarDisciplina(
                            selecionada.getCodigo(),
                            txtDisciplinaNome.getText().trim(),
                            comboDisciplinaDia.getValue(),
                            comboDisciplinaHorario.getValue(),
                            comboDisciplinaAulaDiaria.getValue(),
                            txtDisciplinaCodigoCurso.getText().trim(),
                            comboStatusDisciplina.getValue());
                    carregarTabela();
                    mostrarAviso("Disciplina atualizada com sucesso");
                } catch (Exception ex) {
                    mostrarErro(ex.getMessage());
                }
            } else {
                mostrarAviso("Selecione uma disciplina para editar");
            }
        });

        btnExcluirDisciplina.setOnAction(e -> {
            Disciplina selecionada = tabelaDisciplina.getSelectionModel().getSelectedItem();
            if (selecionada != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Deseja realmente excluir a disciplina selecionada?", ButtonType.YES, ButtonType.NO);
                confirm.setTitle("Confirmação");
                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.YES) {
                        try {
                            controller.excluirDisciplina(selecionada.getCodigo());
                            carregarTabela();
                            limparCampos();
                            mostrarAviso("Disciplina excluída com sucesso");
                        } catch (Exception ex) {
                            mostrarErro(ex.getMessage());
                        }
                    }
                });
            } else {
                mostrarAviso("Selecione uma disciplina para excluir");
            }
        });

        tabelaDisciplina = new TableView<>();
        TableColumn<Disciplina, String> colCod = new TableColumn<>("Código");
        colCod.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        TableColumn<Disciplina, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        TableColumn<Disciplina, String> colDia = new TableColumn<>("Dia");
        colDia.setCellValueFactory(new PropertyValueFactory<>("dia"));
        TableColumn<Disciplina, String> colHorario = new TableColumn<>("Horário");
        colHorario.setCellValueFactory(new PropertyValueFactory<>("horario"));
        TableColumn<Disciplina, String> colAulaDiaria = new TableColumn<>("Aula diária");
        colAulaDiaria.setCellValueFactory(new PropertyValueFactory<>("aulaDiaria"));
        TableColumn<Disciplina, String> colCodCurso = new TableColumn<>("Código do curso");
        colCodCurso.setCellValueFactory(new PropertyValueFactory<>("codigoCurso"));
        TableColumn<Disciplina, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        tabelaDisciplina.getColumns().addAll(colCod, colNome, colDia, colHorario, colAulaDiaria, colCodCurso, colStatus);
        tabelaDisciplina.setPrefHeight(200);

        tabelaDisciplina.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtDisciplinaCodigo.setText(newVal.getCodigo());
                txtDisciplinaNome.setText(newVal.getNome());
                comboDisciplinaDia.setValue(newVal.getDia());
                comboDisciplinaHorario.setValue(newVal.getHorario());
                comboDisciplinaAulaDiaria.setValue(newVal.getAulaDiaria());
                txtDisciplinaCodigoCurso.setText(newVal.getCodigoCurso());
                comboStatusDisciplina.setValue(newVal.getStatus());
            }
        });

        btnBuscarDisciplina.setOnAction(e -> {
            String codigo = txtDisciplinaCodigo.getText().trim();
            if (codigo.isEmpty()) {
                mostrarAviso("Informe o código da disciplina para buscar");
                return;
            }
            Disciplina result = controller.buscarDisciplina(codigo);
            if (result != null) {
                txtDisciplinaNome.setText(result.getNome());
                comboDisciplinaDia.setValue(result.getDia());
                comboDisciplinaHorario.setValue(result.getHorario());
                comboDisciplinaAulaDiaria.setValue(result.getAulaDiaria());
                txtDisciplinaCodigoCurso.setText(result.getCodigoCurso());
                comboStatusDisciplina.setValue(result.getStatus());
            } else {
                mostrarAviso("Disciplina não encontrada");
            }
        });

        vbox.getChildren().addAll(grid, buttonsDisciplina, tabelaDisciplina);

        Scene scene = new Scene(vbox, 900, 600);
        stage.setScene(scene);
        stage.show();

        // Carrega os dados da tabela ao abrir a tela
        carregarTabela();
    }

    /**
     * Carrega todas as disciplinas da base de dados e exibe na tabela
     */
    private void carregarTabela() {
        try {
            Fila<Disciplina> fila = controller.listarDisciplinasEmFila();
            ObservableList<Disciplina> data = FXCollections.observableArrayList();
            while (!fila.isEmpty()) {
                data.add(fila.remove());
            }
            tabelaDisciplina.setItems(data);
        } catch (Exception e) {
            mostrarErro(e.getMessage());
        }
    }

    /**
     * Limpa todos os campos de entrada da tela
     */
    private void limparCampos() {
        txtDisciplinaCodigo.clear();
        txtDisciplinaNome.clear();
        txtDisciplinaCodigoCurso.clear();
        comboDisciplinaDia.setValue(null);
        comboDisciplinaHorario.setValue(null);
        comboDisciplinaAulaDiaria.setValue(null);
        comboStatusDisciplina.setValue(null);
    }

    /**
     * Valida se todos os campos obrigatórios estão preenchidos
     * e retorna true se todos os campos são válidos, false caso contrário
     */
    private boolean validarCampos() {
        if (txtDisciplinaCodigo.getText().trim().isEmpty()) {
            mostrarAviso("Código é obrigatório");
            return false;
        }
        if (txtDisciplinaNome.getText().trim().isEmpty()) {
            mostrarAviso("Nome é obrigatório");
            return false;
        }
        if (comboDisciplinaDia.getValue() == null || comboDisciplinaHorario.getValue() == null || comboDisciplinaAulaDiaria.getValue() == null || comboStatusDisciplina.getValue() == null) {
            mostrarAviso("Preencha todos os campos de seleção");
            return false;
        }
        return true;
    }

    /**
     * Exibe uma caixa de diálogo com aviso/informação
     */
    private void mostrarAviso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    /**
     * Exibe uma caixa de diálogo com mensagem de erro
     */
    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
