package GUI.Simulator;

import javax.json.JsonObject;
import java.util.ArrayList;

public class Layer {
    private int[] layer;
    private JsonObject root;
    private ArrayList<Tileset> tilesets = new ArrayList<>();
    private Tileset tileset;
    private int i;
    private int correction;

    public Layer(JsonObject root, int i, ArrayList<Tileset> tileset) {
        this.root = root;
        this.i = i;
        this.tilesets = tileset;
        createLayer();
    }

    private void createLayer() {
        int height = root.getInt("width");
        int width = root.getInt("height");
        int[] layer = new int[height * width];
        for (int j = 0; j < 100 * 100; j++) {
            layer[j] = root.getJsonArray("layers").getJsonObject(i).getJsonArray("data").getInt(j);
        }
        this.layer = layer;

//        ArrayList<Integer> startIDs = new ArrayList<>();
//
//        for (Tileset tileset : tilesets) {
//            startIDs.add(tileset.getFirstID());
//        }
//        System.out.println(startIDs);
//        for (int i = 0; i < height * width; i++) {
//
//            int tile = layer[i];
////            if (tile <= 0) {
////                continue;
////            } else if (tile > 2817) {
////                tileset = tilesets.get(2);
//////                    tile -= 2817;
////            } else if (tile > 1537) {
////                tileset = tilesets.get(1);
//////                    tile -= 1537;
////            } else {
////                tileset = tilesets.get(0);
//////                    tile -= 1;
////            }
////
//            //                System.out.println(id);
//
//
////                if (startIDs.indexOf(id) == startIDs.size() - 1) {
////                    tileset = tilesets.get(startIDs.size() - 1);
////                    this.correction = id;
//////                    System.out.println(id);
////                    break;
////                } else if (tile >= id && tile < startIDs.get(j + 1)) {
////                    tileset = tilesets.get(j);
//////                    System.out.println(tile - id);
//////                    System.out.println(tile + " : " + id);
////                    this.correction = id;
////                    break;
////                }
//
//        }
    }

    public int[] getLayer() {
        return layer;
    }

    public Tileset getTileset() {
        return tileset;
    }

    public int getCorrection() {
        return correction;
    }
}
