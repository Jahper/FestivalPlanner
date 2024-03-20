package GUI.Simulator.Pathfinding;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Node {
    Point2D pos;
    ArrayList<Node> neighboringNodes = new ArrayList<>();

    public Node(Point2D pos) {
        this.pos = pos;
    }
    public ArrayList<Node> getNeighboringNodes(){
        return neighboringNodes;
    }

    public Point2D getPos(){
        return pos;
    }
    public void addNeighboringNode(Node node){
        neighboringNodes.add(node);
    }


}
