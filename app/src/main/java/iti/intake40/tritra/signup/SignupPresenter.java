package iti.intake40.tritra.signup;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import iti.intake40.tritra.R;
import iti.intake40.tritra.model.Database;
import iti.intake40.tritra.model.UserModle;


public class SignupPresenter implements SignupContract.PresenterInterface {
SignupContract.ViewInterface viewInterface;
    FirebaseAuth mAuth;

    public SignupPresenter(SignupContract.ViewInterface viewInterface) {
        this.viewInterface = viewInterface;
    }

   @Override
    public void signUpUser(final UserModle user, String password) {
       if(viewInterface.isNetworkAvailable()){
       mAuth = FirebaseAuth.getInstance();
       viewInterface.showProgress();
       mAuth.createUserWithEmailAndPassword(user.getEmail(),password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
               if(task.isSuccessful()){
                   viewInterface.displayMessage(viewInterface.getMyConttext().getResources().getString(R.string.user_created_successfully));
                   viewInterface.displayMessage(viewInterface.getMyConttext().getResources().getString(R.string.enter_email_pass_to_login));
                  // startActivity(new Intent(getApplicationContext(), MainActivity.class));
                   user.setId(mAuth.getCurrentUser().getUid());
                   Database.getInstance().addUser(user);
                   viewInterface.redirectId(user.getId());

               }
               else{
                   viewInterface.displayMessage(viewInterface.getMyConttext().getResources().getString(R.string.user_already_exist));
                   viewInterface.showProgress();
               }

           }
       });
    }else{
           viewInterface.getMyConttext().getResources().getString(R.string.check_network_connection);
       }
    }


}
