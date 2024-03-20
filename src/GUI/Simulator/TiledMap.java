package GUI.Simulator;

import GUI.NPC.NPC;
import javafx.animation.AnimationTimer;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class TiledMap {

    private Map map;
    private ResizableCanvas canvas;
    private BorderPane mainPane;
    private FXGraphics2D g2d;
    private Camera camera;
    private ArrayList<NPC> npcs;

    public TiledMap() throws Exception {
        init();
        mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        this.g2d = new FXGraphics2D(canvas.getGraphicsContext2D());
        this.camera = new Camera(canvas, g -> draw(g), g2d);
        g2d.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        new AnimationTimer() {
            long last = -1;
            @Override
            public void handle(long now) {
                if(last == -1)
                    last = now;
                update((now - last) / 1000000000.0);
                last = now;
                draw(g2d);
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

        map = new Map("Festival Planner Normal Version V.2.json");
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
