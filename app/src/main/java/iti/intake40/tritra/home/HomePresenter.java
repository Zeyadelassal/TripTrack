package iti.intake40.tritra.home;

import java.util.ArrayList;
import java.util.List;

import iti.intake40.tritra.model.Database;
import iti.intake40.tritra.model.TripModel;

public class HomePresenter implements HomeContract.PresenterInterface {

    private HomeContract.ViewInterface viewInterface;
    private List<TripModel> tripsList;

    public HomePresenter(HomeContract.ViewInterface viewInterface) {
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
        Database.getInstance().getTripsForUser(id,this);
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
    public void editTrip(int pos) {
        viewInterface.openTripActivityForEdit(tripsList.get(pos).getId());
        //viewInterface.displayMessage("Done Edit"+tripsList.get(pos).getName());
    }

    @Override
    public void deleteTrip(int pos,String userId) {
        //ToDO: Delete Trip
        System.out.println("NOTE pos= " +  pos);
        viewInterface.cancelTripAlarm(tripsList.get(pos));
        Database.getInstance().deleteTrip(tripsList.get(pos).getId(),userId);
        //tripsList.remove(tripsList.get(pos));
        //getTrips(userId);
        //viewInterface.displayMessage("Done Delete"+tripsList.get(pos).getName());
    }

    @Override
    public void moveTripToHistory(TripModel tripModel, String userId) {
        //Database.getInstance().deleteTrip(tripModel.getId(),userId);
        Database.getInstance().addTripHistory(tripModel,userId);
    }

    @Override
    public void createRuturnTrip(TripModel trip, String userId) {
        Database.getInstance().createRuturnTrip(trip,userId);
    }
}
