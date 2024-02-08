package GUI;

import Data.Agenda;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;

public class Tableview extends Tab {
    private Tab tab;
    private Agenda agenda;
    public Tableview(String name, Agenda agenda){
        Tab overview = new Tab(name);
        overview.setContent(new Label("hsbvjsjvsdfjbsfb"));
        this.tab = overview;
        this.agenda = agenda;
    }
    public Tab getTab(){
        return this.tab;
    }
}
