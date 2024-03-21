package GUI.Simulator.NPC;

import GUI.Simulator.Pathfinding.Node;
import GUI.Simulator.Target;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class NPC {
    private Point2D position;
    private double angle;
    private Boolean isDancing;
    private Target target;
    private double speed;
    private BufferedImage[] imageWalking;
    private BufferedImage[] imageDancing;
    private BufferedImage[] finalImage;

    private Point2D targetPosition;
    //todo volgorde van loop sprites aanpassen voor animatie
    //todo dansen laten werken

    public NPC(Point2D position, double angle, Target target) {
        this.position = position;
        this.angle = angle;
        this.target = target;
        this.isDancing = false;
        this.speed = 2;

        Random r = new Random();

        try {
            BufferedImage image1 = ImageIO.read(getClass().getResourceAsStream("NPC sprites.png"));
            imageWalking = new BufferedImage[3];
            imageDancing = new BufferedImage[3];

            if (r.nextInt(2) == 1) {
                for (int i = 0; i < 3; i++) {
                    imageWalking[i] = image1.getSubimage((34 * i) + 15, 14, 34, 34);
                }

                for (int i = 0; i < 3; i++) {
                    imageDancing[i] = image1.getSubimage((34 * (i + 3)) + 15, 14, 34, 34);
                }
            } else {
                for (int i = 0; i < 3; i++) {
                    imageWalking[i] = image1.getSubimage((34 * i) + 15, 61, 34, 34);
                }
                for (int i = 0; i < 3; i++) {
                    imageDancing[i] = image1.getSubimage((34 * (i + 3)) + 15, 61, 34, 34);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.finalImage = this.imageWalking;

        this.targetPosition = position;
    }

    public void update(ArrayList<NPC> npcs) {
        //target route
        int x = (int) position.getX() / 32;
        int y = (int) position.getY() / 32;

        Node node = target.getGraph().getNodes()[x][y];
        if (node.getDistance() == 0) {
            targetPosition = new Point2D.Double(position.getX(), position.getY());
            //todo laten dansen fzo
            startDancing();
            return;
        } else {
            stopDancing();
        }
        Node nearest = node.getNearestNode();
        if (!(node.getX() == nearest.getX())) {
            this.targetPosition = new Point2D.Double(node.getX() + nearest.getX(), node.getY());
            if (node.getX() > nearest.getX()) {
                this.targetPosition = new Point2D.Double(nearest.getX() - node.getX(), node.getY());
            }
        } else if (!(node.getY() == nearest.getY())) {
            this.targetPosition = new Point2D.Double(node.getX(), nearest.getY() + node.getY());
            if (node.getY() > nearest.getY()) {
                this.targetPosition = new Point2D.Double(node.getX(), nearest.getY() - node.getY());
            }
        }

        double newAngle = Math.atan2(this.targetPosition.getY() - this.position.getY(), this.targetPosition.getX() - this.position.getX());
        double angleDifference = angle - newAngle;

        while (angleDifference > Math.PI) {
            angleDifference -= 2 * Math.PI;
        }
        while (angleDifference < -Math.PI) {
            angleDifference += 2 * Math.PI;
        }

        if (angleDifference < -0.1) {
            angle += 0.1;
        } else if (angleDifference > 0.1) {
            angle -= 0.1;
        } else {
            angle = newAngle;
        }

        Point2D newPosition = new Point2D.Double(
                this.position.getX() + speed * Math.cos(angle),
                this.position.getY() + speed * Math.sin(angle)
        );

        boolean hasCollision = false;

        for (NPC visitor : npcs) {
            if (visitor != this) {
                if (visitor.position.distance(newPosition) <= 32) {
                    hasCollision = true;
                }
            }
        }

        if (!hasCollision) {
            this.position = newPosition;
        } else {
            this.angle += 0.2;
        }
    }

    public void startDancing() {
        this.finalImage = this.imageDancing;
    }

    public void stopDancing() {
        this.finalImage = this.imageWalking;
    }

    public void draw(Graphics2D g2d) {

        AffineTransform tx = new AffineTransform();

        int frame = (int) ((position.getX() + position.getY()) / 50) % 3;
        tx.translate(position.getX() - finalImage[frame].getWidth() / 2, position.getY() - finalImage[frame].getHeight() / 2);
        tx.rotate(angle, finalImage[frame].getWidth() / 2, finalImage[frame].getHeight() / 2);

        g2d.drawImage(imageWalking[frame], tx, null);

        g2d.setColor(Color.BLACK);
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public void setTargetPosition(Point2D targetPosition) {
        this.targetPosition = targetPosition;
    }

    public Point2D getPosition() {
        return this.position;
    }
}
