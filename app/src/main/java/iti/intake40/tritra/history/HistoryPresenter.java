package iti.intake40.tritra.history;

import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import iti.intake40.tritra.HistoryMap;
import iti.intake40.tritra.model.Database;
import iti.intake40.tritra.model.TripModel;

public class HistoryPresenter implements HistoryContract.PresenterInterface {
    private HistoryContract.ViewInterface viewInterface;
    private List<TripModel> tripsList;

    public HistoryPresenter(HistoryContract.ViewInterface viewInterface) {
        this.viewInterface = viewInterface;

        //Dumy data this come from db
//        tripsList = new ArrayList<>();
//        for (int i =0; i < 6; i++){
//            TripModel trip = new TripModel("id","Trip ("+i+") ","One Way Trip","ITI - Ismailia",
//                    "ITI - Smart Valige","20-02-2020","02:20 PM");
//            tripsList.add(trip);
//        }
    }


    @Override
    public void getTrips(String id) {
        Database.getInstance().getTripsHistoryForUser(id,this);
    }

    @Override
    public void setTrips(List<TripModel> trips) {
        tripsList = trips;
        if(tripsList != null && tripsList.size() > 0){
            viewInterface.displayTrips(tripsList);
        }else {
            viewInterface.displayNoTrips();
        }
    }

    @Override
    public void deleteTrip(String tripId,String userId) {
        Database.getInstance().deleteTripHistory(tripId,userId);
    }

    @Override
    public  List<TripModel> getTrips(){
        return tripsList;
    }

}
