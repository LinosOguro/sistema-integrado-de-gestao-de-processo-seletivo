package view;

import br.elinos.FilaGenerica.Fila;
import controller.InscricaoController;
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
import model.Entidades.Inscricao;

/**
 * Tela de gerenciamento de Inscrições em Processos Seletivos.
 * Funcionalidades: Inscrever professores, atualizar e remover inscrições.
 */
public class TelaInscricao {

    private final InscricaoController controller = new InscricaoController();

    //TextFields
    private TextField txtCPFProfessor = new TextField();
    private TextField txtCodigoDisciplina = new TextField();
    private TextField txtCodigoProcesso = new TextField();
    
    //TableViews
    private TableView<Inscricao> tabelaInscricoes;

    /**
     * Exibe a tela de gerenciamento de inscrições
     */
    @SuppressWarnings("unchecked")
    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Inscrições em Processos Seletivos");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        //Labels e entrada de dados
        grid.add(new Label("CPF do Professor:"), 0, 0);
        grid.add(txtCPFProfessor, 1, 0);
        Button btnBuscarProfessor = new Button("🔎");
        grid.add(btnBuscarProfessor, 2, 0);

        grid.add(new Label("Código da Disciplina:"), 0, 1);
        grid.add(txtCodigoDisciplina, 1, 1);
        Button btnBuscarDisciplina = new Button("🔎");
        grid.add(btnBuscarDisciplina, 2, 1);

        grid.add(new Label("Código do Processo:"), 3, 0);
        grid.add(txtCodigoProcesso, 4, 0);

        //Botões
        HBox buttonsInscricoes = new HBox(10);
        Button btnInscrever = new Button("Inscrever");
        Button btnLimparInscricao = new Button("Limpar tela");
        Button btnAtualizarInscricao = new Button("Atualizar");
        Button btnRemoverInscricao = new Button("Remover");
        buttonsInscricoes.getChildren().addAll(btnInscrever, btnLimparInscricao, btnAtualizarInscricao, btnRemoverInscricao);

        btnInscrever.setOnAction(e -> {
            if (validarCampos()) {
                try {
                    controller.inscreverProfessor(
                            txtCPFProfessor.getText().trim(),
                            txtCodigoDisciplina.getText().trim(),
                            txtCodigoProcesso.getText().trim());
                    carregarTabela();
                    limparCampos();
                    mostrarAviso("Inscrição realizada com sucesso");
                } catch (Exception ex) {
                    mostrarErro(ex.getMessage());
                }
            }
        });

        btnLimparInscricao.setOnAction(e -> limparCampos());

        btnAtualizarInscricao.setOnAction(e -> {
            Inscricao selecionada = tabelaInscricoes.getSelectionModel().getSelectedItem();
            if (selecionada != null) {
                try {
                    controller.atualizarInscricao(
                            selecionada.getCpfProfessor(),
                            selecionada.getCodigoDisciplina(),
                            txtCodigoProcesso.getText().trim());
                    carregarTabela();
                    mostrarAviso("Inscrição atualizada com sucesso");
                } catch (Exception ex) {
                    mostrarErro(ex.getMessage());
                }
            } else {
                mostrarAviso("Selecione uma inscrição para atualizar");
            }
        });

        btnRemoverInscricao.setOnAction(e -> {
            Inscricao selecionada = tabelaInscricoes.getSelectionModel().getSelectedItem();
            if (selecionada != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Deseja realmente remover esta inscrição?", ButtonType.YES, ButtonType.NO);
                confirm.setTitle("Confirmação");
                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.YES) {
                        try {
                            controller.removerInscricao(selecionada.getCpfProfessor(), selecionada.getCodigoDisciplina());
                            carregarTabela();
                            limparCampos();
                            mostrarAviso("Inscrição removida com sucesso");
                        } catch (Exception ex) {
                            mostrarErro(ex.getMessage());
                        }
                    }
                });
            } else {
                mostrarAviso("Selecione uma inscrição para remover");
            }
        });

        btnBuscarProfessor.setOnAction(e -> {
            if (txtCPFProfessor.getText().trim().isEmpty() || txtCodigoDisciplina.getText().trim().isEmpty()) {
                mostrarAviso("Informe CPF e código da disciplina para buscar a inscrição");
                return;
            }
            Inscricao inscricao = controller.buscarInscricao(txtCPFProfessor.getText().trim(), txtCodigoDisciplina.getText().trim());
            if (inscricao != null) {
                txtCodigoProcesso.setText(inscricao.getCodigoProcesso());
            } else {
                mostrarAviso("Inscrição não encontrada");
            }
        });

        btnBuscarDisciplina.setOnAction(e -> {
            if (txtCPFProfessor.getText().trim().isEmpty() || txtCodigoDisciplina.getText().trim().isEmpty()) {
                mostrarAviso("Informe CPF e código da disciplina para buscar a inscrição");
                return;
            }
            Inscricao inscricao = controller.buscarInscricao(txtCPFProfessor.getText().trim(), txtCodigoDisciplina.getText().trim());
            if (inscricao != null) {
                txtCodigoProcesso.setText(inscricao.getCodigoProcesso());
            } else {
                mostrarAviso("Inscrição não encontrada");
            }
        });

        tabelaInscricoes = new TableView<>();
        TableColumn<Inscricao, String> colCPF = new TableColumn<>("CPF Professor");
        colCPF.setCellValueFactory(new PropertyValueFactory<>("cpfProfessor"));
        TableColumn<Inscricao, String> colDisciplina = new TableColumn<>("Código Disciplina");
        colDisciplina.setCellValueFactory(new PropertyValueFactory<>("codigoDisciplina"));
        TableColumn<Inscricao, String> colProcesso = new TableColumn<>("Código Processo");
        colProcesso.setCellValueFactory(new PropertyValueFactory<>("codigoProcesso"));

        tabelaInscricoes.getColumns().addAll(colCPF, colDisciplina, colProcesso);
        tabelaInscricoes.setPrefHeight(200);

        tabelaInscricoes.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtCPFProfessor.setText(newVal.getCpfProfessor());
                txtCodigoDisciplina.setText(newVal.getCodigoDisciplina());
                txtCodigoProcesso.setText(newVal.getCodigoProcesso());
            }
        });

        vbox.getChildren().addAll(grid, buttonsInscricoes, tabelaInscricoes);

        Scene scene = new Scene(vbox, 900, 600);
        stage.setScene(scene);
        stage.show();

        // Carrega os dados da tabela ao abrir a tela
        carregarTabela();
    }

    /**
     * Carrega todas as inscrições da base de dados e exibe na tabela
     */
    private void carregarTabela() {
        try {
            Fila<Inscricao> fila = controller.listarInscricoesEmFila();
            ObservableList<Inscricao> data = FXCollections.observableArrayList();
            while (!fila.isEmpty()) {
                data.add(fila.remove());
            }
            tabelaInscricoes.setItems(data);
        } catch (Exception e) {
            mostrarErro(e.getMessage());
        }
    }

    /**
     * Limpa todos os campos de entrada da tela
     */
    private void limparCampos() {
        txtCPFProfessor.clear();
        txtCodigoDisciplina.clear();
        txtCodigoProcesso.clear();
    }

    /**
     * Valida se todos os campos obrigatórios estão preenchidos
     */
    private boolean validarCampos() {
        if (txtCPFProfessor.getText().trim().isEmpty()) {
            mostrarAviso("CPF é obrigatório");
            return false;
        }
        if (txtCodigoDisciplina.getText().trim().isEmpty()) {
            mostrarAviso("Código da disciplina é obrigatório");
            return false;
        }
        if (txtCodigoProcesso.getText().trim().isEmpty()) {
            mostrarAviso("Código do processo é obrigatório");
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
