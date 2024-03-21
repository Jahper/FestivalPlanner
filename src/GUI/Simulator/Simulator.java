package GUI.Simulator;

import GUI.Simulator.NPC.NPC;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
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

public class Simulator {

    private Map map;
    private ResizableCanvas canvas;
    private BorderPane mainPane;
    private FXGraphics2D g2d;
    private Camera camera;
    private ArrayList<NPC> npcs;
    private ArrayList<Target> targets;
    private int seconds = 0;
    private int minutes = 0;
    private int hours = 0;
//    private String clock = "test";
    private Label label = new Label("");
    private HBox hBox = new HBox();
    private Slider slider = new Slider();

    private double sliderValue=0;

   // Slider slider = new Slider(0.0,100,0.0);


    public Simulator() throws Exception {
        init();


        slider.setMin(0);
        slider.setMax(24);
        slider.setValue(sliderValue);
        slider.setMinWidth(1500);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setBlockIncrement(23);


        mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);

        label.setFont(new Font(20));

        hBox.setPadding(new Insets(10));
        hBox.setSpacing(10);

        hBox.getChildren().add(label);
        hBox.getChildren().add(slider);

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
        map = new Map("files/Festival Planner Normal Version V.2.json");

        targets = map.getSpectatorTargets();

        npcs = new ArrayList<>();

        Point2D newPosition = new Point2D.Double(500, 700);
        boolean hasCollision = false;
        for (NPC visitor : npcs) {
            if (visitor.getPosition().distance(newPosition) < 64)
                hasCollision = true;
        }
        if (!hasCollision) {
            npcs.add(new NPC(newPosition, 0, targets.get(9)));
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
            visitor.update(this.npcs);
        }


        if (deltaTime > 0.01){
            seconds+= 5;
            if (seconds> 60){
                minutes++;
                seconds = 0;
                if (minutes > 59){
                    hours++;
                    minutes = 0;
                    if (hours > 23){
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


        slider.setValue(hours+((double) minutes /60));
    }


    public Tab getTab() {
        Tab t = new Tab("Simulatie");
        t.setContent(mainPane);
        return t;
    }
}
