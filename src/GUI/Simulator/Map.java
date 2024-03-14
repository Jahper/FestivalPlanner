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
    private ArrayList<Layer> layers;
    ArrayList<BufferedImage> layerImages = new ArrayList<>();
    ArrayList<Tileset> tilesets = new ArrayList<>();

    public Map(String filename) {
        JsonReader reader = null;
        reader = Json.createReader(getClass().getResourceAsStream(filename));
        JsonObject root = reader.readObject();

        this.width = root.getInt("width");
        this.height = root.getInt("height");

        this.layers = new ArrayList<>();


        loadTilesets(root);

        addLayers(root);

        //adding all layers

    }

    private void addLayers(JsonObject root) {
        for (int i = 0; i < root.getJsonArray("layers").size(); i++) {
            layers.add(new Layer(root, i, tilesets));
        }
    }

    BufferedImage cacheImage = null;

    public void draw(Graphics2D g2d) {
        //todo ophalen uit json en in klasse zetten
        if (cacheImage == null) {
            cacheImage = new BufferedImage(width * 32, height * 32, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D graphics = cacheImage.createGraphics();

            ArrayList<Integer> startIDs = new ArrayList<>();

            for (Tileset tileset : tilesets) {
                startIDs.add(tileset.getFirstID());
            }

            for (Layer layer : layers) {
                ArrayList<BufferedImage> usedTileset;
                for (int i = 0; i < height * width; i++) {
//                    Tileset tileset = layer.getTileset();
//                    Tileset tileset = null;
                    int tile = layer.getLayer()[i];

//                    if (tile <= 0) {
//                        continue;
//                    }
//                    for (int j = 0; j < startIDs.size(); j++) {
//                        int id = startIDs.get(j);
//
////                        if (tile <= 0) {
////                            continue;
////                        }
//
//                        if (tile > id) {
//                            tile -= id;
//                            tileset = tilesets.get(startIDs.indexOf(id));
//                            break;
//                        }
//                    }

                    if (tile <= 0) {
                        continue;
                    } else if (tile > 2817) {
                        usedTileset = woodTiles;
                    tile -= 2817;
                    } else if (tile > 1537) {
                        usedTileset = victorianMarketTiles;
                    tile -= 1537;
                    } else {
                        usedTileset = victorianStreetsTiles;
                    tile -= 1;
                    }
                    int x = i % 100;

                    int y;
                    y = (i - x) / 100;

                    Tileset t = layer.getTileset();


                    BufferedImage image = usedTileset.get(tile);
                    graphics.drawImage(image,
                            AffineTransform.getTranslateInstance(x * tileWidth, y * tileHeight),
                            null
                    );
                }
            }
        } else {
            g2d.drawImage(cacheImage, null, null);
        }
    }

    private void loadTilesets(JsonObject root) {
        //todo in for loop zetten en in lijst.
        for (int i = 0; i < root.getJsonArray("tilesets").size(); i++) {
            String imageName = root.getJsonArray("tilesets").getJsonObject(i).getString("name");
            int beginID = root.getJsonArray("tilesets").getJsonObject(i).getInt("firstgid");
            tilesets.add(new Tileset(imageName, beginID));
        }
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
