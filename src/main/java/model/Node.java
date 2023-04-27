package model;

import java.util.*;

/**
 * Implements a simple node representing a metro station
 *
 * N.B a node can be described with a name, a LinkedList in reference to the shortestPath,
 * a distance from the source, and an adjacency list named adjacentNodes.
 */
public class Node {

    private String name;

    private int id;

    /**
     * Each metro line that pass by this node
     */
    private List<Integer> lines;

    private List<Node> shortestPath = new LinkedList<>();

    private Integer distance = Integer.MAX_VALUE;

    /*The adjacentNodes attribute is used to associate immediate neighbors with edge length.
    This is a simplified implementation of an adjacency list,
    which is more suitable for the Dijkstra algorithm than the adjacency matrix.*/
    Map<Node, Integer> adjacentNodes = new HashMap<>();

    /**
     * Adds a neighbor for this node
     * @param destination the new neighbor
     * @param distance the distance between the two nodes
     */
    public void addDestination(Node destination, int distance) {
        adjacentNodes.put(destination, distance);
    }

    public Node(String name) {
        this.name = name;
        this.lines = new ArrayList<>();
        this.id = 0;
    }

    public Node(String name, int id) {
        this.name = name;
        this.lines = new ArrayList<>();
        this.id = id;
    }

    public Node(int id) {
        this.name = "";
        this.lines = new ArrayList<>();
        this.id = id;
    }

    // getters and setters

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public List<Integer> getLines() {
        return lines;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addLine(int line) {
        lines.add(line);
    }

    public List<Node> getShortestPath() {
        return shortestPath;
    }

    public void setShortestPath(List<Node> shortestPath) {
        this.shortestPath = shortestPath;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Map<Node, Integer> getAdjacentNodes() {
        return adjacentNodes;
    }

    @Override
    public String toString() {
        String neighbors = "";
        for (Map.Entry mapentry : adjacentNodes.entrySet()) {
            Node current = (Node)mapentry.getKey();
            neighbors += current.getName() + " ";
        }
        //return name + " : " + neighbors;
        return name;
    }
}
