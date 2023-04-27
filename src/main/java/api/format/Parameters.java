package api.format;

public class Parameters {
    String dataset;
    int rows;
    int start;
    Refine refine;
    String format;
    String timezone;

    public String getDataset() {
        return dataset;
    }

    public int getRows() {
        return rows;
    }

    public int getStart() {
        return start;
    }

    public Refine getRefine() {
        return refine;
    }

    public String getFormat() {
        return format;
    }

    public String getTimezone() {
        return timezone;
    }
}
