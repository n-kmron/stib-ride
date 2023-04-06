package repository.dto;

/**
 * Data transfer object for a shortcut
 * The key is the name of the shortcut
 */
public class ShortcutDto extends Dto<String> {
    private String name;

    private String source;

    private String destination;


    public ShortcutDto(String name, String source, String destination) {
        this.name = name;
        this.source = source;
        this.destination = destination;
    }

    @Override
    public String getKey() {
        return name;
    }


    //getters
    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @Override
    public String toString() {
        return "Name : " + name + " - Source : " + source + " - Destination : " + destination;
    }
}
