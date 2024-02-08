package GUI;

import Data.Agenda;
import Data.Artist;
import Data.Performance;
import Data.Podium;
import javafx.scene.SnapshotResult;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

public class Tableview extends Tab {
    private Tab tab;
    private Agenda agenda;
    private TableView table = new TableView();
    private BorderPane borderPane = new BorderPane();

    public Tableview(String name, Agenda agenda){
        this.agenda = agenda;
        Tab overview = new Tab(name);

        table.setEditable(true);
        TableColumn stageCol = new TableColumn("Podium");

        TableColumn artistCol = new TableColumn("Artiest");
        TableColumn beginTimeCol = new TableColumn("Begintijd");
        
        TableColumn endTimeCol = new TableColumn("Eindtijd");
        TableColumn popularityCol = new TableColumn("Populariteit");

        table.getColumns().addAll(stageCol, artistCol, beginTimeCol,endTimeCol,popularityCol);
        borderPane.setCenter(table);
        overview.setContent(borderPane);
        this.tab = overview;
    }
    public Tab getTab(){
        return this.tab;
    }
}
