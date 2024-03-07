package GUI.Simulator;

import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class TiledMap {

    private Map map;
    private ResizableCanvas canvas;
    private BorderPane mainPane;

    public TiledMap() throws Exception {
        init();
        mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());
        draw(g2d);
    }


    public void init() {
        map = new Map("/GUI/Simulator/Festival_Planner_Lite_Version.json");

    }


    public void draw(Graphics2D g) {
        g.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
        map.draw(g);
        AffineTransform tx = new AffineTransform();
        double zoom = canvas.getWidth() / 6000;
        tx.scale(zoom, zoom);
        g.setTransform(tx);
    }

    public void update(double deltaTime) {


    }

    public Tab getTab() {
        Tab t = new Tab("Simulatie");
        BorderPane b = new BorderPane();
        b.setCenter(this.canvas);
        t.setContent(b);
        return t;
    }
}
