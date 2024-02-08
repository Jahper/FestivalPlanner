package GUI;

import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;

import java.awt.*;

public class Overview extends Tab {
    private Tab tab;
    public Overview(String name){
        Tab overview = new Tab(name);
        overview.setContent(getCanvas());
        this.tab = overview;
    }

    private Node getCanvas() {





        return new Canvas();//todo
    }

    public Tab getTab(){
        return this.tab;
    }
}
