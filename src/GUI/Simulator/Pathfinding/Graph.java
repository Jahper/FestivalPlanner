package GUI.Simulator.Pathfinding;

import GUI.Simulator.Pathfinding.Node;

public class Graph {

    private  Node[][] nodes;

    public Graph(int width, int height) {
        nodes = new Node[width][height];
    }
    public void addNode(int x, int y, Node node){
        nodes[x][y] = node;
    }
}
