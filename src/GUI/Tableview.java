package GUI;

import Data.Agenda;
import Data.Artist;
import Data.Performance;
import Data.Podium;
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

import java.util.ArrayList;

public class Tableview extends Tab {
    private Tab tab;
    private Agenda agenda;
    private TableView table = new TableView();
    private BorderPane borderPane = new BorderPane();
    final HBox hb = new HBox();


    final ObservableList<Performance> data = FXCollections.observableArrayList();


    public Tableview(String name, Agenda agenda){

        for (Performance peformance:agenda.getPerformanceList()) {
            data.add(peformance);
        }
        System.out.println(data);
        this.agenda = agenda;
        Tab overview = new Tab(name);
        table.setEditable(true);
        TableColumn stageCol = new TableColumn("Podium");
        stageCol.setCellValueFactory(
                new PropertyValueFactory<Performance,String>("podium")
        );
        TableColumn artistCol = new TableColumn("Artiest");
        artistCol.setCellValueFactory(
                new PropertyValueFactory<Performance,String>("name")
        );
        TableColumn beginTimeCol = new TableColumn("Begintijd");
        beginTimeCol.setCellValueFactory(
                new PropertyValueFactory<Performance,Integer>("startTime")
        );

        TableColumn endTimeCol = new TableColumn("Eindtijd");
        endTimeCol.setCellValueFactory(
                new PropertyValueFactory<Performance,Integer>("endTime")
        );
        TableColumn popularityCol = new TableColumn("Populariteit");
        popularityCol.setCellValueFactory(
                new PropertyValueFactory<Performance,Integer>("Popularity")
        );
        table.setItems(data);

        final TextField addPodium = new TextField();
        addPodium.setPromptText("Podium");
        addPodium.setMaxWidth(stageCol.getPrefWidth());

        final TextField addArtist = new TextField();
        addArtist.setMaxWidth(artistCol.getPrefWidth()/2);
        addArtist.setPromptText("Artist");

        final TextField addGenre = new TextField();
        addGenre.setMaxWidth(artistCol.getPrefWidth()/2);
        addGenre.setPromptText("Genre");

        final TextField addBeginTime = new TextField();
        addBeginTime.setMaxWidth(beginTimeCol.getPrefWidth());
        addBeginTime.setPromptText("Begin Time");

        final TextField addEndTime = new TextField();
        addEndTime.setMaxWidth(endTimeCol.getPrefWidth());
        addEndTime.setPromptText("End Time");

        final TextField addPopularity = new TextField();
        addPopularity.setMaxWidth(popularityCol.getPrefWidth());
        addPopularity.setPromptText("Popularity");

        final Button addButton = new Button("Add");

        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                data.add(new Performance(new Podium(
                        addPodium.getText()),
                        Integer.parseInt(addBeginTime.getText()), Integer.parseInt(addEndTime.getText()),
                        new Artist(addArtist.getText(), addGenre.getText()),
                        Integer.parseInt(addPopularity.getText())));

                addPodium.clear();
                addBeginTime.clear();
                addEndTime.clear();
                addArtist.clear();
                addGenre.clear();
                addPopularity.clear();

            }

        });


        table.getColumns().addAll(stageCol, artistCol, beginTimeCol,endTimeCol,popularityCol);


        hb.getChildren().addAll(addPodium,addArtist,addGenre,addBeginTime, addEndTime, addPopularity,addButton);
        hb.setSpacing(3);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().addAll(table, hb);

        overview.setContent(vbox);

        this.tab = overview;
    }
    public Tab getTab(){
        return this.tab;
    }
}
