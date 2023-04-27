package model;

import repository.dto.ShortcutDto;
import repository.dto.StationDto;
import utils.Observable;
import utils.Observer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Access facade to the model
 * This class implements only the methods directly asked by the controller
 *
 * N.B this class is the only one which allows to the controller to communicate with the model
 *
 */
public class StibRide implements Observable, Observer{

    private List<Observer> observers;

    private Mechanics mechanics;

    private List<Node> activePath;

    private MetroTime[] nextArrivals;

    public StibRide() {
        this.mechanics = new Mechanics();
        this.observers = new ArrayList<>();
        this.activePath = new ArrayList<>();
    }

    /**
     * Generate a new path between two stations and change the active path to show on the app by generate a new one from a source and a destination
     *
     * @param source the starting station of the travel
     * @param destination the end station of the travel
     */
    public void setActivePath(Node source, Node destination) {
        System.out.println("[DEBUG] " + source.getName() + " - " +destination.getName());
        SearchThread t = new SearchThread(mechanics, source, destination);
        t.addObserver(this);
        t.start();
    }

    /**
     * Get the active path showed on the app
     */
    public List<Node> getActivePath() {
        return activePath;
    }

    /**
     * Get the next arrivals
     * @return
     */
    public MetroTime[] getNextArrivals() {
        return nextArrivals;
    }

    public void exit() {
        System.exit(0);
    }

    /**
     * Allows to save a shortcut in the app
     *
     * @param name the name of the shortcut
     * @param stations the source and the destination of the shortcut
     */
    public void saveShortcut(String name, List<Integer> stations) {
        if(stations.size() != 2) {
            throw new IllegalArgumentException("Shortcut has to have 2 stations");
        }
        ShortcutDto shortcut = new ShortcutDto(name, stations.get(0), stations.get(1));
        mechanics.addShortcut(shortcut);
    }

    /**
     * Ask to remove a shortcut from his key
     *
     * @param key the key of the shortcut to remove
     */
    public void removeShortcut(String key) {
        mechanics.removeShortcut(key);
    }

    /**
     * Allows to load a shortcut on the main app
     *
     * @param name the name of the shortcut to load
     */
    public void loadShortcut(String name) {
        ShortcutDto shortcut = mechanics.getShortcut(name);
        Node source = findNode(shortcut.getSource());
        Node destination = findNode(shortcut.getDestination());
        setActivePath(source, destination);
    }

    /**
     * Find a node by his name (to keep real data from him)
     */
    public Node findNode(String name) {

        return mechanics.getGraph().getNode(new Node(name));
    }

    public Node findNode(int id) {
        return mechanics.getGraph().getNode(new Node(id));
    }

    /**
     * Set the next arrivals metros for a station
     * @param source the station to check
     */
    public void setNextArrival(Node source) {
        try {
            MetroTime[] next = mechanics.getNextMetros(source.getId());
            if(next.length == 0 || next.length > 2) {
                throw new IllegalArgumentException("Too much next metros : " + next.length);
            }
            this.nextArrivals = next;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void changeLanguage(Language language) {
        mechanics.changeLanguage(language);
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

    @Override
    public void update() {
    }

    @Override
    public void update(Object o) {
        this.activePath = (List<Node>) o;
        notifyObservers();
    }
}
