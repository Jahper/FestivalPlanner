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
    private boolean isBusy;
    private boolean isResting;
    private Boolean isDancing;
    private Target target;
    private double speed;
    private BufferedImage[] imageWalking;
    private BufferedImage[] imageDancing;
    private BufferedImage[] finalImage;
    private Point2D targetPosition;
    private Point2D lastPosition;
    private boolean hasCollision = false;
    private boolean hasCollisionWithBorder = false;
    private double scale;
    private boolean isArtist;
    public NPC(Point2D position, double angle, Target target, boolean isArtist) {
        this.position = position;
        this.lastPosition = position;
        this.angle = angle;
        this.target = target;
        this.isDancing = true;
        this.speed = 2;
        this.isBusy = false;
        this.isResting = true;
        this.scale =.7;
        this.isArtist = isArtist;

        try {
            BufferedImage image1 = ImageIO.read(getClass().getResourceAsStream("NPC sprites.png"));
            imageWalking = new BufferedImage[4];
            imageDancing = new BufferedImage[3];

            if (isArtist){
                loadCharacter(image1, 84);
            } else {
                Random r = new Random();
                if (r.nextInt(2) == 1) {
                    loadCharacter(image1,14);
                } else {
                    loadCharacter(image1,49);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        stopDancing();
        this.targetPosition = position;
    }

    public NPC update(ArrayList<NPC> npcs, ArrayList<Target> exits) {
        //target route
        int x = (int) position.getX() / 32;
        int y = (int) position.getY() / 32;

        hasCollision = false;
        hasCollisionWithBorder = false;

        Node node = target.getGraph().getNodes()[x][y];
        stopDancing();
        if (node.getDistance() == 0) {
            if (this.target.equals(exits.get(0)) || this.target.equals(exits.get(1))) {
                return this;
            }
            targetPosition = position;
            lastPosition = new Point2D.Double(node.getX(), node.getY());
            startDancing();
        } else if (!node.isCollision()) {
            createTargetPosition(node);
            lastPosition = new Point2D.Double(node.getX(), node.getY());
        } else if (node.getDistance() == -1){
            this.hasCollisionWithBorder = true;
        }

        if (hasCollisionWithBorder) {
            this.targetPosition = lastPosition;
        }

        Point2D newPosition = updatePosition(npcs);

        if (!hasCollision) {
            this.position = newPosition;
        } else {
            angle += 0.2;
        }
        return null;
    }

    private Point2D updatePosition(ArrayList<NPC> npcs) {
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


        for (NPC visitor : npcs) {
            if (visitor != this) {
                if (visitor.position.distance(newPosition) <= 32 * scale) {
                    hasCollision = true;
                }
            }
        }
        return newPosition;
    }

    private void createTargetPosition(Node node) {
        Node nearest = node.getNearestNode();
        if (!(node.getX() == nearest.getX())) {
            this.targetPosition = new Point2D.Double(node.getX() + nearest.getX(), node.getY());
            lastPosition = new Point2D.Double(node.getX(), node.getY());
            if (node.getX() > nearest.getX()) {
                this.targetPosition = new Point2D.Double(nearest.getX() - node.getX(), node.getY());
            }
        } else if (!(node.getY() == nearest.getY())) {
            this.targetPosition = new Point2D.Double(node.getX(), nearest.getY() + node.getY());
            lastPosition = new Point2D.Double(node.getX(), node.getY());
            if (node.getY() > nearest.getY()) {
                this.targetPosition = new Point2D.Double(node.getX(), nearest.getY() - node.getY());
            }
        }
        lastPosition = position;
    }

    private void loadCharacter(BufferedImage image1, int y){
        for (int i = 0; i < 4; i++) {
                imageWalking[i] = image1.getSubimage((35 * i) + 15, y, 34, 34);
        }

        for (int i = 0; i < 3; i++) {
            imageDancing[i] = image1.getSubimage((35 * (i + 4)) + 15, y, 34, 34);
        }
    }

    public void startDancing() {
        finalImage = imageDancing;
    }

    public void stopDancing() {
        finalImage = imageWalking;
    }

    public void draw(Graphics2D g2d) {

        AffineTransform tx = new AffineTransform();

        int frame = (int) ((position.getX() + position.getY()) / 50) % finalImage.length;
        tx.translate(position.getX() - finalImage[frame].getWidth() / 2, position.getY() - finalImage[frame].getHeight() / 2);
        tx.rotate(angle, finalImage[frame].getWidth() / 2, finalImage[frame].getHeight() / 2);
        tx.scale(.7,.7);
        g2d.drawImage(finalImage[frame], tx, null);

        g2d.setColor(Color.BLACK);
    }

    public void setPerformanceTarget(Target target) {
        this.isBusy = true;
        this.isResting = false;
        this.target = target;
    }

    public void setTarget(Target target) {
        if (!this.isResting && !isBusy) {
            this.target = target;
            this.isResting = true;
        }
    }

    public void emergencyExit(ArrayList<Target> exits) {
        int x = (int) position.getX() / 32;
        int y = (int) position.getY() / 32;

        Node exit1 = exits.get(0).getGraph().getNodes()[x][y];
        Node exit2 = exits.get(1).getGraph().getNodes()[x][y];

        if (exit1.getDistance() < exit2.getDistance()) {
            this.target = exits.get(0);
        } else {
            this.target = exits.get(1);
        }

        isBusy = true;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setTargetPosition(Point2D targetPosition) {
        this.targetPosition = targetPosition;
    }

    public Point2D getPosition() {
        return this.position;
    }

    public boolean isBusy() {
        return isBusy;
    }

    public void setIsBusy(boolean isBusy) {
        this.isBusy = isBusy;
    }
}
