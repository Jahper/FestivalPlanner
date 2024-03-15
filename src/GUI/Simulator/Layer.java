package GUI.Simulator;

import org.jfree.fx.FXGraphics2D;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Layer {
    private ArrayList<Tile> tiles;
    private int[] tileData;
    private ArrayList<ArrayList<BufferedImage>> tilesets;//todo
    private ArrayList<BufferedImage> usedTileSet = new ArrayList<>();
    private int correction;

    public Layer(int[] tileData, ArrayList<ArrayList<BufferedImage>> tileset) {
        this.tileData = tileData;
        this.tilesets = tileset;

        getTiles();
    }

    private void getTiles() {
        setTileSet();

        for (int i = 0; i < tileData.length; i++) {
            int tile = tileData[i];

            int x = i % 100;

            int y = (i - x) / 100;

            tiles.add(new Tile(x,y, usedTileSet.get(tile - correction)));

        }
    }

    private void setTileSet() {
        this.correction = 1;
        this.usedTileSet = tilesets.get(0);

        for (int i = 0; i < tileData.length; i++) {

            int tile = tileData[i];

            if (tile <= 0) {
                continue;
            } else if (tile > 2817) {
                this.correction = 2817;
                this.usedTileSet = tilesets.get(1);
                break;
            } else if (tile > 1537) {
                this.correction = 1537;
                this.usedTileSet = tilesets.get(2);
                break;
            }
        }
    }

    public void draw(FXGraphics2D g2d) {
        for (Tile tile : tiles) {
            tile.draw(g2d);
        }
    }
}
