package iti.intake40.tritra.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import iti.intake40.tritra.R;


public class LoginPresenter implements LoginContract.PresenterInterface {
LoginContract.ViewInterface viewInterface;
    public FirebaseAuth mAuth;


    public LoginPresenter(LoginContract.ViewInterface viewInterface) {
        this.viewInterface = viewInterface;
    }

    @Override
    public void loginUser(String email, String password) {
        if(viewInterface.isNetworkAvailable()){
        mAuth = FirebaseAuth.getInstance();
        viewInterface.showProgress();
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String id = mAuth.getCurrentUser().getUid();
                    String email = mAuth.getCurrentUser().getEmail();
                    System.out.println(email);
                    viewInterface.displayMessage(viewInterface.getMyConttext().getResources().getString(R.string.user_logined_successfully));
                    viewInterface.writeShredPreference(id,email);
                    viewInterface.showProgress();
                    viewInterface.redirectId(email,id);
                }
                else{
                    viewInterface.showProgress();
                    viewInterface.displayMessage(viewInterface.getMyConttext().getResources().getString(R.string.username_and_password_doesnot_matches));

                }

            }
        });
    }
    else{
        viewInterface.displayMessage(viewInterface.getMyConttext().getResources().getString(R.string.check_network_connection));
    }
    }


}
