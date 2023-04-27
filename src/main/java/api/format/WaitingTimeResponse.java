package api.format;

import java.util.List;

public class WaitingTimeResponse {
    int nhits;
    Parameters parameters;
    public List<Record> records;

    public int getNhits() {
        return nhits;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public List<Record> getRecords() {
        return records;
    }
}

