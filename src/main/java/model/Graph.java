package model;

import java.util.HashSet;
import java.util.Set;

/**
 * Implements a simple node graph using a HashSet
 */
public class Graph {

    private Set<Node> nodes = new HashSet<>();

    public void addNode(Node nodeA) {
        nodes.add(nodeA);
    }

    public Set<Node> getNodes() {
        return nodes;
    }

    public Node getNode(Node node) {
        for (Node n : nodes) {
            if(node.getName().equals(n.getName()) || node.getId() == n.getId()) {
                return n;
            }
        }
        throw new IllegalArgumentException("No node with this name/id");
    }
}