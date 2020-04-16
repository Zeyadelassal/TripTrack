package iti.intake40.tritra.login;

import android.content.Context;

public interface LoginContract {

    interface PresenterInterface {
        void loginUser(String email,String password);
    }

    interface ViewInterface {
        void displayMessage(String message);
        void  showProgress();
        void redirectId(String s,String id);
         void writeShredPreference(String id,String email);
        boolean isNetworkAvailable();
        Context getMyConttext();
    }
}
