package model;

public class MetroTime {

    private long duration;

    private int line;

    public MetroTime(long duration, int line) {
        this.duration = duration;
        this.line = line;
    }

    public long getDuration() {
        return duration;
    }

    public int getLine() {
        return line;
    }
}
