package iti.intake40.tritra.home;

import android.view.View;

import iti.intake40.tritra.model.TripModel;

public interface CardMenuInterface {
    void onPopupMenuClick(View view,int pos);
    void openNotes(String tripId);
    void startTrip(TripModel trip);
}
