package view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TelaPrincipal extends Application {

    /**
     * Ponto de entrada da aplicação
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Inicializa a tela principal da aplicação com todos os botões de navegação
     */
    @Override
    public void start(Stage stage) {
        stage.setTitle("Sistema de Inscrição");

        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(30));
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: #f5f5f5;");

        //Labels
        Label lblTitulo = new Label("Sistema de Inscrição");
        lblTitulo.setStyle("-fx-font-size: 28; -fx-font-weight: bold;");

        //Botões
        Button btnDisciplina = new Button("Disciplina");
        btnDisciplina.setStyle("-fx-font-size: 16; -fx-padding: 15;");
        btnDisciplina.setPrefWidth(200);
        btnDisciplina.setOnAction(e -> {
            TelaDisciplina tela = new TelaDisciplina();
            tela.show();
        });

        Button btnProfessor = new Button("Professor");
        btnProfessor.setStyle("-fx-font-size: 16; -fx-padding: 15;");
        btnProfessor.setPrefWidth(200);
        btnProfessor.setOnAction(e -> {
            TelaProfessor tela = new TelaProfessor();
            tela.show();
        });

        Button btnCurso = new Button("Curso");
        btnCurso.setStyle("-fx-font-size: 16; -fx-padding: 15;");
        btnCurso.setPrefWidth(200);
        btnCurso.setOnAction(e -> {
            TelaCurso tela = new TelaCurso();
            tela.show();
        });

        Button btnProcesso = new Button("Cadastro de Processo");
        btnProcesso.setStyle("-fx-font-size: 16; -fx-padding: 15;");
        btnProcesso.setPrefWidth(200);
        btnProcesso.setOnAction(e -> {
            TelaProcesso tela = new TelaProcesso();
            tela.show();
        });

        Button btnInscricoes = new Button("Inscrições em Processos");
        btnInscricoes.setStyle("-fx-font-size: 16; -fx-padding: 15;");
        btnInscricoes.setPrefWidth(200);
        btnInscricoes.setOnAction(e -> {
            TelaInscricao tela = new TelaInscricao();
            tela.show();
        });

        Button btnConsultaInscritos = new Button("Consulta de Inscritos");
        btnConsultaInscritos.setStyle("-fx-font-size: 16; -fx-padding: 15;");
        btnConsultaInscritos.setPrefWidth(200);
        btnConsultaInscritos.setOnAction(e -> {
            TelaConsultaInscritos tela = new TelaConsultaInscritos();
            tela.show();
        });

        Button btnProcessosAbertos = new Button("Processos Abertos");
        btnProcessosAbertos.setStyle("-fx-font-size: 16; -fx-padding: 15;");
        btnProcessosAbertos.setPrefWidth(200);
        btnProcessosAbertos.setOnAction(e -> {
            TelaProcessosAbertos tela = new TelaProcessosAbertos();
            tela.show();
        });

        Button btnSair = new Button("Sair");
        btnSair.setStyle("-fx-font-size: 16; -fx-padding: 15;");
        btnSair.setPrefWidth(200);
        btnSair.setOnAction(e -> stage.close());

        vbox.getChildren().addAll(lblTitulo, btnDisciplina, btnProfessor, btnCurso, btnProcesso, btnInscricoes, btnConsultaInscritos, btnProcessosAbertos, btnSair);

        Scene scene = new Scene(vbox, 500, 630);
        stage.setScene(scene);
        stage.show();
    }
}