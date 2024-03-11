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

    private final int tileHeight = 32;
    private final int tileWidth = 32;
    private ArrayList<BufferedImage> victorianMarketTiles = new ArrayList<>();
    private ArrayList<BufferedImage> victorianStreetsTiles = new ArrayList<>();
    private ArrayList<BufferedImage> woodTiles = new ArrayList<>();
    private int layer1;
    private int layer2;
    private int layer3;
    private int layer4;
    private int layer5;
    private int layer6;
    private int layer7;
    private int layer8;
    private ArrayList<Integer> layers;

    public Map(String filename) {
        JsonReader reader = null;
        reader = Json.createReader(getClass().getResourceAsStream(filename));
        JsonObject root = reader.readObject();

        this.width = root.getInt("width");
        this.height = root.getInt("height");

        this.layers = new ArrayList<>();
        addLayers();

        loadTilesets(root);

        for (int i = 0; i < layers.size(); i++) {
            int layer = layers.get(i);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    layer = root.getJsonArray("layers").getJsonObject(i).getJsonArray("data").getInt(x + y);
                }
            }
        }
    }

    private void addLayers() {
//        layer1 = new int[height][width];
//        layer2 = new int[height][width];
//        layer3 = new int[height][width];
//        layer4 = new int[height][width];
//        layer5 = new int[height][width];
//        layer6 = new int[height][width];
//        layer7 = new int[height][width];
//        layer8 = new int[height][width];

        this.layers.add(layer1);
        this.layers.add(layer2);
        this.layers.add(layer3);
        this.layers.add(layer4);
        this.layers.add(layer5);
        this.layers.add(layer6);
        this.layers.add(layer7);
        this.layers.add(layer8);
    }

    public void draw(Graphics2D g2d) {
        for (int layer : layers) {
            ArrayList<BufferedImage> usedTileset;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {

                    int tile = x + y;

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
                    g2d.drawImage(
                            usedTileset.get(tile),
                            AffineTransform.getTranslateInstance(x * tileWidth, y * tileHeight),
                            null
                    );
                }
            }
        }
    }

    private void loadTilesets(JsonObject root) {

        try {
            BufferedImage tilemap = ImageIO.read(getClass().getResourceAsStream(root.getJsonArray("tilesets").getJsonObject(0).getString("image")));
//            BufferedImage tilemap = ImageIO.read(getClass().getResourceAsStream("victorian-market.png"));


            for (int y = 0; y < tilemap.getHeight(); y += tileHeight) {
                for (int x = 0; x < tilemap.getWidth(); x += tileWidth) {
                    victorianMarketTiles.add(tilemap.getSubimage(x, y, tileWidth, tileHeight));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedImage tilemap = ImageIO.read(getClass().getResourceAsStream(root.getJsonArray("tilesets").getJsonObject(1).getString("image")));

            for (int y = 0; y < tilemap.getHeight(); y += tileHeight) {
                for (int x = 0; x < tilemap.getWidth(); x += tileWidth) {
                    victorianStreetsTiles.add(tilemap.getSubimage(x, y, tileWidth, tileHeight));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedImage tilemap = ImageIO.read(getClass().getResourceAsStream(root.getJsonArray("tilesets").getJsonObject(2).getString("image")));

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
