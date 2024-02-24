package GUI.Popup;

import Data.Agenda;
import Data.Artist;
import Data.Performance;
import Data.Podium;
import GUI.GUI;
import GUI.Refreshable;
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

import java.sql.Ref;
import java.util.ArrayList;


public class Popup implements Refreshable {
    private Agenda agenda;
    private GUI gui;
    private Stage stage;
    final ObservableList<Artist> artists = FXCollections.observableArrayList();
    final ObservableList<Podium> podiums = FXCollections.observableArrayList();
    final ObservableList<Performance> performances = FXCollections.observableArrayList();
    //todo deze lijsten mogelijk naar centrale GUI klasse halen
    final ObservableList<String> hourList = FXCollections.observableArrayList();
    final ObservableList<String> minuteList = FXCollections.observableArrayList();
    final ObservableList<Integer> popularityList = FXCollections.observableArrayList();
    private Artist artistChange = new Artist("", "");
    private Podium podiumChange = new Podium("");


    public Popup(GUI gui) {
        this.gui = gui;
        this.agenda = this.gui.getAgenda();
        artists.addAll(agenda.getArtistList());
        podiums.addAll(agenda.getPodiumList());
        performances.addAll(agenda.getPerformanceList());
        hourList.addAll(getHourList());
        minuteList.addAll(getMinuteList());
        popularityList.addAll(getPopularityList());
    }

    public Stage addPopup() {
        this.stage = new Stage();
        stage.setWidth(400);
        stage.setHeight(400);
        stage.initStyle(StageStyle.UTILITY);
        stage.setResizable(false);
        BorderPane borderPane = new BorderPane();

        Label label = new Label("Kies een optie om toe te voegen");
        label.setFont(new Font(25));
        label.setAlignment(Pos.CENTER);

        Button performanceButton = new Button("Performance");
        performanceButton.setMinSize(150, 75);
        performanceButton.setFont(new Font(20));
        Button artistButton = new Button("Artist");
        artistButton.setMinSize(150, 75);
        artistButton.setFont(new Font(20));
        Button podiumButton = new Button("Podium");
        podiumButton.setMinSize(150, 75);
        podiumButton.setFont(new Font(20));

        VBox vBox = new VBox(performanceButton, artistButton, podiumButton);
        vBox.setSpacing(35);
        vBox.setAlignment(Pos.CENTER);

        borderPane.setTop(label);
        borderPane.setCenter(vBox);

        Scene scene = new Scene(borderPane);
        stage.setScene(scene);

        //all actions for buttons
        performanceButton.setOnAction(event -> stage.setScene(addPerformance()));
        artistButton.setOnAction(event -> stage.setScene(addArtist()));
        podiumButton.setOnAction(event -> stage.setScene(addPodium()));

        return stage;
    }

    private Scene addArtist() {
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


        VBox vBox = new VBox(artistNameLabel, artistNameTextField, artistGenreLabel, artistGenreTextField, artistFotoLabel,
                beginTijd, comboBoxBeginTijd, eindTijd, comboBoxEindTijd);

        Button applyButton = new Button("Save");
        Button terugButton = new Button("Terug");
        Button exitButton = new Button("Exit");

        HBox hBox = new HBox(applyButton, terugButton, exitButton);

        applyButton.setOnAction(event -> {
            if (!artistNameTextField.getText().isEmpty() && !artistGenreTextField.getText().isEmpty()) {
                agenda.addArtist(new Artist(artistNameTextField.getText(), artistGenreTextField.getText()));
                System.out.println(agenda.getArtistList());
                artistNameTextField.clear();
                artistGenreTextField.clear();
                refresh(gui);
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

        return new Scene(borderPane);
    }

    private Scene addPodium() {
        BorderPane borderPane = new BorderPane();
        Label podiumNameLabel = new Label("Name");

        TextField podiumNameTextField = new TextField();

        VBox vBox = new VBox(podiumNameLabel, podiumNameTextField);

        Button applyButton = new Button("Save");
        Button terugButton = new Button("Terug");
        Button exitButton = new Button("Exit");

        HBox buttonHbox = new HBox(applyButton, terugButton, exitButton);

        borderPane.setCenter(vBox);
        borderPane.setBottom(buttonHbox);

        applyButton.setOnAction(event -> {
            if (!podiumNameTextField.getText().isEmpty()) {
                agenda.addPodium(new Podium(podiumNameTextField.getText()));
                System.out.println(agenda.getPodiumList());
                podiumNameTextField.clear();
                refresh(gui);
            }
        });

        terugButton.setOnAction(event -> {
            this.stage.close();
            addPopup().show();
        });
        exitButton.setOnAction(event -> {
            this.stage.close();
        });

        return new Scene(borderPane);
    }

    //todo
    private Scene addPerformance() {
        BorderPane borderPane = new BorderPane();

        Button applyButton = new Button("Save");
        Button terugButton = new Button("Terug");
        Button exitButton = new Button("Exit");

        HBox buttonHbox = new HBox(applyButton, terugButton, exitButton);

        ComboBox<Podium> podiumBox = new ComboBox<>(podiums);
        ComboBox<Artist> artistBox = new ComboBox<>(artists);

        ComboBox<String> startHourBox = new ComboBox<>(hourList);
        ComboBox<String> startMinuteBox = new ComboBox<>(minuteList);

        HBox startTimeSelect = new HBox(startHourBox, new Label(" : "), startMinuteBox);

        ComboBox<String> endHourBox = new ComboBox<>(hourList);
        ComboBox<String> endMinuteBox = new ComboBox<>(minuteList);

        HBox endTimeSelect = new HBox(endHourBox, new Label(" : "), endMinuteBox);

        ComboBox<Integer> popularityBox = new ComboBox<>(popularityList);

        VBox vBox = new VBox(new Label("Podium:"), podiumBox, new Label("Artist:"), artistBox, new Label("Start time:"), startTimeSelect,
                new Label("End time: "), endTimeSelect, new Label("Popularity: "), popularityBox
        );


        borderPane.setCenter(vBox);
        borderPane.setBottom(buttonHbox);

        applyButton.setOnAction(event -> {
            if (podiumBox.getValue() != null && startHourBox.getValue() != null && startMinuteBox.getValue() != null &&
                    endHourBox.getValue() != null && endMinuteBox.getValue() != null && artistBox.getValue() != null &&
                    popularityBox.getValue() != null) {
                agenda.addPerformance(new Performance(podiumBox.getValue(), startHourBox.getValue(),
                        startMinuteBox.getValue(), endHourBox.getValue(), endMinuteBox.getValue(), artistBox.getValue(), popularityBox.getValue())
                );

                refresh(gui);
            }
        });

        terugButton.setOnAction(event -> {
            this.stage.close();
            addPopup().show();
        });
        exitButton.setOnAction(event -> {
            this.stage.close();
        });
        return new Scene(borderPane);
    }

    public Stage changePopup() {
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
        artistButton.setMinSize(150, 75);
        artistButton.setFont(new Font(20));
        Button podiumButton = new Button("Podium");
        podiumButton.setMinSize(150, 75);
        podiumButton.setFont(new Font(20));

        HBox hBox = new HBox(artistButton, podiumButton);
        hBox.setSpacing(35);
        hBox.setAlignment(Pos.CENTER);

        borderPane.setTop(label);
        borderPane.setCenter(hBox);
        artistButton.setOnAction(event -> {
            stage.setScene(changeArtist());
        });

        podiumButton.setOnAction(event -> {
            stage.setScene(changePodium());
        });

        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        return stage;
    }

    private Scene changeArtist() {
        BorderPane borderPane = new BorderPane();

        Label label = new Label("Artiest veranderen");
        label.setFont(new Font(25));
        label.setAlignment(Pos.CENTER);

        ComboBox artistComboBox = new ComboBox<>();
        artistComboBox.setMinSize(200, 50);
        artistComboBox.setItems(artists);

        Button changeButton = new Button("Change");

        VBox vBox = new VBox(artistComboBox);
        vBox.setSpacing(35);

        borderPane.setTop(label);
        borderPane.setCenter(vBox);
        borderPane.setBottom(changeButton);

        changeButton.setOnAction(event -> {
            this.stage.setScene(changeArtistSave());
            this.artistChange = (Artist) artistComboBox.getValue();
        });

        return new Scene(borderPane);
    }

    private Scene changeArtistSave() {
        BorderPane borderPane = new BorderPane();

        Label label = new Label("Artiest veranderen");
        label.setFont(new Font(25));
        label.setAlignment(Pos.CENTER);
        Button changeButton = new Button("Change");

        TextField artistTextField = new TextField(artistChange.getName());
        TextField genreTextField = new TextField(artistChange.getGenre());

        Label artistLabel = new Label("Artist");
        Label genreLabel = new Label("Genre");

        VBox vBox = new VBox(artistLabel, artistTextField, genreLabel, genreTextField);
        vBox.setSpacing(35);

        vBox.setAlignment(Pos.CENTER);

        borderPane.setTop(label);
        borderPane.setCenter(vBox);
        borderPane.setBottom(changeButton);

        changeButton.setOnAction(event -> {
            for (Artist a : artists) {
                if (this.artistChange.equals(a)) {
                    a.setName(artistTextField.getText());
                    a.setGenre(genreTextField.getText());
                }
            }
            refresh(gui);
            System.out.println(agenda.getArtistList());
        });

        return new Scene(borderPane);
    }

    private Scene changePodium() {
        BorderPane borderPane = new BorderPane();

        Label label = new Label("Podium veranderen");
        label.setFont(new Font(25));
        label.setAlignment(Pos.CENTER);

        ComboBox artistComboBox = new ComboBox<>();
        artistComboBox.setMinSize(200, 50);
        artistComboBox.setItems(podiums);

        Button changeButton = new Button("Change");

        VBox vBox = new VBox(artistComboBox);
        vBox.setSpacing(35);

        borderPane.setTop(label);
        borderPane.setCenter(vBox);
        borderPane.setBottom(changeButton);

        changeButton.setOnAction(event -> {
            this.stage.setScene(changePodiumSave());
            this.podiumChange = (Podium) artistComboBox.getValue();
        });

        return new Scene(borderPane);
    }

    private Scene changePodiumSave() {
        BorderPane borderPane = new BorderPane();

        Label label = new Label("Podium veranderen");
        label.setFont(new Font(25));
        label.setAlignment(Pos.CENTER);

        Button changeButton = new Button("Change");

        TextField podiumTextField = new TextField(artistChange.getName());

        Label podiumLabel = new Label("Podium");

        VBox vBox = new VBox(podiumLabel, podiumTextField);
        vBox.setSpacing(35);

        vBox.setAlignment(Pos.CENTER);

        borderPane.setTop(label);
        borderPane.setCenter(vBox);
        borderPane.setBottom(changeButton);

        changeButton.setOnAction(event -> {
            for (Podium p : podiums) {
                if (this.podiumChange.equals(p)) {
                    p.setName(podiumTextField.getText());
                }
            }
            System.out.println(agenda.getPodiumList());
            refresh(gui);
        });

        return new Scene(borderPane);
    }

    public Stage deletePopUp() {
        this.stage = new Stage();
        stage.setWidth(400);
        stage.setHeight(400);
        stage.initStyle(StageStyle.UTILITY);
        stage.setResizable(false);
        BorderPane borderPane = new BorderPane();

        Label label = new Label("Kies een optie om te verwijderen");
        label.setFont(new Font(25));
        label.setAlignment(Pos.CENTER);

        Button artistButton = new Button("Artist");
        artistButton.setMinSize(150, 75);
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
            stage.setScene(deleteArtist());
        });
        podiumButton.setOnAction(event -> stage.setScene(deletePodium()));

        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        return stage;
    }

    private Scene deletePodium() {
        BorderPane borderPane = new BorderPane();

        Label label = new Label("Podium verwijderen");
        label.setFont(new Font(25));
        label.setAlignment(Pos.CENTER);

        Button deleteButton = new Button("Delete");

        ComboBox podiumComboBox = new ComboBox<>();
        podiumComboBox.setMinSize(200, 50);
        podiumComboBox.setItems(podiums);

        borderPane.setTop(label);
        borderPane.setCenter(podiumComboBox);
        borderPane.setBottom(deleteButton);

        deleteButton.setOnAction(event -> {
            agenda.getPodiumList().remove(podiumComboBox.getValue());
            podiums.remove(podiumComboBox.getValue());
            refresh(gui);
        });

        return new Scene(borderPane);
    }

    private Scene deleteArtist() {
        BorderPane borderPane = new BorderPane();

        Label label = new Label("Artiest verwijderen");
        label.setFont(new Font(25));
        label.setAlignment(Pos.CENTER);

        Button deleteButton = new Button("Delete");

        ComboBox artistComboBox = new ComboBox<>();
        artistComboBox.setMinSize(200, 50);
        artistComboBox.setItems(artists);

        borderPane.setTop(label);
        borderPane.setCenter(artistComboBox);
        borderPane.setBottom(deleteButton);

        deleteButton.setOnAction(event -> {
            agenda.getArtistList().remove(artistComboBox.getValue());
            artists.remove(artistComboBox.getValue());
            refresh(gui);
        });

        return new Scene(borderPane);
    }

    private ArrayList<String> getHourList() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                list.add("0" + i);
            } else {
                list.add(i + "");
            }
        }
        return list;
    }

    private ArrayList<String> getMinuteList() {
        ArrayList<String> list = new ArrayList<>();
        list.add("00");
        for (int i = 1; i < 60; i++) {
            if (i % 15 == 0) {
                list.add(i + "");
            }
        }
        return list;
    }

    private ArrayList<Integer> getPopularityList() {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            list.add(i);
        }
        return list;
    }

    @Override
    public void refresh(GUI gui) {
        this.gui.refresh();
    }

    @Override
    public void update() {
        artists.clear();
        artists.addAll(agenda.getArtistList());
        podiums.clear();
        podiums.addAll(agenda.getPodiumList());
        performances.clear();
        performances.addAll(agenda.getPerformanceList());
    }
}
