package iti.intake40.tritra.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import iti.intake40.tritra.navigation.NavigationDraw;
import iti.intake40.tritra.R;
import iti.intake40.tritra.home.HomeFragment;
import iti.intake40.tritra.model.Database;
import iti.intake40.tritra.model.UserModle;
import iti.intake40.tritra.signup.SignUp;

public class LoginActivity extends AppCompatActivity implements LoginContract.ViewInterface {
    private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;
    LoginContract.PresenterInterface presenterInterface;
    ImageView img;
    EditText email,password;
    Button login,face;
    TextView signup;
    ProgressBar progressBar;
    FirebaseDatabase database ;
    DatabaseReference myRef ;
    public static final String MYPREF="myAppPrefs";
    public static final String FROM_LOGIN = "fromlogin";
    SharedPreferences share;
    @Override
    protected void onStart() {
        super.onStart();
        presenterInterface=new LoginPresenter(this);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       /* DatabaseReference loginRef = FirebaseDatabase.getInstance().getReference("user");
        loginRef.keepSynced(true);*/
        setupViews();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isValidate();


            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this, SignUp.class);
                startActivity(intent);
            }
        });
        mAuth = FirebaseAuth.getInstance();
        registerLoginButton();

    }
    private void setupViews(){
        img=findViewById(R.id.img);
        email=findViewById(R.id.name_txt);
        password=findViewById(R.id.email);
        login=findViewById(R.id.login_btn);
        face=findViewById(R.id.face_btn);
        signup=findViewById(R.id.link_txv);
        progressBar=findViewById(R.id.progressBar);
    }

    public void isValidate() {

        String email_str = email.getText().toString().trim();
        String password_str = password.getText().toString().trim();

        if (TextUtils.isEmpty(email_str)) {
            email.setError(getResources().getString(R.string.email_is_required));
            return;
        }
        if (TextUtils.isEmpty(password_str)) {
            password.setError(getResources().getString(R.string.password_is_required));
            return;
        }
        if (password_str.length() < 6) {
            password.setError(getResources().getString(R.string.password_character));
            password.setText("");
            return;
        }
        presenterInterface.loginUser(email_str, password_str);


    }
    @Override
    public void displayMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


    public void registerLoginButton(){

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.face_btn);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //displayMessage("AWATEF LOGGED");
                Log.d("FACE", "facebook:onSuccess:");
//                database = FirebaseDatabase.getInstance();
//                myRef = database.getReference("users");
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                displayMessage(getResources().getString(R.string.fb_reject_request));
                Log.d("FACE", "facebook:onCancel" );
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                displayMessage(getResources().getString(R.string.check_network_connection));
                Log.d("FACE", "facebook:onError", error);
                // ...
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

   /* private void updateUI(FirebaseUser user){
        if(user != null){
            Toast.makeText(LoginActivity.this,"Logged_in id =" + user.getUid(),Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(LoginActivity.this,"Fail to login",Toast.LENGTH_LONG).show();
        }
    }*/

    private void handleFacebookAccessToken(AccessToken token) {


        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            ////
                            UserModle userModle = new UserModle(user.getUid(),user.getDisplayName(),user.getEmail());
                            Database.getInstance().addUser(userModle);
                            ////
                            String id = mAuth.getCurrentUser().getUid();
                            String email = mAuth.getCurrentUser().getEmail();
                            System.out.println(email);

                            writeShredPreference(id,email);
                            showProgress();
                            redirectId(email,id);
                            ////updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Face", "signInWithCredential:failure", task.getException());

                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    @Override
    public void writeShredPreference(String id,String email) {
        //mPrefs = mContext.getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE);
        //SharedPreferences.Editor editor = mPrefs.edit();
        //editor.putString("userId", result.get("uid"));
        //editor.putBoolean("is_logged_before",true); //this line will do trick
        //editor.commit();
        share=getSharedPreferences(MYPREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=share.edit();
        editor.putString("id",id);
        editor.putString("email",email);
        editor.putBoolean("is_logged_before",true);
        editor.commit();

    }

    @Override
    public void redirectId(String s,String id) {
        Intent intent=new Intent(LoginActivity.this, NavigationDraw.class);
        intent.putExtra(HomeFragment.USERID,id);
        intent.putExtra(NavigationDraw.EMAil,s);
        intent.putExtra(FROM_LOGIN,FROM_LOGIN);
        startActivity(intent);
        finish();
    }

    @Override
    public void showProgress() {
        if (progressBar.getVisibility() == View.VISIBLE)
            progressBar.setVisibility(View.INVISIBLE);
        else
            progressBar.setVisibility(View.VISIBLE);
    }
    @Override
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public Context getMyConttext() {
        return getApplicationContext();
    }

}
