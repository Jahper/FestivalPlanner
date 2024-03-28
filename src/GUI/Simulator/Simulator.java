package GUI.Simulator;

import Data.Agenda;
import Data.Performance;
import Data.Podium;
import GUI.Simulator.NPC.NPC;
import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
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
    private Agenda agenda;
    private HashMap<Podium, Target> podia;


    public Simulator(Agenda agenda) throws Exception {
        this.agenda = agenda;
        podia = new HashMap<>();

        init();

        mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);

        label.setFont(new Font(20));
        mainPane.setTop(label);

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
        map = new Map("files/Festival Planner Normal Version V.4.json");
        ArrayList<Podium> podiums = agenda.getPodiumList();
        targets = map.getSpectatorTargets();
        for (int i = 0; i < podiums.size(); i++){
            podia.put(podiums.get(i), targets.get(i + 4));
        }

        entranceAndExitTargets = map.getEntranceAndExitTargets();

        npcs = new ArrayList<>();
        Point2D newPosition = new Point2D.Double(500, 700);
        boolean hasCollision = false;
        for (NPC visitor : npcs) {
            if (visitor.getPosition().distance(newPosition) < 32) {
                hasCollision = true;
            }
        }
        if (!hasCollision) {
            npcs.add(new NPC(newPosition, 0, entranceAndExitTargets.get(0)));
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
        for (NPC visitor : npcs) {
            visitor.update(this.npcs, hours, minutes);
        }

        //fixme
        Point2D newPosition = new Point2D.Double(500, 700);
        boolean hasCollision = false;
        for (NPC visitor : npcs) {
            if (visitor.getPosition().distance(newPosition) < 64) {
                hasCollision = true;
            }
        }
        if (!hasCollision) {
            if (npcs.size() < 100) {//fixme
                npcs.add(new NPC(newPosition, 0, targets.get(5)));
            }
        }


        if (deltaTime > 0.01) {
            seconds += 5;
            if (seconds > 60) {
                minutes++;
                seconds = 0;
                if (minutes > 59) {
                    hours++;
                    minutes = 0;
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
        setNpcTarget();
//        System.out.println(npcs.size());
    }

    private Random r = new Random();

    public void setNpcTarget() {
        String minutes = String.valueOf(this.minutes);
        if (minutes.length() < 2) {
            minutes = "0" + minutes;
        }
        ArrayList<Performance> performances = this.agenda.getLivePerformances(String.valueOf(hours), minutes);
//        ArrayList<NPC> notBusyList = new ArrayList<>();
//        Collections.sort(performances);
//        for (NPC npc : npcs) {
//            if (!npc.isBusy()) {
//                notBusyList.add(npc);
//            }
//        }
//
//        for (int i = 0; i < notBusyList.size(); i++) {
//            if (performances.isEmpty()) {
//                notBusyList.get(i).setTarget(targets.get(2), hours, minutes, true);
//            } else {
//                if (i < notBusyList.size() / 10) {
//                    notBusyList.get(i).setTarget(targets.get(2), hours, minutes, true);
//                } else if (i < notBusyList.size() / performances.size()) {
//                    notBusyList.get(i).setTarget(podia.get(performances.get(0).getPodium()), hours, minutes, false);
//                } else {
//                    notBusyList.get(i).setTarget(podia.get(performances.get(1).getPodium()), hours, minutes, false);
//                }
//            }
//        }

//        for (Performance performance: performances){
        if (performances.isEmpty()) {
            for (NPC npc : npcs) {
                npc.setTarget(targets.get(r.nextInt(4)));
            }
            System.out.println(targets.get(2));
        } else {
            for (NPC npc : npcs) {
                npc.setTarget(podia.get(performances.get(0).getPodium()), String.valueOf(hours), minutes);
            }
        System.out.println(podia.get(performances.get(0).getPodium()));
        }
    }


    public Tab getTab() {
        Tab t = new Tab("Simulatie");
        t.setContent(mainPane);
        return t;
    }
}
