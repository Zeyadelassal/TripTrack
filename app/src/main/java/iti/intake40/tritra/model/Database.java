package iti.intake40.tritra.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import iti.intake40.tritra.add_trip.AddTripContract;
import iti.intake40.tritra.history.HistoryContract;
import iti.intake40.tritra.home.HomeContract;
import iti.intake40.tritra.notes.NotesContract;

public class Database {
    private FirebaseDatabase dbReference;

    private Database() {
        dbReference = FirebaseDatabase.getInstance();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    private static class SingletonHelper{
        private static final Database INSTANCE = new Database();
    }

    public static Database getInstance(){
        return SingletonHelper.INSTANCE;
    }

    public  void addUser(UserModle user){
        dbReference.getReference("user").child(user.getId()).setValue(user);
    }


    public void addTrip(TripModel trip,String userId){
        DatabaseReference databaseReference = dbReference.getReference("trip").child(userId);
        String id = databaseReference.push().getKey();
        trip.setId(id);
        databaseReference.child(id).setValue(trip);
        System.out.println("iiidTrip= "+id);
    }

    public void updateTrip(TripModel trip,String userId){
        DatabaseReference databaseReference = dbReference.getReference("trip").child(userId).child(trip.getId());
        databaseReference.setValue(trip);
    }

    public void createRuturnTrip(TripModel trip,String userId){
        DatabaseReference historyRef = dbReference.getReference("tripHistory").child(userId).child(trip.getId());
        historyRef.setValue(trip);

        String temp = trip.getStartPoint();
        trip.setStartPoint(trip.getEndPoint());
        trip.setEndPoint(temp);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR,7);
        trip.setDate(calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH)) + "-" + calendar.get(Calendar.DAY_OF_MONTH));

        DatabaseReference tripRef = dbReference.getReference("trip").child(userId).child(trip.getId());
        tripRef.setValue(trip);
    }

    public void deleteTrip(String tripId,String userId){
        DatabaseReference drTrip = dbReference.getReference("trip").child(userId).child(tripId);
        DatabaseReference drNote = dbReference.getReference("note").child(tripId);
        drNote.removeValue();
        drTrip.removeValue();
        System.out.println("remove Trip = "+tripId);
    }

    public void getTripsForUser(String userId, final UserTripsInterface presenter){

        dbReference.getReference("trip").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<TripModel> tripList = new ArrayList<>();
                for (DataSnapshot tripsnapshot: dataSnapshot.getChildren()){
                    TripModel trip =tripsnapshot.getValue(TripModel.class);
                    tripList.add(trip);
                }
                presenter.setTrips(tripList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.printf("onCancelled-DatabaseError");
                presenter.setTrips(new ArrayList<TripModel>());
            }
        });
        //homePresnter.setTrips(new ArrayList<TripModel>());
    }

    public void getTripForEdit(String userId, String tripId ,final TripInterface Presnter){

        dbReference.getReference("trip").child(userId).child(tripId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TripModel trip =dataSnapshot.getValue(TripModel.class);
                Presnter.SetTripForEdit(trip);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.printf("onCancelled-DatabaseError");
                Presnter.SetTripForEdit(null);
            }
        });
    }

    public void addNote(NoteModel note,String tripId){
        DatabaseReference databaseReference = dbReference.getReference("note").child(tripId);
        String id = databaseReference.push().getKey();
        note.setId(id);
        databaseReference.child(id).setValue(note);
        System.out.println("iiidnote= "+id);
    }

    public void updateNote(NoteModel note,String tripId){
        DatabaseReference databaseReference = dbReference.getReference("note").child(tripId).child(note.getId());
        databaseReference.setValue(note);
        System.out.println("iiidnote Update= "+note.getId());
    }

    public void getNotesForTrip(String tripId, final NotesPresenterInterface notePresenter){
        dbReference.getReference("note").child(tripId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<NoteModel> noteList = new ArrayList<>();
                for (DataSnapshot tripsnapshot: dataSnapshot.getChildren()){
                    NoteModel note =tripsnapshot.getValue(NoteModel.class);
                    noteList.add(note);
                    System.out.println("DaTABASE GET id = "+note.getId());
                }
                notePresenter.setAllNotes(noteList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                notePresenter.setAllNotes(new ArrayList<NoteModel>());
                System.out.printf("onCancelled-DatabaseError NOTE");
            }
        });
    }

    public void deleteNode(NoteModel note,String tripId){
        DatabaseReference drNote = dbReference.getReference("note").child(tripId).child(note.getId());
        drNote.removeValue();
        System.out.println("remove Note = "+tripId);
    }


    ////////////////////////////////////////////////////////////////////////////
    public void addTripHistory(TripModel trip,String userId){
        DatabaseReference databaseReference = dbReference.getReference("tripHistory").child(userId).child(trip.getId());
        databaseReference.setValue(trip);

        DatabaseReference drTrip = dbReference.getReference("trip").child(userId).child(trip.getId());
        drTrip.removeValue();
    }

    public void deleteTripHistory(String tripId,String userId){
        DatabaseReference drTrip = dbReference.getReference("tripHistory").child(userId).child(tripId);
        DatabaseReference drNote = dbReference.getReference("note").child(tripId);
        drNote.removeValue();
        drTrip.removeValue();
        System.out.println("remove TripHistory = "+tripId);
    }

    public void getTripsHistoryForUser(String userId, final HistoryContract.PresenterInterface historyPresnter){

        dbReference.getReference("tripHistory").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<TripModel> tripList = new ArrayList<>();
                for (DataSnapshot tripsnapshot: dataSnapshot.getChildren()){
                    TripModel trip =tripsnapshot.getValue(TripModel.class);
                    tripList.add(trip);
                }
                historyPresnter.setTrips(tripList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.printf("onCancelled-DatabaseError");
                historyPresnter.setTrips(new ArrayList<TripModel>());
            }
        });
    }


}
