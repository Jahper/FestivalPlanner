package GUI.Simulator;

import org.jfree.fx.FXGraphics2D;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Map {
    private int width;
    private int height;
    private ArrayList<Layer> allLayers = new ArrayList<>();
    private ArrayList<Tileset> allTilesets = new ArrayList<>();
    private Layer paths;

    public Map(String filename) {
        JsonReader reader = null;
        reader = Json.createReader(getClass().getResourceAsStream(filename));
        JsonObject root = reader.readObject();

        this.width = root.getInt("width");
        this.height = root.getInt("height");

        loadTilesets(root);
        addLayers(root);
    }

    private void addLayers(JsonObject root) {
        //todo object layers onderscheiden
        for (int i = 0; i < root.getJsonArray("layers").size(); i++) {
            //todo ipv skippen van objecten deze oa gebruiken voor de targets
            String checkForObject = root.getJsonArray("layers").getJsonObject(i).getString("name");
            if (checkForObject.equals("Spectators") || checkForObject.equals("Podium")) {
                continue;
            }

            int layer[] = new int[height * width];
            for (int j = 0; j < 100 * 100; j++) {
                layer[j] = root.getJsonArray("layers").getJsonObject(i).getJsonArray("data").getInt(j);
            }
            allLayers.add(new Layer(layer, allTilesets));
            if (checkForObject.equals("Pathing")){
            paths = new Layer(layer,allTilesets);}
        }
    }

    BufferedImage cacheImage = null;

    public void draw(FXGraphics2D g2d) {
        if (cacheImage == null) {
            cacheImage = new BufferedImage(width * 32, height * 32, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D graphics = cacheImage.createGraphics();

            for (Layer layer : allLayers) {
                layer.draw(graphics);
            }


        } else {
            g2d.drawImage(cacheImage, null, null);
            paths.draw0(g2d, 20,20);
        }

    }

    private void loadTilesets(JsonObject root) {
        for (int i = 0; i < root.getJsonArray("tilesets").size(); i++) {
            allTilesets.add(new Tileset(root, i));
        }
    }
}
