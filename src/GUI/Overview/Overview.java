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
    private FXGraphics2D graphics;
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
        this.canvas = new ResizableCanvas(g -> draw(g), borderPane);
        this.graphics = new FXGraphics2D(canvas.getGraphicsContext2D());
        draw(graphics);

        return canvas;
    }

    public void draw(FXGraphics2D graphics) {
        this.graphics = graphics;
        graphics.setColor(Color.BLUE);
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, 1920, 1080);
        drawPerformance(graphics);
        for (Shape shape : performanceRectangles) {
            graphics.fill(shape);
            graphics.draw(shape);
        }
    }

    public void drawPerformance(FXGraphics2D graphics) {//standaard spacing voor blokjes is 74

        for (Performance performance : performances) {//podiums.indexOf(performance.getPodium()) * 100
            Shape shape = new Rectangle2D.Double(performance.getStartTime() * 0.75 + 80 , podiums.indexOf(performance.getPodium()) * 100,
                    performance.getEndTime() * 0.75 - performance.getStartTime() * 0.75, 100);
            performanceRectangles.add(shape);
            System.out.println(performance.getStartTime() * 0.75);
            System.out.println(performance.getEndTime() * 0.75 - performance.getStartTime() * 0.75);
//            System.out.println(podiums.indexOf(performance.getPodium()));
//            Shape shape = new Rectangle2D.Double(81,0,81,100);


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
//        this.canvas = getCanvas(this.borderPane);
        draw(graphics);
        performances.clear();
        for (Performance performance : agenda.getPerformanceList()) {
            performances.add(performance);
        }
        artists.clear();
        for (Artist artist : agenda.getArtistList()) {
            artists.add(artist);
        }
        podiums.clear();
        for (Podium podium : agenda.getPodiumList()) {
            podiums.add(podium);
        }
        System.out.println(performances.size());
        System.out.println(agenda.getPerformanceList().size());
    }

    public Tab getTab() {
        return this.tab;
    }
}
