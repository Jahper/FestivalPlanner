package GUI.Simulator;

import GUI.GUI;
import Data.Performance;
import Data.Podium;
import GUI.Simulator.NPC.NPC;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Simulator {

    private Map map;
    private ResizableCanvas canvas;
    private BorderPane mainPane;
    private FXGraphics2D g2d;
    private Camera camera;
    private ArrayList<NPC> npcs;
    private ArrayList<Target> targets;
    private ArrayList<Target> entranceAndExitTargets;
    private ArrayList<Performance> performances;
    private int seconds = 0;
    private int minutes = 0;
    private int hours = 0;
    private Label label = new Label("");
    private HBox hBox = new HBox();
    private Button playPauseButton;
    private Button emergencyButton;
    private Boolean running;
    private GUI gui;
    private HashMap<Podium, Target> podia;
    private Slider timeLine;
    private double sliderValue;
    private Label tijdlijnLabel;
    private boolean emergency = false;
    private Random r = new Random();
    private TextField numberOfVisitors;

    public Simulator(GUI gui) throws Exception {
        this.gui = gui;
        podia = new HashMap<>();
        sliderValue = 0.0;
        init();

        playPauseButton = new Button("▶/II");
        playPauseButton.setOnAction(event -> {
            running = !running;
        });

        emergencyButton = new Button("Noodgeval");
        emergencyButton.setOnAction(event -> {
            emergency = true;
        });

        tijdlijnLabel = new Label("Tijdlijn: ");
        timeLine = new Slider(0.0, 24.0, 1.0);
        timeLine.setValue(sliderValue);
        timeLine.setMinWidth(240.0);
        timeLine.setBlockIncrement(1.0);

        numberOfVisitors = new TextField("75");//fixme
        numberOfVisitors.setMinWidth(100);



        mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);

        label.setFont(new Font(20));

        hBox.setPadding(new Insets(1));
        hBox.setSpacing(5);

        hBox.getChildren().addAll(label, playPauseButton, emergencyButton, numberOfVisitors, tijdlijnLabel, timeLine);

        mainPane.setBottom(hBox);


        this.g2d = new FXGraphics2D(canvas.getGraphicsContext2D());
        this.camera = new Camera(canvas, g -> draw(g), g2d);
        g2d.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());


        new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now) {
                if (last == -1)
                    last = now;
                update((now - last) / 1000000000.0);
                last = now;
                draw(g2d);
            }
        }.start();

        draw(g2d);
    }


    public void init() {
        running = true;
        map = new Map("files/Festival Planner Normal Version V.5.3.json");
        ArrayList<Podium> podiums = gui.getAgenda().getPodiumList();
        targets = map.getSpectatorTargets();
        for (int i = 0; i < podiums.size(); i++) {
            podia.put(podiums.get(i), targets.get(i + 4));
        }

        performances = new ArrayList<>();
        performances.addAll(gui.getAgenda().getPerformanceList());

        entranceAndExitTargets = map.getEntranceAndExitTargets();

        npcs = new ArrayList<>();
        Point2D newPosition = new Point2D.Double(entranceAndExitTargets.get(0).getX() + 70, entranceAndExitTargets.get(0).getY() + 50);
        boolean hasCollision = false;
        for (NPC visitor : npcs) {
            if (visitor.getPosition().distance(newPosition) < 32) {
                hasCollision = true;
            }
        }
        if (!hasCollision && !emergency) {
            npcs.add(new NPC(newPosition, 0, targets.get(r.nextInt(4)), false));
        }
    }

    public void draw(FXGraphics2D g) {
        g.setTransform(new AffineTransform());
        g.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
        g.setTransform(camera.getTransform((int) canvas.getWidth(), (int) canvas.getHeight()));
        map.draw(g);

        for (NPC visitor : npcs) {
            visitor.draw(g);
        }
    }

    public void update(double deltaTime) {
        if (!running || !gui.getTabPane().getTabs().get(2).isSelected()) {
            return;
        }

        tooLate();

        ArrayList<NPC> toRemove = new ArrayList<>();
        for (NPC npc : npcs) {
            toRemove.add(npc.update(this.npcs, this.entranceAndExitTargets));
        }

        for (NPC npc : toRemove) {
            npcs.remove(npc);
        }
        if (emergency) {
            for (NPC npc : npcs) {
                npc.emergencyExit(entranceAndExitTargets);
            }
        }


        //fixme
        Random r = new Random();
        Point2D newPosition = new Point2D.Double(entranceAndExitTargets.get(r.nextInt(2)).getX() + 32, entranceAndExitTargets.get(r.nextInt(2)).getY() + 32);
        boolean hasCollision = false;
        for (NPC visitor : npcs) {
            if (visitor.getPosition().distance(newPosition) < 64) {
                hasCollision = true;
            }
        }
        if (!hasCollision && !emergency) {
            if (npcs.size() < Integer.parseInt(numberOfVisitors.getText())) {//fixme
                npcs.add(new NPC(newPosition, 0, targets.get(r.nextInt(4)), false));
            }
        }


        if (deltaTime > 0.01) {
            seconds += 5;
            if (seconds > 60) {
                this.minutes++;
                seconds = 0;
                if (this.minutes > 59) {
                    hours++;
                    this.minutes = 0;
                    if (hours > 23) {
                        hours = 0;

                    }
                }
            }
            if (hours < 10 && minutes < 10) {
                label.setText("0" + hours + " : 0" + minutes);
            } else if (hours < 10) {
                label.setText("0" + hours + " : " + minutes);
            } else if (minutes < 10) {
                label.setText(hours + " : 0" + minutes);
            } else {
                label.setText(hours + " : " + minutes);
            }
        }
        if (hours < 10 && this.minutes < 10) {
            label.setText("0" + hours + " : 0" + minutes);
        } else if (hours < 10) {
            label.setText("0" + hours + " : " + minutes);
        } else if (this.minutes < 10) {
            label.setText(hours + " : 0" + minutes);
        } else {
            label.setText(hours + " : " + minutes);
        }
        sliderValue = hours + minutes / 60.0;
        timeLine.setValue(sliderValue);

        setNpcTarget();
    }

    public void tooLate() {
        if (hours > 22 && minutes > 58) {
            emergency = true;
        }
    }

    public void setNpcTarget() {
        String minutes = String.valueOf(this.minutes);
        if (minutes.length() < 2) {
            minutes = "0" + minutes;
        }
        ArrayList<Performance> livePerformances = this.gui.getAgenda().getLivePerformances(String.valueOf(hours), minutes);
        ArrayList<NPC> notBusyList = new ArrayList<>();
        for (NPC npc : npcs) {
            if (!npc.isBusy()) {
                notBusyList.add(npc);
            }
        }

        int count;
        if (livePerformances.isEmpty()) {
            for (NPC npc : npcs) {
                npc.setTarget(targets.get(r.nextInt(4)));
            }
            if (performances.isEmpty()) {
                emergency = true;
            }
        } else {
            for (Performance performance : livePerformances) {
                performances.remove(performance);
                count = performance.getAttendanceList();
                while (count < performance.getPopularity() * (npcs.size() / 10)) {
                    if (notBusyList.isEmpty()) {
                        return;
                    }
                    int npc = r.nextInt(notBusyList.size());

                    notBusyList.get(npc).setPerformanceTarget(podia.get(performance.getPodium()));
                    performance.addNpc(notBusyList.get(npc));

                    notBusyList.remove(npc);
                    count++;
                }
            }
            for (NPC npc : notBusyList) {
                npc.setTarget(targets.get(r.nextInt(4)));
            }
        }
    }

    public Tab getTab() {
        Tab t = new Tab("Simulatie");
        t.setContent(mainPane);
        return t;
    }
}
