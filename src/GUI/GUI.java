package GUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class GUI extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Festival planner");
        Tab tab1 = new Tab("hallo");
        Overview overview = new Overview("Overview");
        Tableview tableview = new Tableview("Tableview");
        TabPane tabPane = new TabPane(tableview.getTab(),overview.getTab());
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Scene scene = new Scene(tabPane,500,500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
