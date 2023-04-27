package utils;

public interface Observer {

    /**
     * updates the observer according to the state of the observed
     */
    public void update();

    public void update(Object o);
}
