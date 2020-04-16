package iti.intake40.tritra;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import iti.intake40.tritra.navigation.NavigationDraw;
import iti.intake40.tritra.home.HomeActivity;
import iti.intake40.tritra.login.LoginActivity;

public class Splash extends AppCompatActivity {
  /*  static {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences mPrefs = getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE);
                if (mPrefs.getBoolean("is_logged_before",false))  {
                    Intent intent = new Intent(getApplicationContext(), NavigationDraw.class);
                    intent.putExtra(HomeActivity.USERID,mPrefs.getString("id","id"));
                    intent.putExtra(NavigationDraw.EMAil,mPrefs.getString("email","email"));
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        }, 2000);

    }
    }

