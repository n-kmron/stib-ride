package repository.dto;

/**
 * Data transfer object for a shortcut
 * The key is the name of the shortcut
 */
public class ShortcutDto extends Dto<String> {
    private String name;

    private int source;

    private int destination;


    public ShortcutDto(String name, int source, int destination) {
        this.name = name;
        this.source = source;
        this.destination = destination;
    }

    @Override
    public String getKey() {
        return name;
    }


    //getters
    public int getSource() {
        return source;
    }

    public int getDestination() {
        return destination;
    }

    public void setSource(int source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "Name : " + name + " - Source : " + source + " - Destination : " + destination;
    }
}
