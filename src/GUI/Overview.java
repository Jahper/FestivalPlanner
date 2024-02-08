package GUI;

import javafx.scene.control.Label;
import javafx.scene.control.Tab;

public class Overview extends Tab {
    private Tab tab;
    public Overview(String name){
        Tab overview = new Tab(name);
        Tableview tableview = new Tableview("tabelview");
        overview.setContent(new Label("hsbvjsjvsdfjbsfb"));
        this.tab = overview;
    }
    public Tab getTab(){
        return this.tab;
    }
}
