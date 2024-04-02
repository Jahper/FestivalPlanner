package GUI.Simulator;

import javax.imageio.ImageIO;
import javax.json.JsonObject;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Tileset {
    private ArrayList<BufferedImage> tileset = new ArrayList<>();
    private JsonObject root;
    private int index;
    //waarde die van de tile waarde afgehaald wordt door het gebruik van meerdere layers
    private int offset;

    public Tileset(JsonObject root, int index) {
        this.root = root;
        this.index = index;

        createTileset();
        setOffset();
    }

    private void createTileset() {
        try {
            BufferedImage tilemap = ImageIO.read(getClass().getResourceAsStream("files/"+ root.getJsonArray("tilesets").getJsonObject(index).getString("name") + ".png"));

            for (int y = 0; y < tilemap.getHeight(); y += 32) {
                for (int x = 0; x < tilemap.getWidth(); x += 32) {
                    tileset.add(tilemap.getSubimage(x, y, 32, 32));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setOffset() {
        this.offset = root.getJsonArray("tilesets").getJsonObject(index).getInt("firstgid");
    }

    public ArrayList<BufferedImage> getTileset() {
        return tileset;
    }

    public int getOffset() {
        return offset;
    }

    public boolean usesTileset(int tile) {
        int tileCount = root.getJsonArray("tilesets").getJsonObject(index).getInt("tilecount");
        return tile > offset && tile < offset + tileCount;
    }
}
