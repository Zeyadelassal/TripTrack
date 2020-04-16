package iti.intake40.tritra.signup;

import android.content.Context;

import iti.intake40.tritra.model.UserModle;

public interface SignupContract {

    interface PresenterInterface {
 void signUpUser(UserModle user, String password);


    }

    interface ViewInterface {
        void displayMessage(String message);
        void redirectId(String s);
         void showProgress();
        boolean isNetworkAvailable();
        Context getMyConttext();
    }
}
