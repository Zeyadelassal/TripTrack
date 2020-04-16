package iti.intake40.tritra.model;

public class NoteModel {
    public static final class STATUS{
        public static final String DONE = "DONE";
        public static final String TODO = "TODO";
    }
    private String id;
    private String note;
    private String status;

    public NoteModel() {
        this.id = "id";
        this.note = "note";
        this.status = "status";
    }

    public NoteModel(String id, String note, String status) {
        this.id = id;
        this.note = note;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
