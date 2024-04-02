package GUI;

import Data.Agenda;
import Data.Artist;
import Data.Performance;
import Data.Podium;
import GUI.Overview.Overview;
import GUI.Popup.Popup;
import GUI.Simulator.Simulator;
import GUI.Tableview.Tableview;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class GUI extends Application {
    private final Agenda agenda = new Agenda();
    final ObservableList<Artist> artists = FXCollections.observableArrayList();
    final ObservableList<Podium> podiums = FXCollections.observableArrayList();
    final ObservableList<Performance> performances = FXCollections.observableArrayList();
    private TabPane tabPane;
    private Overview overview;
    private Tableview tableview;
    private Popup popup;
    private Simulator simulator;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        agenda.load();
        primaryStage.setTitle("Festival planner");
        this.popup = new Popup(this);
        this.overview = new Overview(this, popup);
        this.tableview = new Tableview(this, popup, "Tabelweergave", this.agenda);
        this.simulator = new Simulator(this);

        artists.addAll(agenda.getArtistList());
        podiums.addAll(agenda.getPodiumList());
        performances.addAll(agenda.getPerformanceList());

        this.tabPane = new TabPane(overview.getTab(), tableview.getTab(), simulator.getTab());
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Scene scene = new Scene(tabPane);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();

        refresh();
    }

    public Agenda getAgenda() {
        return agenda;
    }

    public void refresh() {
        update();
        overview.update();
        tableview.update();
        popup.update();
    }
    public void update(){
        performances.clear();
        performances.addAll(agenda.getPerformanceList());

        artists.clear();
        artists.addAll(agenda.getArtistList());

        podiums.clear();
        podiums.addAll(agenda.getPodiumList());
    }

    public ObservableList<Artist> getArtists() {
        return artists;
    }

    public ObservableList<Podium> getPodiums() {
        return podiums;
    }

    public ObservableList<Performance> getPerformances() {
        return performances;
    }

    public TabPane getTabPane() {
        return tabPane;
    }
}
