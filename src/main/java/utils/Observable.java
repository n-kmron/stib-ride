package utils;

public interface Observable {

    /**
     * add an observer to the observable
     * @param o
     */
    public void addObserver(Observer o);

    /**
     * remove an observer from the observable
     * @param o
     */
    public void removeObserver(Observer o);
}
