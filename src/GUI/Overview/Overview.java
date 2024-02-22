package GUI.Overview;

import Data.Agenda;
import Data.Artist;
import Data.Performance;
import Data.Podium;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.control.Button;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Overview extends Tab {
    private Tab tab;
    private Agenda agenda;
    private BorderPane borderPane;
    private ResizableCanvas canvas;
    final ObservableList<Artist> artists = FXCollections.observableArrayList();
    final ObservableList<Podium> podiums = FXCollections.observableArrayList();
    final ObservableList<Performance> performances = FXCollections.observableArrayList();
    final ArrayList<Shape> performanceRectangles = new ArrayList<>();

    public Overview(String name, Agenda agenda) {

        Tab overview = new Tab(name);
        this.tab = overview;
        this.agenda = agenda;


        for (Artist artist : agenda.getArtistList()) {
            artists.add(artist);
        }
        for (Podium podium : agenda.getPodiumList()) {
            podiums.add(podium);
        }
        for (Performance performance : agenda.getPerformanceList()) {
            performances.add(performance);
        }

        this.borderPane = new BorderPane();

        this.canvas = getCanvas(borderPane);

        Button refreshButton = new Button("Refresh");

        refreshButton.setOnAction(event -> update());

        borderPane.setLeft(getPodiums());
        borderPane.setCenter(this.canvas);
        borderPane.setTop(getTimetable());
        borderPane.setBottom(refreshButton);

        overview.setContent(borderPane);


    }

    private ResizableCanvas getCanvas(BorderPane borderPane) {
        ResizableCanvas canvas = new ResizableCanvas(g -> draw(g), borderPane);
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));

        return canvas;
    }

    public void draw(FXGraphics2D graphics) {
        graphics.setColor(Color.BLUE);
        drawPerformance(graphics);
        for (Shape shape : performanceRectangles) {
            graphics.fill(shape);
            graphics.draw(shape);
        }
    }

    public void drawPerformance(FXGraphics2D graphics) {//standaard spacing voor blokjes is 74
        for (Performance performance : performances) {//podiums.indexOf(performance.getPodium()) * 100
            Shape shape = new Rectangle2D.Double(performance.getStartTime() * 75, podiums.indexOf(performance.getPodium()) * 100,
                    performance.getEndTime() * 75- performance.getStartTime() * 75, 100);
            performanceRectangles.add(shape);
            System.out.println(podiums.indexOf(performance.getPodium()));
//            Shape shape = new Rectangle2D.Double(81,0,81,100);
//            performanceRectangles.add(shape);


        }
//        Shape shape = new Rectangle2D.Double(162,0,81,100);
//        performanceRectangles.add(shape);
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
        Label podia = new Label("Podia:       ");
        podia.setFont(new Font(20));
        timetable.getChildren().add(podia);
        timetable.setSpacing(57);

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
    public void update(){
        this.canvas = getCanvas(this.borderPane);
        System.out.println(performances.size());
    }

    public TableView getTableView() {
        TableView tableView = new TableView<>();


        return tableView;
    }

    public Tab getTab() {
        return this.tab;
    }
}
