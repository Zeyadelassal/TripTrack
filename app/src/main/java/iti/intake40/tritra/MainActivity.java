package iti.intake40.tritra;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import iti.intake40.tritra.navigation.NavigationDraw;
import iti.intake40.tritra.add_trip.AddTripActivity;
import iti.intake40.tritra.home.HomeActivity;
import iti.intake40.tritra.home.HomeFragment;
import iti.intake40.tritra.login.LoginActivity;
import iti.intake40.tritra.model.NoteModel;
import iti.intake40.tritra.model.TripModel;
import iti.intake40.tritra.model.UserModle;
import iti.intake40.tritra.notes.NoteActivity;
import iti.intake40.tritra.signup.SignUp;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Mazen

        ///////////

        //Zeyad

        /////////

        //Awatef
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        signup=findViewById(R.id.next);
        login=findViewById(R.id.button);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(MainActivity.this, SignUp.class);
                i.putExtra(HomeFragment.USERID,"id5");
                startActivity(i);


            }
        });
        ///////
    }
    ///////////////////////////////
    //Mazen
    List<NoteModel> noteList = noteList = new ArrayList<>();
    List<UserModle> userModleList = new ArrayList<>();
    public void openHome(View view) {
        Toast.makeText(this, "Count= "+noteList.size(), Toast.LENGTH_SHORT).show();

       Intent intent = new Intent(MainActivity.this, NavigationDraw.class);
       intent.putExtra(HomeActivity.USERID,"id5");
       startActivity(intent);

        UserModle userModle = new UserModle();
        userModle.setId("id6");
//        Database.getInstance().addUser(userModle);

        TripModel trip = new TripModel();
        //Database.addTrip(trip,userModle.getId());

        NoteModel note = new NoteModel();
//        Database.getInstance().addNote(note,"-M1XhxGfsBCjY_EEHXGA");

//        List<NoteModel> notes = Database.getNotesForTrip("-M1XhxGfsBCjY_EEHXGA");
//        for (NoteModel noteModel : notes){
//            System.out.println("NNN= "+ note.getId());
//        }


//
//        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("note").child("-M1XhxGfsBCjY_EEHXGA");
//        System.out.println("NNN Start");
//        dbReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot tripsnapshot: dataSnapshot.getChildren()){
//                    NoteModel note =tripsnapshot.getValue(NoteModel.class);
//                    noteList.add(note);
//                    System.out.println("NNN onDataChange = " + note.getId());
//                }
//                System.out.println("NNN onDataChange Finish");
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                System.out.println("NNN onCancelled-DatabaseError NOTE");
//            }
//        });

         //noteList = Database.getNotesForTrip("-M1XhxGfsBCjY_EEHXGA");
        //System.out.println("Main Count=" +noteList.size());

//        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("user");
//        System.out.println("NNN Start");
//        dbReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot tripsnapshot: dataSnapshot.getChildren()){
//                    UserModle user =tripsnapshot.getValue(UserModle.class);
//                    userModleList.add(user);
//                    System.out.println("NNN onDataChange = " + user.getId());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                System.out.println("NNN onCancelled-DatabaseError NOTE");
//            }
//        });

    }


    public void addTrip(View view) {
        Intent id=new Intent(MainActivity.this, AddTripActivity.class);
        startActivity(id);
    }
    ///////////

    //Zeyad

    /////////

    //Awatef
    Button signup,login;
    public void signIn(View view) {
        Intent i=new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
    }

    public void AddNote(View view) {
        Intent intent=new Intent(MainActivity.this, NoteActivity.class);
        intent.putExtra(NoteActivity.TRIP_ID_KEY,"-M1pmfUjMRPGHlG5SKow");
        startActivity(intent);
    }
    ///////
}
