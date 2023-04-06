package model;

import config.ConfigManager;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MechanicsTest {

    private Mechanics mechanics;

    public MechanicsTest() {
        System.out.println("==== StibRideTest Constructor =====");
        try {
            ConfigManager.getInstance().load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        mechanics = new Mechanics();
    }


    @Test
    public void testAjdacentNodesOnSameLine() throws Exception {
        System.out.println("testAjdacentNodesOnLine1");
        //Action
        Set<Node> result = mechanics.getGraph().getNodes();
        Set<Node> expected = new HashSet<>();
        Node a = new Node("GARE DE L'OUEST");
        Node b = new Node("BEEKKANT");
        Node c = new Node("ETANGS NOIRS");
        Node d = new Node("COMTE DE FLANDRE");
        Node e = new Node("SAINTE-CATHERINE");
        Node f = new Node("DE BROUCKERE");
        Node g = new Node("GARE CENTRALE");
        a.addDestination(b, 1);
        b.addDestination(c, 1);
        b.addDestination(a, 1);
        c.addDestination(d, 1);
        c.addDestination(b, 1);
        d.addDestination(e, 1);
        d.addDestination(c, 1);
        e.addDestination(f, 1);
        e.addDestination(d, 1);
        f.addDestination(g, 1);
        f.addDestination(e, 1);
        g.addDestination(f, 1);
        List<Node> list = List.of(a,b,c,d,e,f,g);
        expected.addAll(list);
        //Assert
        assertEquals(expected, result);
    }

    @Test
    public void testSearchWayNearbyStations() {
        System.out.println("testSearchWayNearbyStations");
        //Action
        Node a = mechanics.getGraph().getNode(new Node("GARE DE L'OUEST"));
        Node b = mechanics.getGraph().getNode(new Node("ETANGS NOIRS"));
        List<Node> result = mechanics.searchWay(a, b);
        List<Node> expected = new LinkedList<>();
        expected.add(mechanics.getGraph().getNode(new Node("GARE DE L'OUEST")));
        expected.add(mechanics.getGraph().getNode(new Node("BEEKKANT")));
        //Assert
        assertEquals(expected, result);
    }

    @Test
    public void testSearchWayDistantStationsOnSameLine() {
        System.out.println("testSearchWayDistantStationsOnSameLine");
        //Action
        Node a = mechanics.getGraph().getNode(new Node("GARE DE L'OUEST"));
        Node b = mechanics.getGraph().getNode(new Node("DE BROUCKERE"));
        List<Node> result = mechanics.searchWay(a, b);
        List<Node> expected = new LinkedList<>();
        expected.add(mechanics.getGraph().getNode(new Node("GARE DE L'OUEST")));
        expected.add(mechanics.getGraph().getNode(new Node("BEEKKANT")));
        expected.add(mechanics.getGraph().getNode(new Node("ETANGS NOIRS")));
        expected.add(mechanics.getGraph().getNode(new Node("COMTE DE FLANDRE")));
        expected.add(mechanics.getGraph().getNode(new Node("SAINTE-CATHERINE")));
        //Assert
        assertEquals(expected, result);
    }

}