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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

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
    private int seconds = 0;
    private int minutes = 0;
    private int hours = 0;
    private Label label = new Label("");
    private HBox hBox = new HBox();
    private Button playPauseButton;
    private Button pauseButton;
    private Button emergencyButton;
    private Boolean running;
    private GUI gui;
    private HashMap<Podium, Target> podia;
    private Slider timeLine;
    private double sliderValue;
    private Label label1;


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
            for (NPC npc : npcs) {
                npc.setTarget(entranceAndExitTargets.get(0));//todo dichtstbijzijnde toevoegen
            }
        });

        label1 = new Label("Tijdlijn: ");
        timeLine = new Slider(0.0, 24.0, 1.0);
        timeLine.setValue(sliderValue);
        timeLine.setMinWidth(240.0);
        timeLine.setBlockIncrement(1.0);


        mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);

        label.setFont(new Font(20));

        hBox.setPadding(new Insets(1));
        hBox.setSpacing(5);

        hBox.getChildren().addAll(label, playPauseButton, emergencyButton,label1, timeLine);

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

        entranceAndExitTargets = map.getEntranceAndExitTargets();

        npcs = new ArrayList<>();
        Point2D newPosition = new Point2D.Double(entranceAndExitTargets.get(0).getX() + 70, entranceAndExitTargets.get(0).getY() + 50);
        boolean hasCollision = false;
        for (NPC visitor : npcs) {
            if (visitor.getPosition().distance(newPosition) < 32) {
                hasCollision = true;
            }
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

    public void drawNpc(FXGraphics2D g) {
        AffineTransform tx = new AffineTransform();
        double zoom = canvas.getWidth() / 3000;
        tx.scale(.53, 1);
        g.setTransform(tx);

        for (NPC visitor : npcs) {
            visitor.draw(g);
        }
    }

    public void update(double deltaTime) {
        if (!running || !gui.getTabPane().getTabs().get(2).isSelected()) {
            return;
        }
        for (NPC visitor : npcs) {
            visitor.update(this.npcs, String.valueOf(hours), String.valueOf(minutes));
        }


        //fixme
        Random r = new Random();
        Point2D newPosition = new Point2D.Double(entranceAndExitTargets.get(r.nextInt(2)).getX(), entranceAndExitTargets.get(r.nextInt(2)).getY());
        boolean hasCollision = false;
        for (NPC visitor : npcs) {
            if (visitor.getPosition().distance(newPosition) < 64) {
                hasCollision = true;
            }
        }
        if (!hasCollision) {
            if (npcs.size() < 5) {//fixme
                npcs.add(new NPC(newPosition, 0, targets.get(5), false));
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
        sliderValue = hours +minutes/60.0;
        timeLine.setValue(sliderValue);
    }

    private Random r = new Random();

    public void setNpcTarget() {
        String minutes = String.valueOf(this.minutes);
        if (minutes.length() < 2) {
            minutes = "0" + minutes;
        }
        ArrayList<Performance> performances = this.gui.getAgenda().getLivePerformances(String.valueOf(hours), minutes);
        ArrayList<NPC> notBusyList = new ArrayList<>();
//        for (NPC npc : npcs) {
//            if (!npc.isBusy()) {
//                notBusyList.add(npc);
//            }
//        }
//        if (performances.isEmpty()) {
//            for (NPC npc : npcs) {
//                npc.setTarget(targets.get(r.nextInt(4)));
//            }
//        } else {
//            if (notBusyList.isEmpty()) {
//                int deeldinges = notBusyList.size() / performances.size();
//                for (int i = 0; i < performances.size(); i++) {
//                    performances.get(i);
//                    for (int j = 0; j < notBusyList.size(); i++) {
//                        notBusyList.get(j + (i * deeldinges)).setTarget(podia.get(performances.get(i).getPodium()));
//                    }
//                }
//            }
//        for (NPC npc : npcs) { fixme
//            npc.setTarget(podia.get(performances.get(0).getPodium()));
//        }
//        System.out.println(podia.get(performances.get(0).getPodium()));

    }


    public Tab getTab() {
        Tab t = new Tab("Simulatie");
        t.setContent(mainPane);
        return t;
    }
}
