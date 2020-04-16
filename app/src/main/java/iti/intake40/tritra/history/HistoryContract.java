package iti.intake40.tritra.history;

import java.util.List;

import iti.intake40.tritra.model.TripModel;

public interface HistoryContract {

    interface PresenterInterface {
        void setTrips(List<TripModel> trips);
        void getTrips(String userId);
        void deleteTrip(String tripId,String userId);
        List<TripModel> getTrips();
    }

    interface ViewInterface {
        void displayTrips(List<TripModel> tripsList);
        void displayNoTrips();
        void displayMessage(String msg);
    }
}
