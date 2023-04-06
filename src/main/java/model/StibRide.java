package model;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import repository.dto.ShortcutDto;
import utils.Observable;
import utils.Observer;

import java.util.ArrayList;
import java.util.List;

public class StibRide implements Observable {

    private List<Observer> observers;

    private Mechanics mechanics;

    private List<Node> activePath;

    public StibRide() {
        this.mechanics = new Mechanics();
        this.observers = new ArrayList<>();
        this.activePath = new ArrayList<>();
    }

    /**
     * Generate a new path
     * @param source
     * @param destination
     * @return
     */
    public List<Node> find(Node source, Node destination) {
        return mechanics.searchWay(source, destination);
    }


    /**
     * Change the active path to show on the app by generate a new one from a source and a destination
     * @param source
     * @param destination
     */
    public void setActivePath(Node source, Node destination) {
        this.activePath = find(source, destination);
        notifyObservers();
    }

    /**
     * Get the active path showed on the app
     */
    public List<Node> getActivePath() {
        return activePath;
    }

    public void exit() {
        System.exit(0);
    }

    /**
     * Allows to save a shortcut in the app
     * @param name the name of the shortcut
     * @param stations the source and the destination of the shortcut
     */
    public void saveShortcut(String name, List<String> stations) {
        ShortcutDto shortcut = new ShortcutDto(name, stations.get(0), stations.get(1));
        mechanics.addShortcut(shortcut);
    }

    public void removeShortcut(String key) {
        mechanics.removeShortcut(key);
    }

    /**
     * Allows to load a shortcut on the main app
     * @param name the name of the shortcut to load
     */
    public void loadShortcut(String name) {
        ShortcutDto shortcut = mechanics.getShortcut(name);
        Node source = findNode(shortcut.getSource());
        Node destination = findNode(shortcut.getDestination());
        setActivePath(source, destination);
    }

    /**
     * Find a node by his name (to keep reals data from him)
     */
    public Node findNode(String name) {
        return mechanics.getGraph().getNode(new Node(name));
    }

    @Override
    public void addObserver(Observer o) {
        if(!observers.contains(o)) {
            observers.add(o);
        }
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    public void notifyObservers() {
        for(Observer obs : observers) {
            obs.update();
        }
    }
}
