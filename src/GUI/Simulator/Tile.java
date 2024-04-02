package GUI.Simulator;


import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Tile {
    private int x;
    private int y;
    private BufferedImage image;
    private boolean isChecked;

    public Tile(int x, int y, BufferedImage image) {
        this.x = x;
        this.y = y;
        this.image = image;
    }
    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        this.isChecked = false;
    }

    public int getX() {
        return x * 32;
    }

    public int getY() {
        return y * 32;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void draw(Graphics2D g2d) {
        g2d.drawImage(image, AffineTransform.getTranslateInstance(x * 32, y * 32), null);
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
