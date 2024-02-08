package GUI;

import Data.Agenda;
import Data.Performance;
import javafx.scene.SnapshotResult;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class Tableview extends Tab {
    private Tab tab;
    private Agenda agenda;
    private TableView table = new TableView();

    public Tableview(String name, Agenda agenda){
        Tab overview = new Tab(name);
        table.setEditable(true);

        TableColumn stageCol = new TableColumn("Podium");
        TableColumn artistCol = new TableColumn("Artiest");
        TableColumn beginTimeCol = new TableColumn("Begintijd");
        TableColumn endTimeCol = new TableColumn("Eindtijd");
        TableColumn popularityCol = new TableColumn("Populariteit");

        table.getColumns().addAll(stageCol, artistCol, beginTimeCol,endTimeCol,popularityCol);
        overview.setContent(table);


        this.tab = overview;
        this.agenda = agenda;
    }
    public Tab getTab(){
        return this.tab;
    }
}
