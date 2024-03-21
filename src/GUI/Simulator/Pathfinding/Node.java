package GUI.Simulator.Pathfinding;

public class Node {
    private int x;
    private int y;
    private int distance = -1;
    private boolean collision = false;
    private Node nearestNode;


    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void addNearestNode(Node node) {
        this.nearestNode = node;
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

    public Node getNearestNode() {
        return nearestNode;
    }
}
