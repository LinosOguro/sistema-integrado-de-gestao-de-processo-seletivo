package view;

import br.elinos.FilaGenerica.Fila;
import controller.ProcessoController;
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
import model.Entidades.Processo;

/**
 * Tela de CRUD para gerenciamento de Processos Seletivos.
 * Funcionalidades: Cadastro, busca, edição e exclusão de processos.
 */
public class TelaProcesso {

    private final ProcessoController controller = new ProcessoController();

    //TextFields
    private TextField txtCodigoProcesso = new TextField();
    private TextField txtCodigoDisciplina = new TextField();
    
    //ComboBoxes
    private ComboBox<String> comboStatus = new ComboBox<>();
    
    //TableViews
    private TableView<Processo> tabelaProcessos;

    /**
     * Exibe a tela de gerenciamento de processos seletivos
     */
    @SuppressWarnings("unchecked")
    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Cadastro de Processo Seletivo");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        //Labels e entrada de dados
        grid.add(new Label("Código do Processo:"), 0, 0);
        grid.add(txtCodigoProcesso, 1, 0);
        Button btnBuscarProcesso = new Button("🔎");
        grid.add(btnBuscarProcesso, 2, 0);

        grid.add(new Label("Código da Disciplina:"), 0, 1);
        grid.add(txtCodigoDisciplina, 1, 1);

        grid.add(new Label("Status:"), 0, 2);
        comboStatus.getItems().addAll("Aberto", "Fechado");
        grid.add(comboStatus, 1, 2);

        //Botões
        HBox buttons = new HBox(10);
        Button btnCadastrar = new Button("Cadastrar");
        Button btnEditar = new Button("Editar");
        Button btnExcluir = new Button("Excluir");
        Button btnLimpar = new Button("Limpar tela");
        buttons.getChildren().addAll(btnCadastrar, btnEditar, btnExcluir, btnLimpar);

        btnCadastrar.setOnAction(e -> {
            if (validarCampos()) {
                try {
                    controller.cadastrarProcesso(
                            txtCodigoProcesso.getText().trim(),
                            txtCodigoDisciplina.getText().trim(),
                            comboStatus.getValue());
                    carregarTabela();
                    limparCampos();
                    mostrarAviso("Processo cadastrado com sucesso");
                } catch (Exception ex) {
                    mostrarErro(ex.getMessage());
                }
            }
        });

        btnEditar.setOnAction(e -> {
            Processo selecionado = tabelaProcessos.getSelectionModel().getSelectedItem();
            if (selecionado != null) {
                if (validarCampos()) {
                    try {
                        controller.atualizarProcesso(
                                selecionado.getCodigo(),
                                txtCodigoDisciplina.getText().trim(),
                                comboStatus.getValue());
                        carregarTabela();
                        mostrarAviso("Processo atualizado com sucesso");
                    } catch (Exception ex) {
                        mostrarErro(ex.getMessage());
                    }
                }
            } else {
                mostrarAviso("Selecione um processo para editar");
            }
        });

        btnExcluir.setOnAction(e -> {
            Processo selecionado = tabelaProcessos.getSelectionModel().getSelectedItem();
            if (selecionado != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Deseja realmente excluir este processo?", ButtonType.YES, ButtonType.NO);
                confirm.setTitle("Confirmação");
                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.YES) {
                        try {
                            controller.excluirProcesso(selecionado.getCodigo());
                            carregarTabela();
                            limparCampos();
                            mostrarAviso("Processo excluído com sucesso");
                        } catch (Exception ex) {
                            mostrarErro(ex.getMessage());
                        }
                    }
                });
            } else {
                mostrarAviso("Selecione um processo para excluir");
            }
        });

        btnLimpar.setOnAction(e -> limparCampos());

        btnBuscarProcesso.setOnAction(e -> {
            String codigo = txtCodigoProcesso.getText().trim();
            if (codigo.isEmpty()) {
                mostrarAviso("Informe o código do processo para buscar");
                return;
            }
            Processo processo = controller.buscarProcesso(codigo);
            if (processo != null) {
                txtCodigoDisciplina.setText(processo.getCodigoDisciplina());
                comboStatus.setValue(processo.getStatus());
            } else {
                mostrarAviso("Processo não encontrado");
            }
        });

        tabelaProcessos = new TableView<>();
        TableColumn<Processo, String> colCodigo = new TableColumn<>("Código");
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        TableColumn<Processo, String> colDisciplina = new TableColumn<>("Disciplina");
        colDisciplina.setCellValueFactory(new PropertyValueFactory<>("codigoDisciplina"));
        TableColumn<Processo, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        tabelaProcessos.getColumns().addAll(colCodigo, colDisciplina, colStatus);
        tabelaProcessos.setPrefHeight(250);

        tabelaProcessos.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtCodigoProcesso.setText(newVal.getCodigo());
                txtCodigoDisciplina.setText(newVal.getCodigoDisciplina());
                comboStatus.setValue(newVal.getStatus());
            }
        });

        vbox.getChildren().addAll(grid, buttons, tabelaProcessos);

        Scene scene = new Scene(vbox, 900, 520);
        stage.setScene(scene);
        stage.show();

        // Carrega os dados da tabela ao abrir a tela
        carregarTabela();
    }

    /**
     * Carrega todos os processos da base de dados e exibe na tabela
     */
    private void carregarTabela() {
        try {
            Fila<Processo> fila = controller.listarProcessosEmFila();
            ObservableList<Processo> data = FXCollections.observableArrayList();
            while (!fila.isEmpty()) {
                data.add(fila.remove());
            }
            tabelaProcessos.setItems(data);
        } catch (Exception e) {
            mostrarErro(e.getMessage());
        }
    }

    /**
     * Limpa todos os campos de entrada da tela
     */
    private void limparCampos() {
        txtCodigoProcesso.clear();
        txtCodigoDisciplina.clear();
        comboStatus.setValue(null);
    }

    /**
     * Valida se todos os campos obrigatórios estão preenchidos
     * e retorna true se todos os campos são válidos, false caso contrário
     */
    private boolean validarCampos() {
        if (txtCodigoProcesso.getText().trim().isEmpty()) {
            mostrarAviso("Código do processo é obrigatório");
            return false;
        }
        if (txtCodigoDisciplina.getText().trim().isEmpty()) {
            mostrarAviso("Código da disciplina é obrigatório");
            return false;
        }
        if (comboStatus.getValue() == null || comboStatus.getValue().trim().isEmpty()) {
            mostrarAviso("Status do processo é obrigatório");
            return false;
        }
        return true;
    }
    /**
     * Exibe uma caixa de diálogo com aviso/informação
     */    private void mostrarAviso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
    /**
     * Exibe uma caixa de diálogo com mensagem de erro
     */    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
