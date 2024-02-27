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
import javafx.scene.input.MouseButton;
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

public class Overview implements Refreshable {
    private final Tab tab;
    private final GUI gui;
    private final Agenda agenda;
    private final BorderPane borderPane;
    private ResizableCanvas canvas;
    private FXGraphics2D graphics;
    private final Popup popup;
    final ObservableList<Artist> artists = FXCollections.observableArrayList();
    final ObservableList<Podium> podiums = FXCollections.observableArrayList();
    final ObservableList<Performance> performances = FXCollections.observableArrayList();
    final ArrayList<Performance2D> performanceInfoList = new ArrayList<>();
    private int spacing;


    public Overview(GUI gui, Popup popup) {
        this.gui = gui;

        Tab overview = new Tab("Overview");
        this.tab = overview;
        this.agenda = gui.getAgenda();
        this.popup = popup;

        artists.addAll(agenda.getArtistList());
        podiums.addAll(agenda.getPodiumList());
        performances.addAll(agenda.getPerformanceList());

        this.borderPane = new BorderPane();

        this.canvas = getCanvas(borderPane);

        Button refreshButton = new Button("Refresh");
        Button addPerformance = new Button("Toevoegen");
        Button changeButton = new Button("Aanpassen");
        Button removeButton = new Button("Verwijderen");
        Button opslaanButton = new Button("Opslaan");
        Button inladenButton = new Button("Inladen");

        HBox buttonBox = new HBox(refreshButton, addPerformance, changeButton, removeButton, opslaanButton, inladenButton);

        refreshButton.setOnAction(event -> {
            update();
            refresh(this.gui);
        });

        addPerformance.setOnAction(event -> popup.addPopup().show());

        changeButton.setOnAction(event -> popup.changePopup().show());

        removeButton.setOnAction(event -> popup.deletePopUp().show());

        opslaanButton.setOnAction(event -> {
            agenda.save();
            gui.refresh();
        });
        inladenButton.setOnAction(event -> {
            agenda.load();
            gui.refresh();
        });

        borderPane.setLeft(getPodiums());
        borderPane.setCenter(this.canvas);
        borderPane.setBottom(buttonBox);

        overview.setContent(borderPane);
    }


    private ResizableCanvas getCanvas(BorderPane borderPane) {
        this.canvas = new ResizableCanvas(g -> draw(), borderPane);
        this.graphics = new FXGraphics2D(canvas.getGraphicsContext2D());
        draw();
        //bij een left-click toont de UI met een popup de informatie van een optreden,
        //met een middle of right click kan er een optreden toegevoegd worden.
        canvas.setOnMouseClicked(event -> {
            if (event.getButton() != MouseButton.PRIMARY) {
                Popup p = new Popup(gui);
                p.addPopup().show();
                p.getStage().setScene(p.addPerformance());
            } else {
                for (Performance2D performance : performanceInfoList) {
                    if (event.getX() >= performance.getX() && event.getX() <= performance.getEndX()
                            && event.getY() >= performance.getY() + 40 && event.getY() <= performance.getY() + 140) {//
                        this.popup.infoPopup(performance.getPerformance()).show();
                    }
                }
            }
        });
        return canvas;
    }

    public void draw() {
        this.spacing = (int) (Math.round(canvas.getWidth() / 24));
        graphics.setColor(Color.BLUE);
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        drawTimetable();
        drawPerformances();
    }

    //tekent alle performances voor de overview
    public void drawPerformances() {
        performanceInfoList.clear();
        for (Performance performance : performances) {
            int startMinuteOffset = getMinuteWidth(performance.getStartTime());
            int endMinuteOffset = getMinuteWidth(performance.getEndTime());

            double beginX = ((double) performance.getStartTime() / 100) * spacing + startMinuteOffset;
            double endX = ((double) performance.getEndTime() / 100) * spacing - ((double) performance.getStartTime() / 100) * spacing + endMinuteOffset - startMinuteOffset;

            Shape shape = new Rectangle2D.Double(beginX, podiums.indexOf(performance.getPodium()) * 100 + 40, endX, 100);
            performanceInfoList.add(new Performance2D(performance, shape, performance.getEndTime() - performance.getStartTime(),
                    (int) beginX, podiums.indexOf(performance.getPodium()) * 100, (int) endX)
            );
        }
        graphics.setColor(Color.BLACK);
        for (Performance2D performance2D : performanceInfoList) {
            graphics.setColor(Color.BLACK);
            graphics.draw(performance2D.getShape());
            graphics.setColor(getColor(performance2D.getPerformance().getPopularity()));
            graphics.fill(performance2D.getShape());

            graphics.setColor(Color.BLACK);
            graphics.drawString(performance2D.getArtists(), performance2D.getX(), performance2D.getY() + 65);
            graphics.drawString(performance2D.getTimeDuration(), performance2D.getX(), performance2D.getY() + 95);
            graphics.drawString(performance2D.getPopularity(), performance2D.getX(), performance2D.getY() + 125);
        }

    }

    //methode om de juiste offset te bepalen voor kwartieren etc, met 15 kan er bijvoorbeeld lastig gerekend worden
    private int getMinuteWidth(double startTime) {
        int baseTime = (int) startTime % 100;
        int correctedTime = 0;
        if (startTime != 0) {
            switch (baseTime) {
                case 15:
                    correctedTime = (int) Math.round(spacing * 0.25);
                    break;
                case 30:
                    correctedTime = (int) Math.round(spacing * 0.5);
                    break;
                case 45:
                    correctedTime = (int) Math.round(spacing * 0.75);
                    break;
            }
            return correctedTime - baseTime;
        }
        return correctedTime;
    }

    //methode die de tijdschaal tekent
    private void drawTimetable() {
        graphics.setColor(Color.black);
        graphics.drawLine(0, 40, (int) canvas.getWidth(), 40);
        graphics.drawLine(1, 0, 1, (int) canvas.getHeight());
        graphics.drawLine((int) canvas.getWidth(), 0, (int) canvas.getWidth(), (int) canvas.getHeight());
        graphics.drawLine(0, (int) canvas.getHeight(), (int) canvas.getWidth(), (int) canvas.getHeight());
        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                graphics.drawString("0" + i, spacing * i + spacing / 2, 20);
            } else {
                graphics.drawString("" + i, spacing * i + spacing / 2, 20);
            }
            graphics.drawLine(spacing * i, 0, spacing * i, (int) canvas.getHeight());
        }
        for (int i = 40; i < canvas.getHeight(); i += 100) {
            graphics.drawLine(0, i, (int) canvas.getWidth(), i);
        }
    }

    //toont podiums aan zijkant van scherm
    private Node getPodiums() {
        ArrayList<Podium> podiums = agenda.getPodiumList();
        VBox podiumVBox = new VBox();
        podiumVBox.setMaxWidth(150);
        podiumVBox.setMinWidth(150);
        podiumVBox.setSpacing(spacing * 0.7);
        podiumVBox.setMaxHeight(canvas.getHeight());
        for (Podium podium : podiums) {
            Label l = new Label(podium.toString());
            l.setFont(new Font(20));
            podiumVBox.getChildren().add(l);
        }
        Label podia = new Label("Podia:");
        podia.setFont(new Font(20));
        VBox sideVBox = new VBox(podia, podiumVBox);
        sideVBox.setSpacing(20);
        return sideVBox;
    }

    private Color getColor(int popularity) {
        if (popularity > 8) {
            return Color.YELLOW;
        } else if (popularity > 6) {
            return Color.MAGENTA;
        } else if (popularity > 4) {
            return Color.RED;
        }
        return Color.CYAN;
    }

    @Override
    public void update() {
        performances.clear();
        performances.addAll(agenda.getPerformanceList());

        artists.clear();
        artists.addAll(agenda.getArtistList());

        podiums.clear();
        podiums.addAll(agenda.getPodiumList());

        borderPane.setLeft(getPodiums());
        draw();
    }

    @Override
    public void refresh(GUI gui) {
        gui.refresh();
    }

    public Tab getTab() {
        return this.tab;
    }
}
