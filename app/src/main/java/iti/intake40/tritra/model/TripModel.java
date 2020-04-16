package iti.intake40.tritra.model;

public class TripModel {
    private String id;
    private String name;
    private String type;
    private String startPoint;
    private String endPoint;
    private String date;
    private String time;
    private String status;

    public static class TYPE {
        public static final String ONE_DIRECTION = "oneDirection";
        public static final String ROUND_TRIP = "roundTrip";
    }

    public static class STATUS{
        public static final String UPCOMING = "UPCOMING";
        public static final String GO = "GO";
        public static final String DONE = "DONE";
        public static final String CANCEL = "CANCEL";
    }

    public TripModel() {
        this.id = "id";
        this.name = "name";
        this.type = TYPE.ONE_DIRECTION;
        this.startPoint = "startPoint";
        this.endPoint = "endPoint";
        this.date = "date";
        this.time = "time";
        this.status=STATUS.UPCOMING;
    }

    public TripModel(String id ,String name, String type, String startPoint, String endPoint, String date, String time, String status) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
