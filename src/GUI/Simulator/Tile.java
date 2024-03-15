package GUI.Simulator;


import org.jfree.fx.FXGraphics2D;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Tile {
    private int x;
    private int y;
    private BufferedImage image;

    public Tile(int x, int y, BufferedImage image) {
        this.x = x;
        this.y = y;
        this.image = image;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void draw(FXGraphics2D g2d){
        g2d.drawImage(image, AffineTransform.getTranslateInstance(x * 32, y * 32), null);
    }
}
