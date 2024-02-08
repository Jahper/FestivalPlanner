package GUI;

import Data.Agenda;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;

import java.awt.*;

public class Overview extends Tab {
    private Tab tab;
    private Agenda agenda;
    public Overview(String name, Agenda agenda){
        Tab overview = new Tab(name);
        overview.setContent(new Label("hsbvjsjvsdfjbsfb"));
        overview.setContent(getCanvas());
        this.tab = overview;
        this.agenda = agenda;
    }

    private Node getCanvas() {





        return new Canvas();//todo
    }

    public Tab getTab(){
        return this.tab;
    }
}
