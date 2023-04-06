package model;

import repository.ShortcutRepository;
import repository.StationRepository;
import repository.StopRepository;
import repository.dto.ShortcutDto;
import repository.dto.StationDto;
import repository.dto.StopDto;
import java.util.*;


public class Mechanics {

    private Graph graph;
    private Map<Integer, Node> stationsSet;

    public Mechanics() {
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
        StationRepository stationRepository = new StationRepository();
        List<StationDto> getStations = stationRepository.getAll();

        Map<Integer, Node> stations = new HashMap<>();
        for (int i = 0; i < getStations.size(); i++) {
            Node station = new Node(getStations.get(i).getName());
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
     * Implementation of Dijkstra algorithm to calculate the shortest path from source
     *
     * @param source
     * @param destination
     * @return
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

    public void addShortcut(ShortcutDto shortcut) {
        ShortcutRepository shortcutRepository = new ShortcutRepository();
        shortcutRepository.add(shortcut);
    }

    public void removeShortcut(String key) {
        ShortcutRepository shortcutRepository = new ShortcutRepository();
        shortcutRepository.remove(key);
    }

    public ShortcutDto getShortcut(String key) {
        ShortcutRepository shortcutRepository = new ShortcutRepository();
        return shortcutRepository.get(key);
    }

}
