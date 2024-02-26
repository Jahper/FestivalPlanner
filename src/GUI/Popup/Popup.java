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
import sun.misc.Perf;
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
    private Performance performanceChange = null;


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
        //Creating stage
        this.stage = createStage();

        //Creating labels
        Label label = createLabel("Kies een optie om toe te voegen", 25);

        //Creating all buttons
        Button performaceButton = createButton("Performance", 150, 75, 20);
        Button artistButton = createButton("Artiest", 150, 75, 20);
        Button podiumButton = createButton("Podium", 150, 75, 20);

        //Alligning everything and adding to vBox
        VBox vBox = new VBox(performaceButton, artistButton, podiumButton);
        vBox.setSpacing(35);
        vBox.setAlignment(Pos.CENTER);

        //Creating and filling the borderpane
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(label);
        borderPane.setCenter(vBox);

        //Creating scene and setting scene
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);

        //All actions for buttons
        performaceButton.setOnAction(event -> stage.setScene(addPerformance()));
        artistButton.setOnAction(event -> stage.setScene(addArtist()));
        podiumButton.setOnAction(event -> stage.setScene(addPodium()));

        return stage;
    }

    private Scene addArtist() {
        Label artistNameLabel = new Label("Naam");
        Label artistGenreLabel = new Label("Genre");

        TextField artistNameTextField = new TextField();
        TextField artistGenreTextField = new TextField();

        VBox vBox = new VBox(artistNameLabel, artistNameTextField, artistGenreLabel, artistGenreTextField);

        Button applyButton = new Button("Opslaan");
        Button terugButton = new Button("Terug");
        Button exitButton = new Button("Afsluiten");

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

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(vBox);
        borderPane.setBottom(hBox);

        return new Scene(borderPane);
    }

    private Scene addPodium() {
        Label podiumNameLabel = new Label("Naam");

        TextField podiumNameTextField = new TextField();

        VBox vBox = new VBox(podiumNameLabel, podiumNameTextField);

        Button applyButton = new Button("Opslaan");
        Button terugButton = new Button("Terug");
        Button exitButton = new Button("Afsluiten");

        HBox buttonHbox = new HBox(applyButton, terugButton, exitButton);

        BorderPane borderPane = new BorderPane();
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
    public Scene addPerformance() {
        Button applyButton = new Button("Opslaan");
        Button terugButton = new Button("Terug");
        Button exitButton = new Button("Afsluiten");

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

        VBox vBox = new VBox(new Label("Podium:"), podiumBox, new Label("Artiest:"), artistBox, new Label("Begintijd:"), startTimeSelect,
                new Label("Eindtijd: "), endTimeSelect, new Label("Populariteit: "), popularityBox
        );

        BorderPane borderPane = new BorderPane();
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
        this.stage = createStage();

        Label label = createLabel("Kies een optie om te veranderen", 25);

        Button performanceButton = createButton("Performance", 150, 75, 20);
        Button artistButton = createButton("Artiest", 150, 75, 20);
        Button podiumButton = createButton("Podium", 150, 75, 20);

        VBox vBox = new VBox(performanceButton, artistButton, podiumButton);
        vBox.setSpacing(35);
        vBox.setAlignment(Pos.CENTER);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(label);
        borderPane.setCenter(vBox);

        performanceButton.setOnAction(event -> stage.setScene(changePerformance()));
        artistButton.setOnAction(event -> stage.setScene(changeArtist()));
        podiumButton.setOnAction(event -> stage.setScene(changePodium()));

        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        return stage;
    }

    private Scene changePerformance() {
        ComboBox<Performance> performanceBox = new ComboBox<>(performances);
        Button nextButton = new Button("Volgende");
        VBox selectPerformance = new VBox(new Label("Selecteer een optreden:"), performanceBox, nextButton);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(selectPerformance);

        nextButton.setOnAction(event -> {
            performanceChange = performanceBox.getValue();
            this.stage.setScene(changePerformanceSave());
        });
        return new Scene(borderPane);
    }
    private Scene changePerformanceSave() {
        //todo mogelijk onderste deel van borderpane in methode zetten
        Button applyButton = new Button("Opslaan");

        ComboBox<Podium> podiumBox = new ComboBox<>(podiums);
        podiumBox.setValue(performanceChange.getPodium());

        //todo artist werkt nog niet helemaal?
        ComboBox<Artist> artistBox = new ComboBox<>(artists);
        artistBox.setValue(performanceChange.getArtists().get(0));//todo dit fixen??

        //todo minutes en hours apart niet een ding
        ComboBox<String> startHourBox = new ComboBox<>(hourList);
        startHourBox.setValue(performanceChange.getStartTimeGui());
        ComboBox<String> startMinuteBox = new ComboBox<>(minuteList);

        HBox startTimeSelect = new HBox(startHourBox, new Label(" : "), startMinuteBox);

        //todo hetzelde als bij start
        ComboBox<String> endHourBox = new ComboBox<>(hourList);
        endHourBox.setValue(performanceChange.getEndTimeGui());
        ComboBox<String> endMinuteBox = new ComboBox<>(minuteList);

        HBox endTimeSelect = new HBox(endHourBox, new Label(" : "), endMinuteBox);

        ComboBox<Integer> popularityBox = new ComboBox<>(popularityList);
        popularityBox.setValue(performanceChange.getPopularity());

        VBox vBox = new VBox(new Label("Podium:"), podiumBox, new Label("Artist:"), artistBox, new Label("Start time:"), startTimeSelect,
                new Label("End time: "), endTimeSelect, new Label("Popularity: "), popularityBox
        );

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(vBox);
        borderPane.setBottom(applyButton);

        applyButton.setOnAction(event -> {
            if (podiumBox.getValue() != null && startHourBox.getValue() != null && startMinuteBox.getValue() != null &&
                    endHourBox.getValue() != null && endMinuteBox.getValue() != null && artistBox.getValue() != null &&
                    popularityBox.getValue() != null) {
//                agenda.addPerformance(new Performance(podiumBox.getValue(), startHourBox.getValue(),
//                        startMinuteBox.getValue(), endHourBox.getValue(), endMinuteBox.getValue(), artistBox.getValue(), popularityBox.getValue())
//                );
                Performance performance = performanceChange;
                performance.setPodium(podiumBox.getValue());
                performance.setStartTime(startHourBox.getValue(), startMinuteBox.getValue());
                performance.setEndTime(endHourBox.getValue(), endMinuteBox.getValue());
                ArrayList<Artist> artistList = new ArrayList<>();
                artistList.add(artistBox.getValue());
                performance.setArtists(new ArrayList<>(artistList));
                performance.setPopularity(popularityBox.getValue());
                System.out.println(performance);
                refresh(gui);
            }
        });
        return new Scene(borderPane);
    }

    private Scene changeArtist() {
        //todo hier ff naar kijken
        Label label = createLabel("Artiest veranderen", 25);

        ComboBox<Artist> artistComboBox = new ComboBox<>(artists);
        artistComboBox.setMinSize(200, 50);

        Button changeButton = new Button("Veranderen");

        VBox vBox = new VBox(artistComboBox);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(label);
        borderPane.setCenter(vBox);
        borderPane.setBottom(changeButton);

        changeButton.setOnAction(event -> {
            this.artistChange = artistComboBox.getValue();
            this.stage.setScene(changeArtistSave());
        });

        return new Scene(borderPane);
    }

    private Scene changeArtistSave() {
        Label label = createLabel("Artiest veranderen", 25);

        Button changeButton = new Button("Veranderen");

        TextField artistTextField = new TextField(artistChange.getNameGui());
        TextField genreTextField = new TextField(artistChange.getGenre());

        Label artistLabel = new Label("Artiest");
        Label genreLabel = new Label("Genre");

        VBox vBox = new VBox(artistLabel, artistTextField, genreLabel, genreTextField);
        vBox.setSpacing(35);

        vBox.setAlignment(Pos.CENTER);

        BorderPane borderPane = new BorderPane();
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
        Label label = createLabel("Podium veranderen", 25);

        ComboBox artistComboBox = new ComboBox<>(podiums);
        artistComboBox.setMinSize(200, 50);

        Button changeButton = new Button("Veranderen");

        VBox vBox = new VBox(artistComboBox);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(label);
        borderPane.setCenter(vBox);
        borderPane.setBottom(changeButton);

        changeButton.setOnAction(event -> {
            this.podiumChange = (Podium) artistComboBox.getValue();
            this.stage.setScene(changePodiumSave());
        });

        return new Scene(borderPane);
    }

    private Scene changePodiumSave() {
        Label label = createLabel("Podium veranderen", 25);

        Button changeButton = new Button("Veranderen");

        TextField podiumTextField = new TextField(podiumChange.getName());

        Label podiumLabel = new Label("Podium");

        VBox vBox = new VBox(podiumLabel, podiumTextField);
        vBox.setSpacing(35);
        vBox.setAlignment(Pos.CENTER);

        BorderPane borderPane = new BorderPane();
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
        this.stage = createStage();

        Label label = createLabel("Kies een optie om te verwijderen", 25);

        Button performanceButton = createButton("Performance", 150, 75, 20);
        Button artistButton = createButton("Artiest", 150, 75, 20);
        Button podiumButton = createButton("Podium", 150, 75, 20);

        VBox vBox = new VBox(performanceButton, artistButton, podiumButton);
        vBox.setSpacing(35);
        vBox.setAlignment(Pos.CENTER);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(label);
        borderPane.setCenter(vBox);

        performanceButton.setOnAction(event -> stage.setScene(deletePerformance()));
        artistButton.setOnAction(event -> stage.setScene(deleteArtist()));
        podiumButton.setOnAction(event -> stage.setScene(deletePodium()));

        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        return stage;
    }

    private Scene deletePerformance() {
        Label label = createLabel("Optreden verwijderen", 25);

        Button deleteButton = new Button("Verwijderen");

        ComboBox podiumComboBox = new ComboBox<>(performances);
        podiumComboBox.setMinSize(200, 50);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(label);
        borderPane.setCenter(podiumComboBox);
        borderPane.setBottom(deleteButton);

        deleteButton.setOnAction(event -> {
            agenda.getPerformanceList().remove(podiumComboBox.getValue());
            performances.remove(podiumComboBox.getValue());
            refresh(gui);
        });

        return new Scene(borderPane);
    }

    private Scene deletePodium() {
        Label label = createLabel("Podium verwijderen", 25);

        Button deleteButton = new Button("Verwijderen");

        ComboBox podiumComboBox = new ComboBox<>(podiums);
        podiumComboBox.setMinSize(200, 50);

        BorderPane borderPane = new BorderPane();
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
        Label label = createLabel("Artiest verwijderen", 25);

        Button deleteButton = new Button("Verwijderen");

        ComboBox artistComboBox = new ComboBox<>(artists);
        artistComboBox.setMinSize(200, 50);

        BorderPane borderPane = new BorderPane();
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

    public Stage infoPopup(Performance performance) {
        this.stage = createStage();

        Label label = createLabel("Informatie over performance", 25);
        Label artistLabel = createLabel("Artist: " + performance.getArtistName(), 15);
        Label genreLabel = createLabel("Genre: " + performance.getArtists(), 15); //geen genre getter
        Label stageLabel = createLabel("Stage: " + performance.getPodium(), 15);
        Label populairityLabel = createLabel("Populairity: " + performance.getPopularity(), 15);
        Label startTimeLabel = createLabel("Starttime: " + performance.getStartTimeGui(), 15);
        Label endTimeLabel = createLabel("Endtime: " + performance.getEndTimeGui(), 15);

        VBox vBox = new VBox(artistLabel, genreLabel, stageLabel, populairityLabel, startTimeLabel, endTimeLabel);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(label);
        borderPane.setCenter(vBox);

        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        return stage;
    }
    private Button createButton(String text, double width, double height, double fontSize){
        Button button = new Button(text);
        button.setMinSize(width, height);
        button.setFont(new Font(fontSize));
        return button;
    }

    private Label createLabel(String text, double fontSize){
        Label label = new Label(text);
        label.setFont(new Font(fontSize));
        return label;
    }
    private Stage createStage(){
        this.stage = new Stage();
        stage.setWidth(400);
        stage.setHeight(400);
        stage.initStyle(StageStyle.UTILITY);
        stage.setResizable(false);
        return stage;
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