package GUI.Simulator;


import sun.misc.Queue;

import java.awt.*;
import java.util.ArrayList;

public class Layer {
    private ArrayList<Tile> tiles = new ArrayList<>();
    private int[] tileData;
    private ArrayList<Tileset> tilesets;
    private Tileset usedTileSet;
    private int correction;

    public Layer(int[] tileData, ArrayList<Tileset> tileset) {
        this.tileData = tileData;
        this.tilesets = tileset;

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

        for (Tileset tileset : tilesets) {
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
//fixme
    public void draw0(Graphics2D graphics2D, int targetX, int targetY) {
        Queue<Tile> toCheck = new Queue<>();
        ArrayList<Tile> checked = new ArrayList<>();

        Tile currentTile = new Tile(targetX, targetY);
//        checked.add(currentTile);
        toCheck.enqueue(currentTile);
        int distance = 0;
        graphics2D.drawString(String.valueOf(distance), targetX * 32, targetY * 32);

        while (!toCheck.isEmpty()) {
            boolean added = false;
            Tile tile = null;
            for (int j = 0; j < 4; j++) {
                for (int i = 0; i < tiles.size(); i++) {
                    tile = tiles.get(i);

                    if (tile.getX() == currentTile.getX() - 32 && tile.getY() == currentTile.getY() && !checked.contains(tile)) {
                        graphics2D.drawString(String.valueOf(distance + 1), tile.getX(), tile.getY());
                        checked.add(tile);
                        toCheck.enqueue(tile);
                        added = true;
                    }
                    if (tile.getX() == currentTile.getX() + 32 && tile.getY() == currentTile.getY() && !checked.contains(tile)) {
                        graphics2D.drawString(String.valueOf(distance + 1), tile.getX(), tile.getY());
                        checked.add(tile);
                        toCheck.enqueue(tile);
                        added = true;
                    }
                    if (tile.getX() == currentTile.getX() && tile.getY() == currentTile.getY() - 32 && !checked.contains(tile)) {
                        graphics2D.drawString(String.valueOf(distance + 1), tile.getX(), tile.getY());
                        checked.add(tile);
                        toCheck.enqueue(tile);
                        added = true;
                    }
                    if (tile.getX() == currentTile.getX() && tile.getY() == currentTile.getY() + 32 && !checked.contains(tile)) {
                        graphics2D.drawString(String.valueOf(distance + 1), tile.getX(), tile.getY());
                        checked.add(tile);
                        toCheck.enqueue(tile);
                        added = true;
                    }
                }
            }
            if (added) {
                currentTile = tile;
                distance++;
            }
        }
    }

    private void checkNeighbor(int x, int y) {

    }
}
