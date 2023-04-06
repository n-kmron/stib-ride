package repository.dto;

/**
 * Data transfer object for a station of metro
 * The key is the id of the station
 */
public class StationDto extends Dto<Integer> {

    private Integer id;

    private String name;


    public StationDto(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public Integer getKey() {
        return id;
    }


    //getters & setters
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Id : " + id + " - Station : " + name;
    }
}