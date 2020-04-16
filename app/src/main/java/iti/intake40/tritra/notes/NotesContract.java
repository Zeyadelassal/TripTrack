package iti.intake40.tritra.notes;

import java.util.List;

import iti.intake40.tritra.history.NoteInterface;
import iti.intake40.tritra.model.NoteModel;
import iti.intake40.tritra.model.NotesPresenterInterface;

public interface NotesContract {

    interface PresenterInterface extends NotesPresenterInterface {
//        void getAllNotes(String tripId);
//        void setAllNotes(List<NoteModel> notes);
        void notifyUpdate(NoteModel note, int pos);
        void notifyDelete(NoteModel note, int pos);

    }

    interface ViewInterface {
        void updateNoteList(List<NoteModel> notes);
    }
}
