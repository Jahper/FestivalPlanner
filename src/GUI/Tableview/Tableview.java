package GUI.Tableview;

import Data.Agenda;
import Data.Artist;
import Data.Performance;
import Data.Podium;
import GUI.GUI;
import GUI.Refreshable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.SnapshotResult;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.swing.*;
import java.sql.Ref;
import java.util.ArrayList;

public class Tableview implements Refreshable {
    private Tab tab;
    private Agenda agenda;
    private TableView table = new TableView();
    private BorderPane borderPane = new BorderPane();
    final HBox hb = new HBox();
    final ObservableList<Performance> data = FXCollections.observableArrayList();
    final ObservableList<Podium> podiums = FXCollections.observableArrayList();
    final ObservableList<Artist> artists = FXCollections.observableArrayList();

    public Tableview(String name, Agenda agenda) {

        for (Performance peformance : agenda.getPerformanceList()) {
            data.add(peformance);
        }
        this.agenda = agenda;
        Tab overview = new Tab(name);
        table.setEditable(true);


        TableColumn stageCol = new TableColumn("Podium");
        stageCol.setCellValueFactory(
                new PropertyValueFactory<Performance, String>("podium")
        );
        TableColumn artistCol = new TableColumn("Artiest");
        artistCol.setCellValueFactory(
                new PropertyValueFactory<Performance, String>("artistName")
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


        final TextField addBeginTimeHour = new TextField();
        addBeginTimeHour.setMaxWidth(beginTimeCol.getPrefWidth() / 2);
        addBeginTimeHour.setPromptText("Begin Time");

        final TextField addBeginTimeMinutes = new TextField();
        addBeginTimeMinutes.setMaxWidth(beginTimeCol.getPrefWidth() / 2);
        addBeginTimeMinutes.setPromptText("Begin Time");

        final TextField addEndTimeHour = new TextField();
        addEndTimeHour.setMaxWidth(endTimeCol.getPrefWidth() / 2);
        addEndTimeHour.setPromptText("End Time");

        final TextField addEndTimeMinutes = new TextField();
        addEndTimeMinutes.setMaxWidth(endTimeCol.getPrefWidth() / 2);
        addEndTimeMinutes.setPromptText("End Time");

        final TextField addEndTime = new TextField();
        addEndTime.setMaxWidth(endTimeCol.getPrefWidth());
        addEndTime.setPromptText("End Time");

        final TextField addPopularity = new TextField();
        addPopularity.setMaxWidth(popularityCol.getPrefWidth());
        addPopularity.setPromptText("Popularity");

        final Label seperatorLabel = new Label(":");
        final Label seperatorLabel1 = new Label(":");
        final Label toLabel = new Label(" to ");

        final ComboBox<Podium> addPodium = new ComboBox<>();
        final ComboBox<Artist> addArtist = new ComboBox<>();
        final Button addButton = new Button("Add");

        for (Podium podium : agenda.getPodiumList()) {
            podiums.add(podium);
        }
        for (Artist artist : agenda.getArtistList()) {
            artists.add(artist);
        }

        addPodium.setItems(podiums);
        addArtist.setItems(artists);


        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    agenda.addPerformance(new Performance(
                            addPodium.getValue(),
                            addBeginTimeHour.getText(), addBeginTimeMinutes.getText(), addEndTimeHour.getText(), addEndTimeMinutes.getText(),
                            addArtist.getValue(),
                            Integer.parseInt(addPopularity.getText())));

                    data.clear();

                    for (Performance peformance : agenda.getPerformanceList()) {
                        data.add(peformance);
                    }

                } catch (Exception e) {

                }
                addEndTime.clear();
                addPopularity.clear();
            }

        });

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().addAll(stageCol, artistCol, beginTimeCol, endTimeCol, popularityCol);

        hb.getChildren().addAll(addPodium, addArtist, addBeginTimeHour, seperatorLabel, addBeginTimeMinutes, toLabel, addEndTimeHour, seperatorLabel1, addEndTimeMinutes, addPopularity, addButton);
        hb.setSpacing(3);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().addAll(table, hb);

        overview.setContent(vbox);

        this.tab = overview;
    }

    public Tab getTab() {
        return this.tab;
    }

    @Override
    public void update() {
        data.clear();
        for (Performance peformance : agenda.getPerformanceList()) {
            data.add(peformance);
        }
        podiums.clear();
        for (Podium podium : agenda.getPodiumList()) {
            podiums.add(podium);
        }
        artists.clear();
        for (Artist artist : agenda.getArtistList()) {
            artists.add(artist);
        }
    }

    @Override
    public void refresh(GUI gui) {
        gui.refresh();
    }
}
