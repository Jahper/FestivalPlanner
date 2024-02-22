package GUI;

import Data.Agenda;
import Data.Artist;
import Data.Podium;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Popup{
    private Agenda agenda;
    private Stage stage;
    final ObservableList<Artist> artists = FXCollections.observableArrayList();


    public Popup(Agenda agenda) {
        this.agenda = agenda;
    }

    public Stage addPopup(){
        this.stage = new Stage();
        stage.setWidth(400);
        stage.setHeight(400);
        stage.initStyle(StageStyle.UTILITY);
        stage.setResizable(false);
        BorderPane borderPane = new BorderPane();

        Label label = new Label("Kies een optie om toe te voegen");
        label.setFont(new Font(25));
        label.setAlignment(Pos.CENTER);

        Button artistButton = new Button("Artist");
        artistButton.setMinSize(150,75);
        artistButton.setFont(new Font(20));
        Button podiumButton = new Button("Podium");
        podiumButton.setMinSize(150, 75);
        podiumButton.setFont(new Font(20));

        HBox hBox = new HBox();
        hBox.setSpacing(35);
        hBox.getChildren().add(artistButton);
        hBox.getChildren().add(podiumButton);
        hBox.setAlignment(Pos.CENTER);

        borderPane.setTop(label);
        borderPane.setCenter(hBox);

        Scene scene = new Scene(borderPane);
        stage.setScene(scene);

        //all actions for buttons
        artistButton.setOnAction(event -> {
            stage.setScene(addArtist());
        });

        podiumButton.setOnAction(event -> {
            stage.setScene(addPoduim());
        });

        return stage;
    }
    public Scene addArtist(){
        BorderPane borderPane = new BorderPane();
        Label artistNameLabel = new Label("Name");
        Label artistGenreLabel = new Label("Genre");
        Label artistFotoLabel = new Label("Foto");
        Label beginTijd = new Label("Begintijd");
        Label eindTijd = new Label("Eindtijd");

        TextField artistNameTextField = new TextField();
        TextField artistGenreTextField = new TextField();

        ComboBox comboBoxBeginTijd = new ComboBox<>();
        ComboBox comboBoxEindTijd = new ComboBox<>();


        VBox vBox = new VBox();
        vBox.getChildren().add(artistNameLabel);
        vBox.getChildren().add(artistNameTextField);
        vBox.getChildren().add(artistGenreLabel);
        vBox.getChildren().add(artistGenreTextField);
        vBox.getChildren().add(artistFotoLabel);
        vBox.getChildren().add(beginTijd);
        vBox.getChildren().add(comboBoxBeginTijd);
        vBox.getChildren().add(eindTijd);
        vBox.getChildren().add(comboBoxEindTijd);

        Button applyButton = new Button("Save");
        Button exitButton = new Button("Exit");
        Button terugButton = new Button("Terug");

        HBox hBox = new HBox();
        hBox.getChildren().add(applyButton);
        hBox.getChildren().add(exitButton);
        hBox.getChildren().add(terugButton);

        applyButton.setOnAction(event -> {
            if (!artistNameTextField.getText().isEmpty() && !artistGenreTextField.getText().isEmpty()){
            agenda.addArtist(new Artist(artistNameTextField.getText(), artistGenreTextField.getText()));
            System.out.println(agenda.getArtistList());
            artistNameTextField.clear();
            artistGenreTextField.clear();
            }
        });

        exitButton.setOnAction(event -> {
            this.stage.close();
        });

        terugButton.setOnAction(event -> {
            this.stage.close();
            addPopup().show();

        });



        borderPane.setCenter(vBox);
        borderPane.setBottom(hBox);

        Scene scene = new Scene(borderPane);

        return scene;
    }

    public Scene addPoduim(){
        BorderPane borderPane = new BorderPane();
        Label podiumNameLabel = new Label("Name");

        TextField podiumNameTextField = new TextField();

        VBox vBox = new VBox();
        vBox.getChildren().add(podiumNameLabel);
        vBox.getChildren().add(podiumNameTextField);

        Button applyButton = new Button("Save");
        Button terugButton = new Button("Terug");
        Button exitButton = new Button("Exit");

        HBox buttonHbox = new HBox();
        buttonHbox.getChildren().add(applyButton);
        buttonHbox.getChildren().add(terugButton);
        buttonHbox.getChildren().add(exitButton);

        borderPane.setCenter(vBox);
        borderPane.setBottom(buttonHbox);

        Scene scene = new Scene(borderPane);

        applyButton.setOnAction(event -> {
            if (!podiumNameTextField.getText().isEmpty()){
            agenda.addPodium(new Podium(podiumNameTextField.getText()));
            System.out.println(agenda.getPodiumList());
            podiumNameTextField.clear();
            }
        });

        terugButton.setOnAction(event -> {
            this.stage.close();
            addPopup().show();
        });
        exitButton.setOnAction(event -> {
            this.stage.close();
        });


        return scene;
    }

    public Stage changePopup(){
        this.stage = new Stage();
        stage.setWidth(400);
        stage.setHeight(400);
        stage.initStyle(StageStyle.UTILITY);
        stage.setResizable(false);
        BorderPane borderPane = new BorderPane();

        Label label = new Label("Kies een optie om te veranderen");
        label.setFont(new Font(25));
        label.setAlignment(Pos.CENTER);

        Button artistButton = new Button("Artist");
        artistButton.setMinSize(150,75);
        artistButton.setFont(new Font(20));
        Button podiumButton = new Button("Podium");
        podiumButton.setMinSize(150, 75);
        podiumButton.setFont(new Font(20));

        HBox hBox = new HBox();
        hBox.setSpacing(35);
        hBox.getChildren().add(artistButton);
        hBox.getChildren().add(podiumButton);
        hBox.setAlignment(Pos.CENTER);

        borderPane.setTop(label);
        borderPane.setCenter(hBox);
        artistButton.setOnAction(event -> {
            stage.setScene(changeArtist());
        });


        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        return stage;
    }
    public Scene changeArtist(){
        for (Artist a : agenda.getArtistList()) {
            if (!artists.contains(a)){
            artists.add(a);
            }
        }
        BorderPane borderPane = new BorderPane();

        Label label = new Label("Artiest veranderen");
        label.setFont(new Font(25));
        label.setAlignment(Pos.CENTER);

        ComboBox artistComboBox = new ComboBox<>();
        artistComboBox.setMinSize(200,50);
        artistComboBox.setItems(artists);

        Button changeButton = new Button("Change");


        HBox hBox = new HBox();
        hBox.setSpacing(35);
        hBox.getChildren().add(artistComboBox);
        hBox.setAlignment(Pos.CENTER);

        borderPane.setTop(label);
        borderPane.setCenter(hBox);
        borderPane.setBottom(changeButton);

        changeButton.setOnAction(event -> {
            this.stage.setScene(changeArtistSave());
        });

        Scene scene = new Scene(borderPane);
        return scene;

    }

    public Scene changeArtistSave(){
        BorderPane borderPane = new BorderPane();

        Label label = new Label("Artiest veranderen");
        label.setFont(new Font(25));
        label.setAlignment(Pos.CENTER);

        Button changeButton = new Button("Change");

        TextField artistTextField = new TextField();
        TextField genreTextField = new TextField();

        Label artistLabel = new Label("Artist");
        Label genreLabel = new Label("Genre");


        VBox vBox = new VBox();
        vBox.getChildren().add(artistLabel);
        vBox.getChildren().add(artistTextField);
        vBox.getChildren().add(genreLabel);
        vBox.getChildren().add(genreTextField);
        vBox.setSpacing(35);

        vBox.setAlignment(Pos.CENTER);

        borderPane.setTop(label);
        borderPane.setCenter(vBox);
        borderPane.setBottom(changeButton);

        changeButton.setOnAction(event -> {
            for (Artist a:artists) {
                a.getName()
            }
        });

        Scene scene = new Scene(borderPane);
        return scene;

    }
}
