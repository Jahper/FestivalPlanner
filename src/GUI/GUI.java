package GUI;
import Data.Agenda;
import Data.Artist;
import Data.Performance;
import Data.Podium;
import GUI.Overview.Overview;
import GUI.Popup.Popup;
import GUI.Tableview.Tableview;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class GUI extends Application {
    private Agenda agenda = new Agenda();
    private Overview overview;
    private Tableview tableview;
    private Popup popup;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        testData();
        primaryStage.setTitle("Festival planner");
        this.popup = new Popup(this);
        this.overview = new Overview(this, popup);
        this.tableview = new Tableview("Tableview", this.agenda);
        TabPane tabPane = new TabPane(overview.getTab(), tableview.getTab());
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Scene scene = new Scene(tabPane);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }
    public void testData(){
        agenda.addArtist(new Artist("Korte Frans", "Happy Hardcore"));
        agenda.addArtist(new Artist("Duits Frans", "Happy Hardcore"));
        agenda.addArtist(new Artist("Ronnie niet Flex", "Rap"));

        agenda.addPodium(new Podium("Stage 1"));
        agenda.addPodium(new Podium("Stage 2"));

        agenda.addPerformance(new Performance(agenda.getPodiumList().get(0), "10", "14","10","15",
                new Artist("Duits Frans", "Rap"), 10));
        agenda.addPerformance(new Performance(agenda.getPodiumList().get(1), "15","10", "17","30",
                new Artist("Korte Frans", "Country"), 9));
    }

//    public void refresh(this) {
//        overview.refresh(this);
//    }


    public Agenda getAgenda() {
        return agenda;
    }

    public void refresh() {
        overview.update();
        tableview.update();
        popup.update();
    }
}
