package GUI.Simulator;

import javax.imageio.ImageIO;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Map {
    private int width;
    private int height;

    private int tileHeight = 32;
    private int tileWidth = 32;
    private ArrayList<BufferedImage> victorianMarketTiles = new ArrayList<>();
    private ArrayList<BufferedImage> victorianStreetsTiles = new ArrayList<>();
    private ArrayList<BufferedImage> woodTiles = new ArrayList<>();
    private ArrayList<int[]> layers;

    public Map(String filename) {
        JsonReader reader = null;
        reader = Json.createReader(getClass().getResourceAsStream(filename));
        JsonObject root = reader.readObject();

        this.width = root.getInt("width");
        this.height = root.getInt("height");

        this.layers = new ArrayList<>();
        addLayers();

        loadTilesets(root);

        for (int i = 0; i < root.getJsonArray("layers").size(); i++) {
            int[] layer = new int[height * width];
            for (int j = 0; j < height * width; j++) {
                layer[j] = root.getJsonArray("layers").getJsonObject(i).getJsonArray("data").getInt(j);
            }
            layers.add(layer);
        }
    }

    private void addLayers() {

    }

    public void draw(Graphics2D g2d) {
        for (int[] layer : layers) {
            ArrayList<BufferedImage> usedTileset;
            for (int i = 0; i < height * width; i++) {

                int tile = layer[i];

                if (tile <= 0) {
                    continue;
                } else if (tile > 2817) {
                    usedTileset = woodTiles;
                    tile -= 2817;
                } else if (tile > 1537) {
                    usedTileset = victorianStreetsTiles;
                    tile -= 1537;
                } else {
                    usedTileset = victorianMarketTiles;
                }

                int x = i % 100;

                int y = 0;
                y = (i - x) / 100;
                System.out.println("x: " + x);
                System.out.println("y: " + y);
                g2d.drawImage(
                        usedTileset.get(tile),
                        AffineTransform.getTranslateInstance(x * tileWidth, y * tileHeight),
                        null
                );
            }
        }
    }

    private void loadTilesets(JsonObject root) {
        try {
            BufferedImage tilemap = ImageIO.read(getClass().getResourceAsStream(root.getJsonArray("tilesets").getJsonObject(0).getString("name") + ".png"));

            for (int y = 0; y < tilemap.getHeight(); y += tileHeight) {
                for (int x = 0; x < tilemap.getWidth(); x += tileWidth) {
                    victorianMarketTiles.add(tilemap.getSubimage(x, y, tileWidth, tileHeight));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedImage tilemap = ImageIO.read(getClass().getResourceAsStream(root.getJsonArray("tilesets").getJsonObject(1).getString("name") + ".png"));

            for (int y = 0; y < tilemap.getHeight(); y += tileHeight) {
                for (int x = 0; x < tilemap.getWidth(); x += tileWidth) {
                    victorianStreetsTiles.add(tilemap.getSubimage(x, y, tileWidth, tileHeight));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedImage tilemap = ImageIO.read(getClass().getResourceAsStream(root.getJsonArray("tilesets").getJsonObject(2).getString("name") + ".png"));

            for (int y = 0; y < tilemap.getHeight(); y += tileHeight) {
                for (int x = 0; x < tilemap.getWidth(); x += tileWidth) {
                    woodTiles.add(tilemap.getSubimage(x, y, tileWidth, tileHeight));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
