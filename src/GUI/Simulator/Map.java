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
    private ArrayList<int[]> layers = new ArrayList<>();

    public Map(String filename) {
        JsonReader reader = null;
        reader = Json.createReader(getClass().getResourceAsStream(filename));
        JsonObject root = reader.readObject();

        this.width = root.getInt("width");
        this.height = root.getInt("height");


        loadTilesets(root);//todo meegeven voor layer

        int height = root.getInt("width");
        int width = root.getInt("height");

        for (int i = 0; i < root.getJsonArray("layers").size(); i++) {
            int layer[] = new int[height * width];
            for (int j = 0; j < 100 * 100; j++) {
                layer[j] = root.getJsonArray("layers").getJsonObject(i).getJsonArray("data").getInt(j);
            }
            layers.add(layer);
        }

//        addLayers(root);

    }

    private void addLayers(JsonObject root) {
//        for (int i = 0; i < root.getJsonArray("layers").size(); i++) {
//            layers.add(new Layer(root, i, tilesets));
//        }
    }

    BufferedImage cacheImage = null;

    public void draw(Graphics2D g2d) {
        //todo ophalen uit json en in klasse zetten
        if (cacheImage == null) {
            cacheImage = new BufferedImage(width * 32, height * 32, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D graphics = cacheImage.createGraphics();

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
                        tile -= 1;
                    }

                    int x = i % 100;

                    int y = (i - x) / 100;

                    graphics.drawImage(usedTileset.get(tile),
                            AffineTransform.getTranslateInstance(x * tileWidth, y * tileHeight),
                            null
                    );
                }
            }

        } else {
            g2d.drawImage(cacheImage, null, null);
        }
    }

    private ArrayList<ArrayList<BufferedImage>> loadTilesets(JsonObject root) {
        //todo in for loop zetten en in lijst.
//        for (int i = 0; i < root.getJsonArray("tilesets").size(); i++) {
//            String imageName = root.getJsonArray("tilesets").getJsonObject(i).getString("name");
//            int beginID = root.getJsonArray("tilesets").getJsonObject(i).getInt("firstgid");
//            tilesets.add(new Tileset(imageName, beginID));
//        }
        ArrayList<ArrayList<BufferedImage>> tilesets = new ArrayList<>();

        try {
            BufferedImage tilemap = ImageIO.read(getClass().getResourceAsStream(root.getJsonArray("tilesets").getJsonObject(0).getString("name") + ".png"));

            for (int y = 0; y < tilemap.getHeight(); y += tileHeight) {
                for (int x = 0; x < tilemap.getWidth(); x += tileWidth) {
                    victorianMarketTiles.add(tilemap.getSubimage(x, y, tileWidth, tileHeight));
                }
            }
            tilesets.add(victorianMarketTiles);
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
            tilesets.add(victorianStreetsTiles);
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
            tilesets.add(woodTiles);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tilesets;
    }
}
