package repository.dto;

import javafx.util.Pair;

/**
 * Data transfer object for a stop of a metro line
 * The compound key is a pair of a line and a station
 */
public class StopDto extends Dto<Pair<Integer,Integer>> {

    private Integer line;

    private Integer station;

    private String stationName;

    private Integer order;


    public StopDto(Integer line, Integer station, Integer order, String name) {
        this.line = line;
        this.station = station;
        this.order = order;
        this.stationName = name;
    }

    /**
     * Pour créer une clé composée, on utilise une paire
     * @return
     */
    @Override
    public Pair<Integer,Integer> getKey() {
        return new Pair<>(line, station);
    }


    //getters & setters

    public Integer getLine() {
        return line;
    }

    public Integer getStation() {
        return station;
    }

    public String getStationName() {
        return stationName;
    }

    @Override
    public String toString() {
        return "Line : " + line + " - Arrêt : " + order + " - Station :  " + station;
    }
}