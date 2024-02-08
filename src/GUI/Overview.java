package GUI;

import Data.Agenda;
import Data.Podium;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.jfree.fx.FXGraphics2D;

import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;

public class Overview extends Tab {
    private Tab tab;
    private Agenda agenda;

    public Overview(String name, Agenda agenda) {
        Tab overview = new Tab(name);
        this.tab = overview;
        this.agenda = agenda;

        BorderPane borderPane = new BorderPane();

        borderPane.setLeft(getPodiums());
        borderPane.setCenter(getCanvas());
        borderPane.setTop(getTimetable());

        overview.setContent(borderPane);


    }

    private Node getCanvas() {
        Canvas canvas = new Canvas();
        FXGraphics2D graphics = new FXGraphics2D(canvas.getGraphicsContext2D());


        return canvas;
    }

    private Node getPodiums() {
        ArrayList<Podium> podiums = agenda.getPodiumList();
        VBox stages = new VBox();
        stages.setSpacing(75);
        for (Podium podium : podiums) {
            Label l = new Label(podium.toString());
            l.setFont(new Font(20));
            stages.getChildren().add(l);
        }
        return stages;
    }

    private Node getTimetable() {
        HBox timetable = new HBox();
        Label podia = new Label("Podia:                ");
        podia.setFont(new Font(20));
        timetable.getChildren().add(podia);
        timetable.setSpacing(55);

        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                Label l = new Label("0" + i);
                timetable.getChildren().add(l);
            } else {
                Label l = new Label(i + "");
                timetable.getChildren().add(l);
            }
        }
        return timetable;
    }
    public TableView getTableView(){
        TableView tableView = new TableView<>();



        return null;
    }

    public Tab getTab() {
        return this.tab;
    }
}
