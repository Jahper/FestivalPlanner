package GUI.Simulator;

import org.jfree.fx.FXGraphics2D;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

public class Map {
    private int width;
    private int height;
    private ArrayList<Layer> allLayers = new ArrayList<>();
    private ArrayList<Tileset> allTilesets = new ArrayList<>();
    private ArrayList<Target> spectatorTargets = new ArrayList<>();
    private ArrayList<Target> artistTargets = new ArrayList<>();
    private int[] paths;

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
            if (checkForObject.equals("Spectators")) {
                JsonArray objects = root.getJsonArray("layers").getJsonObject(i).getJsonArray("objects");

                for (int j = 0; j < objects.size(); j++) {
                    String name = objects.getJsonObject(j).getString("name");
                    int id = objects.getJsonObject(j).getInt("id");
                    int height = objects.getJsonObject(j).getInt("height");
                    int width = objects.getJsonObject(j).getInt("width");
                    int x = objects.getJsonObject(j).getInt("x");
                    int y = objects.getJsonObject(j).getInt("y");

                    spectatorTargets.add(new Target(name, id, height, width, x, y, paths));


                }


                continue;
            }
            if (checkForObject.equals("Podium")) {
                JsonArray objects = root.getJsonArray("layers").getJsonObject(i).getJsonArray("objects");

                for (int j = 0; j < objects.size(); j++) {
                    String name = objects.getJsonObject(j).getString("name");
                    int id = objects.getJsonObject(j).getInt("id");
                    int height = objects.getJsonObject(j).getInt("height");
                    int width = objects.getJsonObject(j).getInt("width");
                    int x = objects.getJsonObject(j).getInt("x");
                    int y = objects.getJsonObject(j).getInt("y");

                    artistTargets.add(new Target(name, id, height, width, x, y, paths));
                }

                continue;
            }


            int layer[] = new int[height * width];
            for (int j = 0; j < 100 * 100; j++) {
                layer[j] = root.getJsonArray("layers").getJsonObject(i).getJsonArray("data").getInt(j);
            }
            allLayers.add(new Layer(layer, allTilesets));
            if (checkForObject.equals("Pathing")) {
                paths = layer;
            }
        }
//        System.out.println(spectatorTargets.get(1));

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
//            paths.draw0(g2d, 20, 20);
        }

//        for (Target spectatorTarget : spectatorTargets) {
//            spectatorTarget.draw(g2d);
//        }
        spectatorTargets.get(5).draw(g2d);
    }

    private void loadTilesets(JsonObject root) {
        for (int i = 0; i < root.getJsonArray("tilesets").size(); i++) {
            allTilesets.add(new Tileset(root, i));
        }
    }
}
