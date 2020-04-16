package iti.intake40.tritra.history_note_dialog;

import java.util.List;

import iti.intake40.tritra.model.NoteModel;
import iti.intake40.tritra.model.NotesPresenterInterface;

public interface HistoryNotesContract {

    interface PresenterInterface extends NotesPresenterInterface {
        void updateNoteStaus(NoteModel noteModel,String tripId);
    }

    interface ViewInterface {
        void updateNoteList(List<NoteModel> notes);
        void updateNoteStaus(NoteModel noteModel);
    }
}
