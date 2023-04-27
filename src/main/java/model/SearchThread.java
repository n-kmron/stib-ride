package model;

import utils.Observable;
import utils.Observer;

import java.util.ArrayList;
import java.util.List;

public class SearchThread extends Thread implements Observable {

    private Mechanics mechanics;

    private Node source;

    private Node destination;

    private List<Node> path;

    private List<Observer> observers;

    public SearchThread(Mechanics mechanics, Node source, Node destination) {
        this.mechanics = mechanics;
        this.source = source;
        this.destination = destination;
        this.path = new ArrayList<>();
        this.observers = new ArrayList<>();
    }

    @Override
    public void run() {
        path = mechanics.searchWay(source, destination);
        notifyObservers(path);
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

    public void notifyObservers(List<Node> path) {
        for(Observer o : observers) {
            o.update(path);
        }
    }

}
