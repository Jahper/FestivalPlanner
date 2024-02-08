package GUI;
import Data.Agenda;
import Data.Artist;
import Data.Performance;
import Data.Podium;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class GUI extends Application {
    private Agenda agenda = new Agenda();
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        testData();
        primaryStage.setTitle("Festival planner");
        Overview overview = new Overview("Overview", this.agenda);
        Tableview tableview = new Tableview("Tableview", this.agenda);
        TabPane tabPane = new TabPane(tableview.getTab(), overview.getTab());
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Scene scene = new Scene(tabPane,500,500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public void testData(){
//        agenda.addArtist(new Artist("Korte Frans", "Happy Hardcore"));
//        agenda.addArtist(new Artist("Duits Frans", "Happy Hardcore"));
//        agenda.addArtist(new Artist("Ronnie niet Flex", "Rap"));
//
//        agenda.addPodium(new Podium("Stage 1"));
//        agenda.addPodium(new Podium("Stage 2"));

        agenda.addPerformance(new Performance(new Podium("stage 1"), 10, 14,
                new Artist("Duits Frans", "Rap"), 10));
        agenda.addPerformance(new Performance(new Podium("stage 2"), 15, 17,
                new Artist("Korte Frans", "Country"), 9));
    }
}
