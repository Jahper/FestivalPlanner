package GUI.Simulator;

import GUI.NPC.NPC;
import javafx.animation.AnimationTimer;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class TiledMap {

    private Map map;
    private ResizableCanvas canvas;
    private BorderPane mainPane;
    private ArrayList<NPC> npcs;

    public TiledMap() throws Exception {
        init();
        mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());
        FXGraphics2D g2d2 = new FXGraphics2D(canvas.getGraphicsContext2D());

        new AnimationTimer() {
            long last = -1;
            @Override
            public void handle(long now) {
                if(last == -1)
                    last = now;
                update((now - last) / 1000000000.0);
                last = now;
                drawNpc(g2d2);
//                draw(g2d);
            }
        }.start();

        draw(g2d);

        canvas.setOnMouseMoved(event -> {
            for (NPC visitor : npcs) {
                visitor.setTargetPosition(new Point2D.Double(event.getX(), event.getY()));
            }
        });
    }


    public void init() {
        npcs = new ArrayList<>();
        map = new Map("/GUI/Simulator/Festival_Planner_Lite_Version.json");

        while(npcs.size() < 20) {
            Point2D newPosition = new Point2D.Double(Math.random()*1000, Math.random()*1000);
            boolean hasCollision = false;
            for (NPC visitor : npcs) {
                if(visitor.getPosition().distance(newPosition) < 64)
                    hasCollision = true;
            }
            if(!hasCollision)
                npcs.add(new NPC(newPosition, 0));
        }

    }


    public void draw(Graphics2D g) {
        g.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
        map.draw(g);
        AffineTransform tx = new AffineTransform();
        double zoom = canvas.getWidth() / 6000;
        tx.scale(zoom, zoom);
        g.setTransform(tx);

//        g.setTransform(new AffineTransform());
//        for (NPC visitor : npcs) {
//            visitor.draw(g);
//        }
    }

    public void drawNpc(Graphics2D g){
//        g.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        AffineTransform tx = new AffineTransform();
        double zoom = canvas.getWidth() / 3000;
        tx.scale(.53 , 1);
        g.setTransform(tx);

        for (NPC visitor : npcs) {
            visitor.draw(g);
        }
    }

    public void update(double deltaTime) {
        for (NPC visitor : npcs) {
            visitor.update(this.npcs);
        }
    }

    public Tab getTab() {
        Tab t = new Tab("Simulatie");
        BorderPane b = new BorderPane();
        b.setCenter(this.canvas);
        t.setContent(b);
        return t;
    }
}
