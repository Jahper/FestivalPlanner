package GUI.Simulator;

import java.awt.image.BufferedImage;

public class Tile {
    private BufferedImage image;
    private Tileset tileset;
    private int index;

    public Tile(Tileset tileset, int index) {
        this.tileset = tileset;
        this.index = index;
    }

    public BufferedImage getImage() {
        int correctedIndex = index - tileset.getFirstID();
        return tileset.getTile(correctedIndex);
    }
}
