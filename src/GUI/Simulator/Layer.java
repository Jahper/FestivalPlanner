package GUI.Simulator;

import java.awt.*;
import java.util.ArrayList;

public class Layer {
    private ArrayList<Tile> tiles = new ArrayList<>();
    private int[] tileData;
    private ArrayList<Tileset> tileSets;
    private Tileset usedTileSet;
    private int correction;

    public Layer(int[] tileData, ArrayList<Tileset> tileset) {
        this.tileData = tileData;
        this.tileSets = tileset;

        getTiles();
    }

    private void getTiles() {
        setTileSet(0);

        for (int i = 0; i < tileData.length; i++) {
            int tile = tileData[i];

            setTileSet(tile);

            if (tile <= 0) {
                continue;
            }

            int x = i % 100;

            int y = (i - x) / 100;

            tiles.add(new Tile(x, y, usedTileSet.getTileset().get((tile - correction))));
        }
    }

    private void setTileSet(int tile) {
        if (tile <= 0) {
            return;
        }

        for (Tileset tileset : tileSets) {
            if (tileset.usesTileset(tile)) {
                this.usedTileSet = tileset;
                this.correction = tileset.getOffset();
                return;
            }
        }
    }

    public void draw(Graphics2D g2d) {
        for (Tile tile : tiles) {
            tile.draw(g2d);
        }
    }
}
