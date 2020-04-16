package iti.intake40.tritra.add_trip;

import iti.intake40.tritra.model.TripInterface;
import iti.intake40.tritra.model.TripModel;

public interface AddTripContract {

    interface PresenterInterface extends TripInterface {
        void addTrip(TripModel tripModel,String userId);
        void getTripForEdit(String userId,String tripId);
        void updateTrip(TripModel tripModel,String userId);
    }

    interface ViewInterface {
        void SetTripForEdit(TripModel tripModel);
    }
}
