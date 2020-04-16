package iti.intake40.tritra.model;

import java.util.List;

public interface NotesPresenterInterface {
    void getAllNotes(String tripId);
    void setAllNotes(List<NoteModel> notes);
}
