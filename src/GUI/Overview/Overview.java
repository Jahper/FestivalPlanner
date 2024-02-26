package GUI.Overview;

import Data.Agenda;
import Data.Artist;
import Data.Performance;
import Data.Podium;
import GUI.GUI;
import GUI.Popup.Popup;
import GUI.Refreshable;
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
import org.omg.CosNaming.BindingIterator;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Overview implements Refreshable {
    private Tab tab;
    private GUI gui;
    private Agenda agenda;
    private BorderPane borderPane;
    private ResizableCanvas canvas;
    private FXGraphics2D graphics;
    final ObservableList<Artist> artists = FXCollections.observableArrayList();
    final ObservableList<Podium> podiums = FXCollections.observableArrayList();
    final ObservableList<Performance> performances = FXCollections.observableArrayList();
    final ArrayList<Shape> performanceRectangles = new ArrayList<>();
    final ArrayList<Performance2D> performanceInfoList = new ArrayList<>();
    private Popup popup;
    private double spacing;


    public Overview(GUI gui, Popup popup) {
        this.gui = gui;

        Tab overview = new Tab("Overview");
        this.tab = overview;
        this.agenda = gui.getAgenda();
        this.popup = popup;


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
        Button addPerformance = new Button("Add");
        Button changeButton = new Button("Change");
        Button removeButton = new Button("Remove");

        HBox buttonBox = new HBox(refreshButton, addPerformance, changeButton, removeButton);

        refreshButton.setOnAction(event -> {
            update();
            refresh(this.gui);
        });
        addPerformance.setOnAction(event -> {
            popup.addPopup().show();

        });
        changeButton.setOnAction(event -> {
            popup.changePopup().show();
        });
        removeButton.setOnAction(event -> {
            popup.deletePopUp().show();
        });

        borderPane.setLeft(getPodiums());
        borderPane.setCenter(this.canvas);
//        borderPane.setTop(getTimetable());
        borderPane.setBottom(buttonBox);

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
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        drawPerformance(graphics);
        drawTimetable(graphics);
    }

    public void drawPerformance(Graphics2D graphics) {
        performanceRectangles.clear();
        performanceInfoList.clear();
        for (Performance performance : performances) {
            Shape shape = new Rectangle2D.Double(performance.getStartTime() * 0.75, podiums.indexOf(performance.getPodium()) * 100 + 40,
                    performance.getEndTime() * 0.75 - performance.getStartTime() * 0.75, 100);
            performanceRectangles.add(shape);
            performanceInfoList.add(new Performance2D(performance, (int) (performance.getEndTime() * 0.75 - performance.getStartTime() * 0.75),
                    (int) (performance.getStartTime() * 0.75), podiums.indexOf(performance.getPodium()) * 100)
            );
        }
        graphics.setColor(Color.CYAN);
        for (Shape performanceRectangle : performanceRectangles) {
            graphics.draw(performanceRectangle);
            graphics.fill(performanceRectangle);
        }
        graphics.setColor(Color.BLACK);
        for (Performance2D performance2D : performanceInfoList) {
            graphics.drawString(performance2D.getArtists(), performance2D.getX(), performance2D.getY() + 65);
            graphics.drawString(performance2D.getTimeDuration(), performance2D.getX(), performance2D.getY() + 95);
            graphics.drawString(performance2D.getPopularity(), performance2D.getX(), performance2D.getY() + 125);
        }
    }

    //toont podiums aan zijkant van scherm
    private Node getPodiums() {
        ArrayList<Podium> podiums = agenda.getPodiumList();
        VBox podiumVBox = new VBox();
        podiumVBox.setMaxWidth(150);
        podiumVBox.setMinWidth(150);
        podiumVBox.setSpacing(75);
        for (Podium podium : podiums) {
            Label l = new Label(podium.toString());
            l.setFont(new Font(20));
            podiumVBox.getChildren().add(l);
        }
        Label podia = new Label("Podia:");
        podia.setFont(new Font(20));
        VBox sideVBox = new VBox(podia, podiumVBox);
        return sideVBox;
    }

    private void drawTimetable(FXGraphics2D graphics) {
        graphics.setColor(Color.black);
        this.spacing = canvas.getWidth() / 24;
        graphics.drawLine(0, 40, (int) canvas.getWidth(), 40);
        graphics.drawLine(1, 0, 1, (int) canvas.getHeight());
        graphics.drawLine((int) canvas.getWidth(), 0,(int) canvas.getWidth(), (int) canvas.getHeight());
        graphics.drawLine(0,  (int) canvas.getHeight(),(int) canvas.getWidth(), (int) canvas.getHeight());
        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                graphics.drawString("0" + i, (int) spacing * i + 35, 20);
            } else {
                graphics.drawString("" + i, (int) spacing * i + 35, 20);
            }
            int halfSpacing = (int) (spacing * 0.5 + 40);
            if (i < 23) {
                graphics.drawLine((int) spacing * i + halfSpacing, 0, (int) spacing * i + halfSpacing, 40);
            }
        }
    }

    @Override
    public void update() {
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

        borderPane.setLeft(getPodiums());
        draw(graphics);
    }

    public Tab getTab() {
        return this.tab;
    }

    @Override
    public void refresh(GUI gui) {
        gui.refresh();
    }
}
