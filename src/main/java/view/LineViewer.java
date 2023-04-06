package view;

import java.util.List;

public class LineViewer {

    private String name;
    private List<Integer> lines;

    public LineViewer(String name, List<Integer> lines) {
        this.name = name;
        this.lines = lines;
    }

    public String getName() {
        return name;
    }

    public String getLine() {
        String stationLines = "[";
        for (int i = 0; i < lines.size(); i++) {
                if(i == lines.size() -1 || lines.get(i) == 0) {
                    stationLines += lines.get(i);
                } else {
                    stationLines += lines.get(i)+ "-";
                }
            }
        stationLines += "]";
        return stationLines;
    }
}
