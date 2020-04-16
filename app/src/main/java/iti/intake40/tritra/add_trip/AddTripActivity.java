package iti.intake40.tritra.add_trip;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.util.Arrays;
import java.util.Calendar;

import iti.intake40.tritra.R;
import iti.intake40.tritra.alarm.AlarmReceiver;
import iti.intake40.tritra.home.HomeFragment;
import iti.intake40.tritra.model.TripModel;

public class AddTripActivity extends AppCompatActivity implements AddTripContract.ViewInterface {
   // private String apiKey="AIzaSyCX00aiZAeqt9sbXM-0JGjk4evA54bKS6I"; //me
    //private String apiKey="AIzaSyBHn174_ktTUup-lFD_cO07b2cyx1_zmXE"; //zezo
    private String apiKey="AIzaSyBCXNUjza_-JQWSpFhvMgzpXQqgifH9qak"; //Awatef
    public static final String TAG = "TAG_AUTOSEARCH";
    public static final String TRIP_ID ="TRIP_ID";
    public static final String TRIP_NAME = "TRIP_NAME";
    public static final String TRIP_START_POINT = "TRIP_START_POINT";
    public static final String TRIP_END_POINT = "TRIP_END_POINT";
    public static final String ALARM_ID = "ALARM_ID";
    TextInputEditText txtTripName;
    Button btnSave;
    Button btnAddNoteCancel;
    Button btnDate;
    Button btnTime;
    MaterialTextView txtDate;
    MaterialTextView txtTime;
    Calendar calendar;
    AlarmManager alarmManager;
    int tripYear=-1;
    int tripMonth=-1;
    int tripDay=-1;
    int tripHour=-1;
    int tripMinute=-1;
    ToggleButton tglBtnTripType;
    Place startPoint;
    Place endPoint;
    NestedScrollView spinerContainerLayout;

    TripModel trip;
    String userId;

    int oldAlarmRequestId;


    AddTripContract.PresenterInterface presenterInterface;

    AutocompleteSupportFragment acfStartPoint;
    AutocompleteSupportFragment acfEndpoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        presenterInterface = new AddTripPresenter(this);
        userId = getIntent().getStringExtra(HomeFragment.USERID);
        calendar = Calendar.getInstance();

        txtTripName = findViewById(R.id.txt_trip_name);
        btnSave = findViewById(R.id.btn_save);
        btnAddNoteCancel = findViewById(R.id.btn_add_note_cancel);
        btnDate = findViewById(R.id.btn_date);
        btnTime = findViewById(R.id.btn_time);
        txtDate = findViewById(R.id.txt_date);
        txtTime = findViewById(R.id.txt_time);
        tglBtnTripType = findViewById(R.id.tgl_btn_trip_type);
        spinerContainerLayout = findViewById(R.id.spiner_container);

        if(getIntent().getStringExtra(TRIP_ID)== null)
            trip = new TripModel();
        else{
            presenterInterface.getTripForEdit(userId,getIntent().getStringExtra(TRIP_ID));
        }

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddTripActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                tripYear = year ;
                                tripMonth = monthOfYear;
                                tripDay = dayOfMonth;
                                txtDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        },calendar.get(Calendar.YEAR) , calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddTripActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                tripHour = hourOfDay;
                                tripMinute = minute;
                                txtTime.setText(hourOfDay + ":" + minute);
                            }
                        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
                timePickerDialog.show();
            }
        });


        tglBtnTripType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked)
                        trip.setType(TripModel.TYPE.ROUND_TRIP);
                    else
                        trip.setType(TripModel.TYPE.ONE_DIRECTION);
            }
        });

        btnAddNoteCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(txtTripName.getText())){
                    Snackbar.make(spinerContainerLayout, R.string.name_of_trip, Snackbar.LENGTH_SHORT).show();
                }else if(startPoint == null && getIntent().getStringExtra(TRIP_ID) == null){
                    Snackbar.make(spinerContainerLayout, R.string.start_ponit_empty, Snackbar.LENGTH_SHORT).show();
                }else if(endPoint == null && getIntent().getStringExtra(TRIP_ID) == null){
                    Snackbar.make(spinerContainerLayout, R.string.end_ponit_empty, Snackbar.LENGTH_SHORT).show();
                }else if(tripYear==-1 || tripMonth==-1 || tripDay==-1){
                    Snackbar.make(spinerContainerLayout, R.string.date_empty, Snackbar.LENGTH_SHORT).show();
                }else if(tripHour==-1 || tripMinute==-1) {
                    Snackbar.make(spinerContainerLayout, R.string.time_empty, Snackbar.LENGTH_SHORT).show();
                }else{
                    trip.setName(txtTripName.getText().toString());
                    trip.setDate(tripYear+"-"+tripMonth+"-"+tripDay);
                    trip.setTime(tripHour+":"+tripMinute);
                    if(trip.getStatus() == null)
                        trip.setStatus(TripModel.STATUS.UPCOMING);
                    if(getIntent().getStringExtra(TRIP_ID) == null){
                        trip.setStartPoint(startPoint.getName());
                        trip.setEndPoint(endPoint.getName());
                        presenterInterface.addTrip(trip,userId);
                    }
                    else{
                        if(startPoint != null)
                            trip.setStartPoint(startPoint.getName());
                        if(endPoint != null)
                            trip.setEndPoint(endPoint.getName());
                        presenterInterface.updateTrip(trip,userId);
                        //Todo: ZEYAD Clear Last Alarm !!
                        cancelAlarm();
                    }
                    createAlarm();
                    //Toast.makeText(AddTripActivity.this,"Alarm set on" + tripHour + tripMinute,Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });

        //StartPoint
        Places.initialize(getApplicationContext(), apiKey);
        PlacesClient placesClient = Places.createClient(this);
        acfStartPoint = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment_startpoint);
        acfStartPoint.setHint(getString(R.string.select_start_point));
        acfStartPoint.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        acfStartPoint.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                startPoint = place;
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(AddTripActivity.this, "Check your internet connection!", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "An error occurred: " + status);
            }
        });
        //EndPoint
        acfEndpoint = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment_endpoint);
        acfEndpoint.setHint(getString(R.string.select_end_point));
        acfEndpoint.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        acfEndpoint.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                endPoint = place;
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(AddTripActivity.this, "Check your internet connection!", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }


    private void createAlarm(){
        setCalender();
        Intent tripAlarmIntent = configAlarmIntent();
        int tripAlarmPendintgIntentRequestCode = generateCode();
        PendingIntent tripAlarmPendingIntent = PendingIntent.getBroadcast(AddTripActivity.this
                ,tripAlarmPendintgIntentRequestCode,
                tripAlarmIntent,
                PendingIntent.FLAG_ONE_SHOT);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //Android kitkat or above
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),tripAlarmPendingIntent);
        }else{
            //Android below kitkat
            alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),tripAlarmPendingIntent);
        }
    }

    private void cancelAlarm(){
        //Toast.makeText(AddTripActivity.this,"cancel alarm"+oldAlarmRequestId,Toast.LENGTH_LONG).show();
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent cancelAlarmIntent = new Intent(AddTripActivity.this, AlarmReceiver.class);
        PendingIntent cancelAlarmPendingIntent = PendingIntent.getBroadcast(AddTripActivity.this,
                oldAlarmRequestId,
                cancelAlarmIntent,
                PendingIntent.FLAG_ONE_SHOT);
        alarmManager.cancel(cancelAlarmPendingIntent);
    }

    private void setCalender(){
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(tripYear,tripMonth,tripDay,tripHour,tripMinute,0);
    }

    private Intent configAlarmIntent(){
        Intent tripAlarmIntent = new Intent(AddTripActivity.this, AlarmReceiver.class);

        tripAlarmIntent.putExtra(HomeFragment.USERID,userId);
        tripAlarmIntent.putExtra(TRIP_ID,trip.getId());
        tripAlarmIntent.putExtra(TRIP_NAME,trip.getName());
        tripAlarmIntent.putExtra(TRIP_START_POINT,trip.getStartPoint());
        tripAlarmIntent.putExtra(TRIP_END_POINT,trip.getEndPoint());
        tripAlarmIntent.putExtra(ALARM_ID,generateCode());
        return  tripAlarmIntent;
    }

    private int generateCode(){
        int generatedCode =Integer.parseInt(tripMonth+""+tripDay+""+tripHour+""+tripMinute);
        return generatedCode;
    }



    @Override
    public void SetTripForEdit(TripModel tripModel) {
        if(tripModel == null)
            this.trip = new TripModel();
        else{
            this.trip = tripModel;
            txtTripName.setText(trip.getName());
            txtTime.setText(trip.getTime());
            String[] date = trip.getDate().split("-");
            String[] time = trip.getTime().split(":");
            tripYear =Integer.parseInt(date[0]);
            tripMonth = Integer.parseInt(date[1]);
            tripDay = Integer.parseInt(date[2]);
            tripHour = Integer.parseInt(time[0]);
            tripMinute = Integer.parseInt(time[1]);
            calendar.set(tripYear,tripMonth,tripDay,tripHour,tripMinute);
            txtDate.setText(tripYear+"-"+(tripMonth+1)+"-"+tripDay);
            acfStartPoint.setText(trip.getStartPoint());
            acfEndpoint.setText(trip.getEndPoint());
            if(trip.getType().equals(TripModel.TYPE.ROUND_TRIP))
                tglBtnTripType.setChecked(true);
            else
                tglBtnTripType.setChecked(false);
            if(trip.getStatus().equals(TripModel.STATUS.GO))
                tglBtnTripType.setClickable(false);
            oldAlarmRequestId = generateCode();
        }
    }

}
