package iti.intake40.tritra.history;

import android.view.View;

import iti.intake40.tritra.model.NoteModel;

public interface NoteInterface {
    void openNote(String tripId);
    void deleteTrip(String tripId);
}
