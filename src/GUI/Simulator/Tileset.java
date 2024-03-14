package GUI.Simulator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Tileset {
    private String imageName;
    private int firstID;
    private ArrayList<BufferedImage> tileSet = new ArrayList<>();

    public Tileset(String imageName, int firstID) {
        this.imageName = imageName + ".png";
        this.firstID = firstID;

//        System.out.println(firstID);

        createTileSet();
    }

    private void createTileSet() {
        try {
            BufferedImage tilemap = ImageIO.read(getClass().getResourceAsStream(imageName));

            for (int y = 0; y < tilemap.getHeight(); y += 32) {
                for (int x = 0; x < tilemap.getWidth(); x += 32) {
                    tileSet.add(tilemap.getSubimage(x, y, 32, 32));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getImageName() {
        return imageName;
    }

    public int getFirstID() {
        return firstID;
    }

    public ArrayList<BufferedImage> getTileSet() {
            return tileSet;
    }
    public boolean isUsedTileset(int firstID){
        return firstID == this.firstID;
    }

    public BufferedImage getTile(int index){
        if (index - firstID > firstID){
//            System.out.println(index - firstID);
//            System.out.println(index);
//            System.out.println(firstID);
        }
//        System.out.println(index);
//        System.out.println(firstID);
        return tileSet.get(index);
    }
}
