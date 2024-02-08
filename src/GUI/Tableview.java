package GUI;

import javafx.scene.control.Label;
import javafx.scene.control.Tab;

public class Tableview extends Tab {
    private Tab tab;
    public Tableview(String name){
        Tab overview = new Tab(name);
        overview.setContent(new Label("hsbvjsjvsdfjbsfb"));
        this.tab = overview;
    }
    public Tab getTab(){
        return this.tab;
    }
}
