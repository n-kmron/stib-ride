package model;

import java.util.List;

public class Shortcut {

    private String name;

    private List<Node> path;


    /**
     * Represent a shortcut to store
     * @param name the name of the shortcut
     * @param path the complete path of the shortcut
     */
    public Shortcut(String name, List<Node> path) {
        this.path = path;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Node> getPath() {
        return path;
    }
}
