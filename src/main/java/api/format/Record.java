package api.format;

public class Record {
    String datasetid;
    String recordid;
    Fields fields;
    String record_timestamp;

    public String getDatasetid() {
        return datasetid;
    }

    public String getRecordid() {
        return recordid;
    }

    public Fields getFields() {
        return fields;
    }

    public String getRecord_timestamp() {
        return record_timestamp;
    }
}
