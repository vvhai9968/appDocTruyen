package vuvanhai568.project.appDocTruyen.SignIn_SignUp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import vuvanhai568.project.appDocTruyen.MainActivity;
import vuvanhai568.project.appDocTruyen.Modal.User;
import vuvanhai568.project.appDocTruyen.Modal.getLinkAPI;
import vuvanhai568.project.appDocTruyen.R;
import vuvanhai568.project.appDocTruyen.SeverDB;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignIn extends AppCompatActivity {
    TextInputEditText username,pass;
    TextInputLayout checkUser,checkPass;
    CardView signIn;
    TextView signUp,err;
    Retrofit retrofit;
    SeverDB serverDB;
    private String API = getLinkAPI.API;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        username = findViewById(R.id.signInUser);
        pass = findViewById(R.id.signInPass);
        checkUser = findViewById(R.id.checkUserSignIn);
        checkPass = findViewById(R.id.checkpassSignIn);

        err = findViewById(R.id.signinNotification);
        signIn = findViewById(R.id.SignIn);
        signUp = findViewById(R.id.txtSignUp);

        retrofit = new Retrofit.Builder()
                .baseUrl(API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        serverDB = retrofit.create(SeverDB.class);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!User() | !Pass()){
                    return;
                }else {
                    User user = new User(username.getText().toString(), pass.getText().toString());
                    Call<User>call =serverDB.signIn(user);
                    
                     call.enqueue(new Callback<User>() {
                         @Override
                         public void onResponse(Call<User> call, Response<User> response) {
                             User userResponse = response.body();
                             String message = userResponse.getMessage();
                             String username1 = username.getText().toString();

                              if ("Done".equals(message)){

                                  SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
                                  SharedPreferences.Editor editor = sharedPreferences.edit();

                                  editor.putString("username", username1);
                                  editor.putString("name", response.body().getUser());

                                  editor.apply();
                                  Intent i = new Intent(SignIn.this,MainActivity.class);
                                  startActivity(i);
                              }else {
                                  err.setText(message);
                              }
                         }

                         @Override
                         public void onFailure(Call<User> call, Throwable t) {

                         }
                     });
                }

            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),SignUp.class);
                startActivity(i);
                finish();
            }
        });


    }

    private boolean Pass() {

        String pass = checkPass.getEditText().getText().toString().trim();
        if (pass.isEmpty()) {
            checkPass.setError(" ");
            return false;
        } else {
            checkPass.setError(null);
            return true;
        }
    }

    private boolean User() {
        String user = checkUser.getEditText().getText().toString().trim();
        if (user.isEmpty()) {
            checkUser.setError(" ");
            return false;
        } else {
            checkUser.setError(null);
            return true;
        }
    }
}