package model;

import api.ServiceGenerator;
import api.WaitingTimeApi;
import api.format.PassingTime;
import api.format.Record;
import api.format.WaitingTimeResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import repository.ShortcutRepository;
import repository.StationRepository;
import repository.StopRepository;
import repository.dto.ShortcutDto;
import repository.dto.StationDto;
import repository.dto.StopDto;
import retrofit2.Call;
import retrofit2.Response;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * Manage the app mechanics and implements the different algorithms
 */
public class Mechanics {

    private Graph graph;
    private Map<Integer, Node> stationsSet;
    private Language language;
    private static String API_TOKEN = "470d76825dce88e1b1a609c3f93f3cd9c36f546b28ed9cdb1107f2da";


    public Mechanics() {
        this.language = Language.FR;
        graph = new Graph();
        stationsSet = putAdjacentNodes(readDB());
        initializeGraph(stationsSet);
    }

    public Graph getGraph() {
        return graph;
    }

    /**
     * Read the database and put store all stations in a map
     * @return a map with a key : the id of the station and a value : Node representing a station
     */
    private Map<Integer, Node> readDB() {
        StationRepository stationRepository = new StationRepository(language);
        List<StationDto> getStations = stationRepository.getAll();

        Map<Integer, Node> stations = new HashMap<>();
        for (int i = 0; i < getStations.size(); i++) {
            Node station = new Node(getStations.get(i).getName(), getStations.get(i).getKey());
            stations.put(getStations.get(i).getKey(), station);
        }
        return stations;
    }

    /**
     * Set neighbours to all nodes from a map of all stations and the database of stops
     * @param stations
     * @return the map with nodes updated
     */
    private Map<Integer, Node> putAdjacentNodes(Map<Integer, Node> stations) {
        StopRepository stopRepository = new StopRepository();
        List<StopDto> getStops = stopRepository.getAll();

        for (int i = 0; i < getStops.size(); i++) {
            if(i+1<getStops.size()) {
                //bottom line is there to add all the lines which deserve the station
                stations.get(getStops.get(i).getStation()).addLine(getStops.get(i).getLine());
                //the stations are ordered by line and sequence number so if the line is equal, 2 stations following each other are neighbors
                if(getStops.get(i).getLine() == getStops.get(i+1).getLine()) {
                    //add a neighbor to the corresponding node which is the next element if this neighbor does not exist yet
                    if(!stations.get(getStops.get(i).getStation()).adjacentNodes.containsKey(stations.get(getStops.get(i+1).getStation()))) {
                        stations.get(getStops.get(i).getStation()).adjacentNodes.put(stations.get(getStops.get(i+1).getStation()), 1);
                    }
                }

            }
            if(i-1>=0) {
                //the stations are ordered by line and sequence number so if the line is equal, 2 stations following each other are neighbors
                if(getStops.get(i).getLine() == getStops.get(i-1).getLine()) {
                    //add a neighbor to the corresponding node which is the next element if this neighbor does not exist yet
                    if(!stations.get(getStops.get(i).getStation()).adjacentNodes.containsKey(stations.get(getStops.get(i-1).getStation()))) {
                        stations.get(getStops.get(i).getStation()).adjacentNodes.put(stations.get(getStops.get(i-1).getStation()), 1);
                    }
                }

            }
        }
        return stations;
    }

    /**
     * Initialize the graph from nodes stored in the map of stations
     * @param stations a map with all nodes and their neighbours
     */
    private void initializeGraph(Map<Integer, Node> stations) {
        for (Map.Entry mapentry : stations.entrySet()) {
            graph.addNode((Node) mapentry.getValue());
        }
    }

    /**
     * Implementation of Dijkstra algorithm to calculate the shortest path from source station to the destination one
     *
     * N.B this algorithm comes from `www.baeldung.com/java-dijkstra` but is edited for the needs of the app.
     *
     * @param source the starting station of the travel
     * @param destination the end station of the travel
     * @return a path between these two stations
     */
    public List<Node> searchWay(Node source, Node destination) {
        graph = new Graph();
        stationsSet = putAdjacentNodes(readDB());
        initializeGraph(stationsSet);
        source.setDistance(0);

        Set<Node> settledNodes = new HashSet<>();
        Set<Node> unsettledNodes = new HashSet<>();

        unsettledNodes.add(source);

        while (unsettledNodes.size() != 0 && !settledNodes.contains(destination)) {
            Node currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);
            for (Map.Entry< Node, Integer> adjacencyPair: currentNode.getAdjacentNodes().entrySet()) {
                Node adjacentNode = adjacencyPair.getKey();
                Integer edgeWeight = adjacencyPair.getValue();
                if (!settledNodes.contains(adjacentNode)) {
                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    unsettledNodes.add(adjacentNode);
                }
            }
            settledNodes.add(currentNode);
        }
        List<Node> finalWay = destination.getShortestPath();
        finalWay.add(destination);
        destination.setShortestPath(finalWay);
        return destination.getShortestPath();
    }

    private Node getLowestDistanceNode(Set <Node> unsettledNodes) {
        Node lowestDistanceNode = null;
        int lowestDistance = Integer.MAX_VALUE;
        for (Node node: unsettledNodes) {
            int nodeDistance = node.getDistance();
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }

    private void calculateMinimumDistance(Node evaluationNode, Integer edgeWeigh, Node sourceNode) {
        Integer sourceDistance = sourceNode.getDistance();
        if (sourceDistance + edgeWeigh < evaluationNode.getDistance()) {
            evaluationNode.setDistance(sourceDistance + edgeWeigh);
            LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
        }
    }

    /**
     * Create a new shortcut via the repository
     * @param shortcut the shortcut to add
     */
    public void addShortcut(ShortcutDto shortcut) {
        ShortcutRepository shortcutRepository = new ShortcutRepository();
        shortcutRepository.add(shortcut);
    }

    /**
     * Remove a shortcut via the repository
     * @param key the key of the shortcut to remove
     */
    public void removeShortcut(String key) {
        ShortcutRepository shortcutRepository = new ShortcutRepository();
        shortcutRepository.remove(key);
    }

    /**
     * Get a shortcut via the repository
     * @param key the key of the shortcut to get
     */
    public ShortcutDto getShortcut(String key) {
        ShortcutRepository shortcutRepository = new ShortcutRepository();
        return shortcutRepository.get(key);
    }

    public void changeLanguage(Language language) {
        this.language = language;
        graph = new Graph();
        stationsSet = putAdjacentNodes(readDB());
        initializeGraph(stationsSet);
    }

    /**
     * Get the two next metros for a station
     * @param id station's id
     */
    public MetroTime[] getNextMetros(int id) throws IOException {
        MetroTime[] arrivals = getArrivals(id);

        MetroTime min = new MetroTime(Integer.MAX_VALUE, 9999);
        MetroTime min2 = new MetroTime(Integer.MAX_VALUE, 9999);

        for(int i=0; i<arrivals.length; i++) {
            if(arrivals[i].getDuration() < min.getDuration()) {
                if(min2.getDuration() == Integer.MAX_VALUE) {
                    min2 = min;
                }
                min = arrivals[i];
            }
            else if(arrivals[i].getDuration() < min2.getDuration()) {
                min2 = arrivals[i];
            }
        }

        MetroTime[] nextMetros;
        if(arrivals.length > 1) {
            nextMetros = new MetroTime[2];
            nextMetros[0] = min;
            nextMetros[1] = min2;
        } else {
            nextMetros = new MetroTime[1];
            nextMetros[0] = min;
        }

        return nextMetros;
    }

    /**
     * Get every arrival from records
     */
    private MetroTime[] getArrivals(int id) throws IOException {
        List<Record> records = getApiRecords(id);
        MetroTime[] arrivals = new MetroTime[records.size()];

        for(int i=0; i<records.size(); i++) {

            //convert passingTimes as String into List<PassingTime>
            String passingTimesJsonString = records.get(i).getFields().getPassingtimes();
            Type passingTimesListType = new TypeToken<List<PassingTime>>(){}.getType();
            List<PassingTime> passingTimes = new Gson().fromJson(passingTimesJsonString, passingTimesListType);

            for(PassingTime passingTime : passingTimes) {
                String dateTimeString = passingTime.getExpectedArrivalTime();
                LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                Instant instant = dateTime.toInstant(ZoneOffset.ofHours(2));
                Duration duration = Duration.between(Instant.now(), instant);

                int line = Integer.parseInt(passingTime.getLineId());
                long durationToInt = duration.toMinutes();
                MetroTime metroTime = new MetroTime(durationToInt, line);

                arrivals[i] = metroTime;
            }
        }
        return arrivals;
    }

    /**
     * Get each records from API for a station
     * @param id station's id
     */
    private List<Record> getApiRecords(int id) throws IOException {
        List<Record> records = new ArrayList<>();

        List<Integer> catchAllIds = catchAllIds(id);
        for (int station : catchAllIds) {
            records.addAll(askRecordsToApi(station));
        }
        return records;
    }

    /**
     * In the database some stations have many id and in this app, these ids have been merged
     * to have all the metro in the station, this needs to be checked
     * @param id the station to check
     * @return all the id for the station for the API
     */
    private List<Integer> catchAllIds(int id) {
        List<Integer> ids = new ArrayList<>();

        switch(id) {
            case 8641:
                ids.add(8641);
                ids.add(8642);
                break;
            case 8833:
                ids.add(8833);
                ids.add(8834);
                break;
            case 8742:
                ids.add(8742);
                ids.add(8744);
                break;
            case 8042:
                ids.add(8042);
                ids.add(8402);
                break;
            case 8382:
                ids.add(8382);
                ids.add(8732);
                ids.add(8733);
                break;
            default:
                ids.add(id);
        }
        return ids;
    }

    /**
     * Asks records for a station to the STIB API
     * @param id station's id
     * @return
     */
    private List<Record> askRecordsToApi(int id) throws IOException {
        WaitingTimeApi api = ServiceGenerator.createService(WaitingTimeApi.class, API_TOKEN);
        Call<WaitingTimeResponse> call = api.getWaitingTimes("waiting-time-rt-production", "", 10, id);
        Response<WaitingTimeResponse> response = call.execute();
        if (!response.isSuccessful()) {
            throw new IOException("Failed to retrieve data from the API");
        }
        WaitingTimeResponse res = response.body();
        if (res == null || res.getRecords().isEmpty()) {
            throw new IOException("No records found in the API response");
        }
        List<Record> records = res.getRecords();
        return records;
    }
}
