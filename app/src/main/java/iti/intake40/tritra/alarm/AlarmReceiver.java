package iti.intake40.tritra.alarm;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import java.util.Calendar;;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.security.acl.LastOwnerException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import iti.intake40.tritra.add_trip.AddTripActivity;
import iti.intake40.tritra.floating_head.HeadService;
import iti.intake40.tritra.home.HomeFragment;
import iti.intake40.tritra.login.LoginActivity;
import iti.intake40.tritra.model.Database;
import iti.intake40.tritra.model.TripInterface;
import iti.intake40.tritra.model.TripModel;
import iti.intake40.tritra.model.UserTripsInterface;
import iti.intake40.tritra.notes.NoteActivity;

import static java.security.AccessController.getContext;

public class AlarmReceiver extends BroadcastReceiver implements TripInterface, UserTripsInterface {

    private static final String BOOT_COMPLETED =
            "android.intent.action.BOOT_COMPLETED";
    private static final String QUICKBOOT_POWERON =
            "android.intent.action.QUICKBOOT_POWERON";

    TripModel trip;
    String userId;
    String tripId;
    Context context;
    Intent intent;
    int notificationId;
    boolean roundTripGO;
    List<TripModel> tripsList;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Reached", Toast.LENGTH_LONG).show();
        System.out.println("reeeached");
        roundTripGO = false;
        this.context = context;
        this.intent = intent;
        String action = intent.getAction();
        if (BOOT_COMPLETED.equals(action) || QUICKBOOT_POWERON.equals(action)) {
            if(isUserLogged()){
                getUserId();
                getUserTrips();
            }
        } else {
            // getTrip();
            checkIntent(context, intent);
        }
    }

    private void setAlarms(Context context, List<TripModel> trips) {
            for(TripModel trip : trips){
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
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
                Intent tripAlarmIntent = new Intent(context, AlarmActivity.class);
                tripAlarmIntent.putExtra(HomeFragment.USERID,userId);
                tripAlarmIntent.putExtra(AddTripActivity.TRIP_ID,trip.getId());
                tripAlarmIntent.putExtra(AddTripActivity.TRIP_NAME,trip.getName());
                tripAlarmIntent.putExtra(AddTripActivity.TRIP_START_POINT,trip.getStartPoint());
                tripAlarmIntent.putExtra(AddTripActivity.TRIP_END_POINT,trip.getEndPoint());
                tripAlarmIntent.putExtra(AddTripActivity.ALARM_ID,alarmPendingIntentRequestCode);
                PendingIntent tripAlarmPendingIntent = PendingIntent.getActivity(context, alarmPendingIntentRequestCode, tripAlarmIntent, PendingIntent.FLAG_ONE_SHOT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    //Android kitkat or above
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),tripAlarmPendingIntent);
                }else{
                    //Android below kitkat
                    alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),tripAlarmPendingIntent);
                }
            }
    }

    private void checkIntent(Context context, Intent intent) {
        int notificationTag = intent.getIntExtra(AlarmActivity.NOTIFICATION_TAG, 0);
        notificationId = intent.getIntExtra(AlarmActivity.NOTIFICATION_ID, 0);
        userId = intent.getStringExtra(HomeFragment.USERID);
        tripId = intent.getStringExtra(AddTripActivity.TRIP_ID);
        if (notificationTag == 1) {
            getTrip();
        } else {
            Intent tripAlarmIntent = configAlarmIntent(context, intent);
            context.startActivity(tripAlarmIntent);
        }
    }

    private Intent configAlarmIntent(Context context, Intent inIntent) {
        Intent tripAlarmIntent = new Intent(context, AlarmActivity.class);
        userId = inIntent.getStringExtra(HomeFragment.USERID);
        tripId = inIntent.getStringExtra(AddTripActivity.TRIP_ID);
        String tripTitle = inIntent.getStringExtra(AddTripActivity.TRIP_NAME);
        String tripStartPoint = inIntent.getStringExtra(AddTripActivity.TRIP_START_POINT);
        String tripEndPoint = inIntent.getStringExtra(AddTripActivity.TRIP_END_POINT);
        Integer alarmId = inIntent.getIntExtra(AddTripActivity.ALARM_ID, 0);
        tripAlarmIntent.putExtra(HomeFragment.USERID, userId);
        tripAlarmIntent.putExtra(AddTripActivity.TRIP_ID, tripId);
        tripAlarmIntent.putExtra(AddTripActivity.TRIP_NAME, tripTitle);
        tripAlarmIntent.putExtra(AddTripActivity.TRIP_START_POINT, tripStartPoint);
        tripAlarmIntent.putExtra(AddTripActivity.TRIP_END_POINT, tripEndPoint);
        tripAlarmIntent.putExtra(AddTripActivity.ALARM_ID, alarmId);
        tripAlarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return tripAlarmIntent;
    }

    private void cancelNotification(int notificationId, Context context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationId);
    }

    private void performStartAction(Context context) {
        Intent mapsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + trip.getEndPoint()));
        mapsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(mapsIntent);
        if (trip.getType().equals(TripModel.TYPE.ROUND_TRIP) && trip.getStatus().equals(TripModel.STATUS.UPCOMING)) {
            trip.setStatus(TripModel.STATUS.GO);
            Database.getInstance().createRuturnTrip(trip, userId);
        } else {
            trip.setStatus(TripModel.STATUS.DONE);
            Database.getInstance().addTripHistory(trip, userId);
        }
        //Cancel Alarm
        Intent closeIntent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(closeIntent);
        if (isFloatingServiceRunning(HeadService.class, context)) {
            Intent closeServiceIntent = new Intent(context, HeadService.class);
            context.stopService(closeServiceIntent);
        }
        Intent serviceIntent = new Intent(context, HeadService.class);
        serviceIntent.putExtra(NoteActivity.TRIP_ID_KEY, tripId);
        context.startService(serviceIntent);

    }

    private void performEndAction(Context context) {
        trip.setStatus(TripModel.STATUS.CANCEL);
        Database.getInstance().addTripHistory(trip, userId);
    }

    public void getTrip() {
        Database.getInstance().getTripForEdit(userId, tripId, this);
    }

    @Override
    public void SetTripForEdit(TripModel trip) {
        if (trip != null && !roundTripGO) {
            roundTripGO = true;
            System.out.println("ROUNDTRIIIP" + trip.getStatus());
            this.trip = trip;
            cancelNotification(notificationId, context);
            int actionButtonId = intent.getIntExtra("ActionButtonId", 0);
            if (actionButtonId == 1) {
                performStartAction(context);
            } else {
                performEndAction(context);
            }
        }
    }

    private boolean isFloatingServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void getUserTrips() {
        Database.getInstance().getTripsForUser(userId, this);
    }

    private void getUserId() {
        SharedPreferences share = context.getSharedPreferences(LoginActivity.MYPREF, Context.MODE_PRIVATE);
        userId = share.getString("id", "0");
    }

    private boolean isUserLogged() {
        SharedPreferences share = context.getSharedPreferences(LoginActivity.MYPREF, Context.MODE_PRIVATE);
        return share.getBoolean("is_logged_before", false);
    }

    @Override
    public void setTrips(List<TripModel> trips) {
        if (trips != null && trips.size() > 0) {
            setAlarms(context, trips);
        }
    }
}
