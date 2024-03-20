package GUI.Simulator.Pathfinding;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Node {
    int x;
    int y;
    int distance = -1;
    ArrayList<Node> neighboringNodes = new ArrayList<>();

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public ArrayList<Node> getNeighboringNodes(){
        return neighboringNodes;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void addNeighboringNode(Node node){
        neighboringNodes.add(node);
    }


}
