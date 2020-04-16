package iti.intake40.tritra.history_note_dialog;

import java.util.List;

import iti.intake40.tritra.model.Database;
import iti.intake40.tritra.model.NoteModel;

public class HistoryNotesPresenter implements HistoryNotesContract.PresenterInterface {
    private HistoryNotesContract.ViewInterface viewInterface;
    private List<NoteModel> noteList;
    private String tripid;

    public HistoryNotesPresenter(HistoryNotesContract.ViewInterface viewInterface){
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
    public void updateNoteStaus(NoteModel noteModel, String tripId) {
        Database.getInstance().updateNote(noteModel,tripid);
    }

}
