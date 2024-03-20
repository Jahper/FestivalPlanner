package GUI.Simulator.Pathfinding;

import java.util.ArrayList;

public class Node {
    int x;
    int y;
    int distance = -1;
    boolean collision = false;
    ArrayList<Node> surroundingNodes = new ArrayList<>();

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void addSurroundingNode(Node node) {
        surroundingNodes.add(node);
    }

    public ArrayList<Node> getSurroundingNodes() {
        return surroundingNodes;
    }

    public int getDistance() {
        return distance;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isCollision() {
        return collision;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setCollision(boolean collision) {
        this.collision = collision;
    }
}
