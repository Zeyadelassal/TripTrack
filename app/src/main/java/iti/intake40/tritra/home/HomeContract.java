package iti.intake40.tritra.home;

import java.util.List;

import iti.intake40.tritra.model.TripModel;
import iti.intake40.tritra.model.UserTripsInterface;

public interface HomeContract {

    interface PresenterInterface extends UserTripsInterface {
        void getTrips(String userId);
        void editTrip(int pos);
        void deleteTrip(int pos,String userId);
        void moveTripToHistory(TripModel tripModel,String userId);
        void createRuturnTrip(TripModel tripModel,String userId);
    }

    interface ViewInterface {
        void displayTrips(List<TripModel> tripsList);
        void displayNoTrips();
        void displayMessage(String msg);
        void openTripActivityForEdit(String tripId);
        void cancelTripAlarm(TripModel trip);
    }
}
