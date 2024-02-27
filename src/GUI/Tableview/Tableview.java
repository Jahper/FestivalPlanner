package GUI.Tableview;

import Data.Agenda;
import Data.Artist;
import Data.Performance;
import Data.Podium;
import GUI.GUI;
import GUI.Popup.Popup;
import GUI.Refreshable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Tableview implements Refreshable {
    private final Tab tab;
    private final Agenda agenda;
    private final GUI gui;
    private final Popup popup;


    final HBox hb = new HBox();
    final ObservableList<Performance> data = FXCollections.observableArrayList();
    final ObservableList<Podium> podiums = FXCollections.observableArrayList();
    final ObservableList<Artist> artists = FXCollections.observableArrayList();

    public Tableview(GUI gui, Popup popup, String name, Agenda agenda) {
        this.gui = gui;
        this.popup=popup;

        data.addAll(agenda.getPerformanceList());
        this.agenda = agenda;
        Tab overview = new Tab(name);
        TableView table = new TableView();
        table.setEditable(true);


        TableColumn stageCol = new TableColumn("Podium");
        stageCol.setCellValueFactory(
                new PropertyValueFactory<Performance, String>("podium")
        );
        TableColumn artistCol = new TableColumn("Artiest");
        artistCol.setCellValueFactory(
                new PropertyValueFactory<Performance, Artist>("artist")
        );
        TableColumn beginTimeCol = new TableColumn("Begintijd");
        beginTimeCol.setCellValueFactory(
                new PropertyValueFactory<Performance, String>("startTimeGui")
        );

        TableColumn endTimeCol = new TableColumn("Eindtijd");
        endTimeCol.setCellValueFactory(
                new PropertyValueFactory<Performance, Integer>("endTimeGui")
        );
        TableColumn popularityCol = new TableColumn("Populariteit");
        popularityCol.setCellValueFactory(
                new PropertyValueFactory<Performance, Integer>("Popularity")
        );
        table.setItems(data);


        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().addAll(stageCol, artistCol, beginTimeCol, endTimeCol, popularityCol);



        Button refreshButton = new Button("Refresh");
        Button addPerformance = new Button("Toevoegen");
        Button changeButton = new Button("Aanpassen");
        Button removeButton = new Button("Verwijderen");
        Button opslaanButton = new Button("Opslaan");
        Button inladenButton = new Button("Inladen");

        HBox buttonBox = new HBox(refreshButton, addPerformance, changeButton, removeButton, opslaanButton, inladenButton);

        refreshButton.setOnAction(event -> {
            update();
            refresh(this.gui);
        });

        addPerformance.setOnAction(event -> popup.addPopup().show());

        changeButton.setOnAction(event -> popup.changePopup().show());

        removeButton.setOnAction(event -> popup.deletePopUp().show());

        opslaanButton.setOnAction(event -> {
            agenda.save();
            gui.refresh();
        });
        inladenButton.setOnAction(event -> {
            agenda.load();
            gui.refresh();
        });

        final VBox vbox = new VBox();
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(table);
        borderPane.setBottom(buttonBox);


        overview.setContent(borderPane);

        this.tab = overview;
    }

    public Tab getTab() {
        return this.tab;
    }

    @Override
    public void update() {
        data.clear();
        data.addAll(agenda.getPerformanceList());

        podiums.clear();
        podiums.addAll(agenda.getPodiumList());

        artists.clear();
        artists.addAll(agenda.getArtistList());
    }

    @Override
    public void refresh(GUI gui) {
        gui.refresh();
    }
}
