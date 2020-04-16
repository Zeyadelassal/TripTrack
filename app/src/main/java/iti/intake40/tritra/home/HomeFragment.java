package iti.intake40.tritra.home;


import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

import iti.intake40.tritra.MainActivity;
import iti.intake40.tritra.R;
import iti.intake40.tritra.add_trip.AddTripActivity;
import iti.intake40.tritra.alarm.AlarmActivity;
import iti.intake40.tritra.alarm.AlarmReceiver;
import iti.intake40.tritra.floating_head.HeadService;
import iti.intake40.tritra.login.LoginActivity;
import iti.intake40.tritra.model.Database;
import iti.intake40.tritra.model.TripModel;
import iti.intake40.tritra.notes.NoteActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements HomeContract.ViewInterface , CardMenuInterface{

    HomeContract.PresenterInterface presenter;
    LinearLayout noTripsLayout;
    FloatingActionButton fabAddTrip;
    RecyclerView recyclerView;
    String userId;
    public static final String USERID = "USERID";



    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_home, container, false);

        presenter = new HomePresenter(this);

        userId = getActivity().getIntent().getStringExtra(USERID);
        presenter.getTrips(userId);

        noTripsLayout = root.findViewById(R.id.no_trips_layout);
        recyclerView = root.findViewById(R.id.history_recyclerview);
        //recyclerView = root.findViewById(R.id.home_recyclerview);

        fabAddTrip = root.findViewById(R.id.fab_add_trip);
        fabAddTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "ADD TRIP UID=" + userId, Toast.LENGTH_SHORT).show();
                //TripModel trip = new TripModel();
                //Database.getInstance().addTrip(trip, userId);
                Intent intent = new Intent(getContext(), AddTripActivity.class);
                intent.putExtra(HomeFragment.USERID,userId);
                startActivity(intent);
            }
        });
        return root;

    }

    @Override
    public void displayMessage(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayTrips(List<TripModel> tripsList) {
        RecyclerAdapter adapter = new RecyclerAdapter(tripsList,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.VISIBLE);
        noTripsLayout.setVisibility(View.INVISIBLE);
        if(getActivity()!= null && LoginActivity.FROM_LOGIN.equals(getActivity().getIntent().getStringExtra(LoginActivity.FROM_LOGIN))){
            for(TripModel trip : tripsList){
                String[] dateParams = trip.getDate().split("-");
                String[]timeParams = trip.getTime().split(":");
                int tripYear = Integer.parseInt(dateParams[0]);
                int tripMonth = Integer.parseInt(dateParams[1]);
                int tripDay = Integer.parseInt(dateParams[2]);
                int tripHour = Integer.parseInt(timeParams[0]);
                int tripMinute = Integer.parseInt(timeParams[1]);
                int alarmPendingIntentRequestCode = Integer.parseInt(dateParams[1]+dateParams[2]+timeParams[0]+timeParams[1]);
                Calendar calendar = Calendar.getInstance();
                calendar.set(tripYear, tripMonth, tripDay, tripHour, tripMinute
                        , 0);
                AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(getContext().ALARM_SERVICE);
                Intent tripAlarmIntent = new Intent(getContext(), AlarmActivity.class);
                tripAlarmIntent.putExtra(HomeFragment.USERID,userId);
                tripAlarmIntent.putExtra(AddTripActivity.TRIP_ID,trip.getId());
                tripAlarmIntent.putExtra(AddTripActivity.TRIP_NAME,trip.getName());
                tripAlarmIntent.putExtra(AddTripActivity.TRIP_START_POINT,trip.getStartPoint());
                tripAlarmIntent.putExtra(AddTripActivity.TRIP_END_POINT,trip.getEndPoint());
                tripAlarmIntent.putExtra(AddTripActivity.ALARM_ID,alarmPendingIntentRequestCode);
                PendingIntent tripAlarmPendingIntent = PendingIntent.getActivity(getContext(), alarmPendingIntentRequestCode, tripAlarmIntent, PendingIntent.FLAG_ONE_SHOT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    //Android kitkat or above
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),tripAlarmPendingIntent);
                }else{
                    //Android below kitkat
                    alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),tripAlarmPendingIntent);
                }
            }
        }
    }

    @Override
    public void displayNoTrips() {
        //TODO: displayNoTrips
        //Toast.makeText(getContext(), "NO TRIP!!!!!!", Toast.LENGTH_SHORT).show();
        noTripsLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPopupMenuClick(View view, final int pos) {
        PopupMenu popup = new PopupMenu(getContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.card_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.card_menu_edit:
                        //Toast.makeText(getContext(), "Menu EDIT"+pos, Toast.LENGTH_SHORT).show();
                        presenter.editTrip(pos);
                        return true;
                    case R.id.card_menu_delete:
                        //Toast.makeText(getContext(), "Menu DELETE"+pos, Toast.LENGTH_SHORT).show();
                        deleteTrip(pos);
                        return true;
                    default:
                        //Toast.makeText(getContext(), "Menu Error", Toast.LENGTH_SHORT).show();
                        return false;
                }
            }
        });
        popup.show();
    }

    @Override
    public void openNotes(String tripId) {
        Intent intent=new Intent(getContext(), NoteActivity.class);
        intent.putExtra(NoteActivity.TRIP_ID_KEY,tripId);
        startActivity(intent);
    }

    @Override
    public void startTrip(TripModel trip) {
        //Intent mapsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + 48.860294 + "," + 2.338629 + "&daddr=" + 48.858093 + "," + 2.294694));
        Intent mapsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + trip.getEndPoint()));
        mapsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mapsIntent);
        cancelTripAlarm(trip);
        if(trip.getType().equals(TripModel.TYPE.ROUND_TRIP) && trip.getStatus().equals(TripModel.STATUS.UPCOMING )){
            trip.setStatus(TripModel.STATUS.GO);
            presenter.createRuturnTrip(trip,userId);
        }else{
            trip.setStatus(TripModel.STATUS.DONE);
            presenter.moveTripToHistory(trip,userId);
        }
        if(isFloatingServiceRunning(HeadService.class)){
            Intent closeServiceIntent = new Intent(getContext(), HeadService.class);
            getContext().stopService(closeServiceIntent);
        }
        Intent serviceIntent = new Intent(getContext(), HeadService.class);
        serviceIntent.putExtra(NoteActivity.TRIP_ID_KEY,trip.getId());
        getActivity().startService(serviceIntent);
    }

    @Override
    public void openTripActivityForEdit(String tripId){
        Intent intent = new Intent(getContext(), AddTripActivity.class);
        intent.putExtra(HomeFragment.USERID,userId);
        intent.putExtra(AddTripActivity.TRIP_ID,tripId);
        startActivity(intent);
    }


    public void deleteTrip(final int pos) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle(R.string.delete_warning);
        alertDialogBuilder
                .setIcon(R.drawable.ic_close)
                .setMessage(R.string.delete_question)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        presenter.deleteTrip(pos, userId);

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

    @Override
    public void cancelTripAlarm(TripModel trip){
        String[] dateParams = trip.getDate().split("-");
        String[]timeParams = trip.getTime().split(":");
        int alarmPendingIntentRequestCode = Integer.parseInt(dateParams[1]+dateParams[2]+timeParams[0]+timeParams[1]);
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(getContext().ALARM_SERVICE);
        Intent cancelAlarmIntent = new Intent(getContext(), AlarmReceiver.class);
        PendingIntent cancelAlarmPendingIntent = PendingIntent.getBroadcast(getContext(),
                                                    alarmPendingIntentRequestCode,
                                                        cancelAlarmIntent,
                                                        PendingIntent.FLAG_ONE_SHOT);
        alarmManager.cancel(cancelAlarmPendingIntent);
        NotificationManager manager = (NotificationManager) getContext().getSystemService(getContext().NOTIFICATION_SERVICE);
        manager.cancel(alarmPendingIntentRequestCode);
    }

    private boolean isFloatingServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getContext().getSystemService(getContext().ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
