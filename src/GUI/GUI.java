package GUI;

import Data.Agenda;
import GUI.Overview.Overview;
import GUI.Popup.Popup;
import GUI.Simulator.TiledMap;
import GUI.Tableview.Tableview;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class GUI extends Application {
    private final Agenda agenda = new Agenda();
    private Overview overview;
    private Tableview tableview;
    private Popup popup;
    private TiledMap tiledMap;

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
        this.tiledMap = new TiledMap();
        TabPane tabPane = new TabPane(overview.getTab(), tableview.getTab(), tiledMap.getTab());
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
        overview.update();
        tableview.update();
        popup.update();
    }
}
