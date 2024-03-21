package GUI.Simulator;

import GUI.Simulator.Pathfinding.Graph;
import GUI.Simulator.Pathfinding.Node;
import org.jfree.fx.FXGraphics2D;

import java.util.*;

public class Target {
    private String name;
    private int id;
    private double height;
    private double width;
    private double x;
    private double y;
    private int[] paths;
    private Graph graph;

    public Target(String name, int id, double height, double width, double x, double y, int[] paths) {
        this.name = name;
        this.id = id;
        this.height = height;
        this.width = width;
        this.x = x;
        this.y = y;
        this.paths = paths;

        createGraph();
        createDistanceField();
    }

    private void createGraph() {
        this.graph = new Graph(100, 100);

        for (int i = 0; i < paths.length; i++) {
            int tile = paths[i];

            int x = i % 100;

            int y = (i - x) / 100;

            Node node = new Node(x * 32, y * 32);

            if (tile > 0) {
                node.setDistance(1);
                //checks if node is in target
                int actualX = x * 32;
                int actualY = y * 32;

                boolean lowerRangeX = actualX >= this.x;
                boolean upperRangeX = actualX <= this.width + this.x;
                boolean lowerRangeY = actualY >= this.y;
                boolean upperRangeY = actualY <= this.height + this.y;

                if (lowerRangeX && upperRangeX && lowerRangeY && upperRangeY) {
                    node.setDistance(0);
                }
            } else {
                node.setCollision(true);
            }
            graph.addNode(x, y, node);
        }
    }

    private void createDistanceField() {
        Queue<Node> toCheck = new LinkedList<>();
        ArrayList<Node> checked = new ArrayList<>();
        //adding targetTiles to checked etc.
        for (int y = 0; y < 100; y++) {
            for (int x = 0; x < 100; x++) {
                Node node = graph.getNodes()[x][y];
                int distance = node.getDistance();

                if (distance == 0) {
                    toCheck.add(node);
                    checked.add(node);
                }
            }
        }
        Iterator iterator = toCheck.iterator();
        Comparator<Node> comparator = new NodeComparator();
        while (iterator.hasNext()) {
            Node next = toCheck.poll();

            int x = next.getX() / 32;
            int y = next.getY() / 32;

            int distance = next.getDistance();

            if (next.isCollision()) {
                continue;
            }

            Node top = checkForOutOfBounds(x, y + 1);
            Node right = checkForOutOfBounds(x + 1, y);
            Node bottom = checkForOutOfBounds(x, y - 1);
            Node left = checkForOutOfBounds(x - 1, y);

            List<Node> neighbours = addNeighbours(top, right, bottom, left);

            if (!checked.contains(top) && top != null) {
                top.setDistance(distance + 1);
                toCheck.add(top);
                checked.add(top);
            }
            if (!checked.contains(right) && right != null) {
                right.setDistance(distance + 1);
                toCheck.add(right);
                checked.add(right);
            }
            if (!checked.contains(bottom) && bottom != null) {
                bottom.setDistance(distance + 1);
                toCheck.add(bottom);
                checked.add(bottom);
            }
            if (!checked.contains(left) && left != null) {
                left.setDistance(distance + 1);
                toCheck.add(left);
                checked.add(left);
            }

            neighbours.sort(comparator);

            if (!neighbours.isEmpty()) {
                next.addNearestNode(neighbours.get(0));
            }
        }
    }

    private List<Node> addNeighbours(Node top, Node right, Node bottom, Node left) {
        List<Node> neighbours = new ArrayList<>();
        if (top != null) {
            neighbours.add(top);
        }
        if (right != null) {
            neighbours.add(right);
        }
        if (bottom != null) {
            neighbours.add(bottom);
        }
        if (left != null) {
            neighbours.add(left);
        }
        return neighbours;
    }

    private Node checkForOutOfBounds(int x, int y) {
        Node node;
        try {
            node = graph.getNodes()[x][y];
            if (node.getDistance() == -1) {
                throw new ArrayIndexOutOfBoundsException();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
        return node;
    }

    public void draw(FXGraphics2D g2d) {
        for (int y = 0; y < 100; y++) {
            for (int x = 0; x < 100; x++) {
                Node node = graph.getNodes()[x][y];
                g2d.drawString(node.getDistance() + "", x * 32, y * 32);
            }
        }
    }

    public Graph getGraph() {
        return graph;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public double getHeight() {
        return this.height;
    }

    public double getWidth() {
        return this.width;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    @Override
    public String toString() {
        return "name: " + this.getName() + " id: " + this.id + " height: " + this.height + " whidth: " + this.width + " x: " + this.x + " y: " + this.y;
    }

    public class NodeComparator implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            if (o2 == null) {
                return 1;
            } else if (o1 == null) {
                return -1;
            }
            return o1.getDistance() - o2.getDistance();
        }
    }
}
