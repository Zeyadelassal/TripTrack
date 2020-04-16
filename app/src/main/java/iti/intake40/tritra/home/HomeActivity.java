package iti.intake40.tritra.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import iti.intake40.tritra.history.HistoryFragment;
import iti.intake40.tritra.R;

public class HomeActivity extends AppCompatActivity //implements HomeContract.ViewInterface , CardMenuInterface{
{
    HomeContract.PresenterInterface presenter;
    LinearLayout noTripsLayout;
    FloatingActionButton fabAddTrip;
    RecyclerView recyclerView;
    String userId;
    public static final String USERID = "USERID";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_home);
        setContentView(R.layout.fragment_home);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.dynamic_fragment,new HomeFragment());
        //fragmentTransaction.replace(R.id.dynamic_fragment,new HistoryFragment());
        fragmentTransaction.commit();


//        presenter = new HomePresenter(this);
//
//        userId = getIntent().getStringExtra(USERID);
//        presenter.getTrips(userId);
//
//        noTripsLayout = findViewById(R.id.no_trips_layout);
//        recyclerView = findViewById(R.id.home_recyclerview);
//
//        fabAddTrip = findViewById(R.id.fab_add_trip);
//        fabAddTrip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(HomeActivity.this, "ADD TRIP UID=" + userId, Toast.LENGTH_SHORT).show();
//                TripModel trip = new TripModel();
//                Database.getInstance().addTrip(trip, userId);
//            }
//        });


    }

//    @Override
//    public void displayMessage(String msg) {
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void displayTrips(List<TripModel> tripsList) {
//        RecyclerAdapter adapter = new RecyclerAdapter(tripsList,this);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
//
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setVisibility(View.VISIBLE);
//        noTripsLayout.setVisibility(View.INVISIBLE);
//    }
//
//    @Override
//    public void displayNoTrips() {
//        //TODO: displayNoTrips
//        Toast.makeText(this, "NO TRIP!!!!!!", Toast.LENGTH_SHORT).show();
//        noTripsLayout.setVisibility(View.VISIBLE);
//        recyclerView.setVisibility(View.INVISIBLE);
//    }
//
//    @Override
//    public void onPopupMenuClick(View view, final int pos) {
//        PopupMenu popup = new PopupMenu(this, view);
//        MenuInflater inflater = popup.getMenuInflater();
//        inflater.inflate(R.menu.card_menu, popup.getMenu());
//        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.card_menu_edit:
//                        Toast.makeText(HomeActivity.this, "Menu EDIT"+pos, Toast.LENGTH_SHORT).show();
//                        presenter.editTrip(pos);
//                        return true;
//                    case R.id.card_menu_delete:
//                        Toast.makeText(HomeActivity.this, "Menu DELETE"+pos, Toast.LENGTH_SHORT).show();
//                        presenter.deleteTrip(pos, userId);
//                        return true;
//                    default:
//                        Toast.makeText(HomeActivity.this, "Menu Error", Toast.LENGTH_SHORT).show();
//                        return false;
//                }
//            }
//        });
//        popup.show();
//    }
}
