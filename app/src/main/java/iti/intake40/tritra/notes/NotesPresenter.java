package iti.intake40.tritra.notes;

import java.util.List;

import iti.intake40.tritra.model.Database;
import iti.intake40.tritra.model.NoteModel;

public class NotesPresenter implements NotesContract.PresenterInterface {
    private NotesContract.ViewInterface viewInterface;
    private List<NoteModel> noteList;
    private String tripid;

    public NotesPresenter(NotesContract.ViewInterface viewInterface){
        this.viewInterface = viewInterface;
    }


    @Override
    public void getAllNotes(String tripId) {
        this.tripid = tripId;
        Database.getInstance().getNotesForTrip(tripId,this);
    }

    @Override
    public void setAllNotes(List<NoteModel> notes) {
        noteList = notes;
        viewInterface.updateNoteList(notes);
    }

    @Override
    public void notifyUpdate(NoteModel note, int pos) {
        Database.getInstance().updateNote(note,tripid);
    }

    @Override
    public void notifyDelete(NoteModel note, int pos) {
        Database.getInstance().deleteNode(note,tripid);
    }


}
