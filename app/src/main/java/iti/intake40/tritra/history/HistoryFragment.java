package iti.intake40.tritra.history;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import iti.intake40.tritra.HistoryMap;
import iti.intake40.tritra.R;
import iti.intake40.tritra.history_note_dialog.NoteDialogActivity;
import iti.intake40.tritra.home.HomeFragment;
import iti.intake40.tritra.model.TripModel;
import iti.intake40.tritra.notes.NoteActivity;


public class HistoryFragment extends Fragment implements HistoryContract.ViewInterface, NoteInterface{
    HistoryContract.PresenterInterface presenter;
    LinearLayout noTripsLayout;
    RecyclerView recyclerView;
    String userId;

    public HistoryFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_history, container, false);

        presenter = new HistoryPresenter(this);

        userId = getActivity().getIntent().getStringExtra(HomeFragment.USERID);
        presenter.getTrips(userId);

        noTripsLayout = root.findViewById(R.id.no_trips_layout);
        recyclerView = root.findViewById(R.id.history_recyclerview);
        Button btnmap = getActivity().findViewById(R.id.toolbar_btn_history_map);
        btnmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(), "MapClick", Toast.LENGTH_SHORT).show();
                openGoogleMap();
            }
        });
        return root;
    }

    @Override
    public void displayTrips(List<TripModel> tripsList) {
        HistoryRecyclerAdapter adapter = new HistoryRecyclerAdapter(tripsList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.VISIBLE);
        noTripsLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void displayNoTrips() {
        //TODO: displayNoTrips
       // Toast.makeText(getContext(), "NO TRIP!!!!!!", Toast.LENGTH_SHORT).show();
        noTripsLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void displayMessage(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void openNote(String tripId) {
        Intent intent = new Intent(getContext(), NoteDialogActivity.class);
        intent.putExtra(NoteActivity.TRIP_ID_KEY,tripId);
        //intent.putExtra(NoteDialogActivity.CLICKABLE,"Click"); //put this to can make check
        startActivity(intent);
    }

    public void openGoogleMap(){
        ArrayList<String>startPoints=new ArrayList<>();
        ArrayList<String>endPoints=new ArrayList<>();
        List<TripModel>trips=presenter.getTrips();
        if(trips==null||trips.size()==0){
            displayMessage(getResources().getString(R.string.no_trips_history));
        }else{
            for(TripModel trip:trips){
                startPoints.add(trip.getStartPoint());
                endPoints.add(trip.getEndPoint());
            }
            Intent intent=new Intent(getActivity(), HistoryMap.class);
            intent.putStringArrayListExtra(HistoryMap.START_POINTS,startPoints);
            intent.putStringArrayListExtra(HistoryMap.END_POINT,endPoints);
            startActivity(intent);
        }
    }

    @Override
    public void deleteTrip(final String tripId) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle(R.string.delete_warning);
        alertDialogBuilder
                .setIcon(R.drawable.ic_close)
                .setMessage(R.string.delete_question)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        presenter.deleteTrip(tripId,userId);
                    }
                })
                .setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
