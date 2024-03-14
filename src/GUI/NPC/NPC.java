package GUI.NPC;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class NPC
{
    private Point2D position;
    private double angle;
    private double speed;
    private BufferedImage[] image;

    private Point2D targetPosition;

    public NPC(Point2D position, double angle)
    {
        this.position = position;
        this.angle = angle;
        this.speed = 2;


            try {
                BufferedImage image1 =ImageIO.read(getClass().getResourceAsStream("NPC sprites.png"));
                image = new BufferedImage[3];

                for (int i = 0; i < 3; i++) {
                    image[i] = image1.getSubimage(0,34 * i,34,34);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        this.targetPosition = new Point2D.Double(Math.random()*1000, Math.random()*1000);
    }

    public void update(ArrayList<NPC> npcs)
    {
        double newAngle = Math.atan2(this.targetPosition.getY() - this.position.getY(), this.targetPosition.getX() - this.position.getX());

        double angleDifference = angle - newAngle;
        while(angleDifference > Math.PI)
            angleDifference -= 2 * Math.PI;
        while(angleDifference < -Math.PI)
            angleDifference += 2 * Math.PI;

        if(angleDifference < -0.1)
            angle += 0.1;
        else if(angleDifference > 0.1)
            angle -= 0.1;
        else
            angle = newAngle;

        Point2D newPosition = new Point2D.Double(
                this.position.getX() + speed * Math.cos(angle),
                this.position.getY() + speed * Math.sin(angle)
        );

        boolean hasCollision = false;

        for (NPC visitor : npcs) {
            if(visitor != this)
                if(visitor.position.distance(newPosition) <= 32)
                    hasCollision = true;
        }

        if(!hasCollision)
            this.position = newPosition;
        else
            this.angle += 0.2;
    }


    public void draw(Graphics2D g2d)
    {

        AffineTransform tx = new AffineTransform();

        int frame = ((((int)(position.getX() / 50)) % image.length) + (((int)( position.getY() / 50) % image.length)) / 2);
        System.out.println(frame);

        tx.translate(position.getX() - image[frame].getWidth()/2, position.getY() - image[frame].getHeight() / 2);
//        tx.scale(.5,.25);
        tx.rotate(angle, image[frame].getWidth()/2, image[frame].getHeight()/2);


        g2d.drawImage(image[frame], tx, null);

        g2d.setColor(Color.RED);
        g2d.fill(new Ellipse2D.Double(position.getX()-16, position.getY()-24, 1, 1));

    }

    public void setTargetPosition(Point2D targetPosition)
    {
        this.targetPosition = targetPosition;
    }

    public Point2D getPosition()
    {
        return this.position;
    }
}
