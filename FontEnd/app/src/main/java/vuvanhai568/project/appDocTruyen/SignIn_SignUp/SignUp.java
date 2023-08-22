package vuvanhai568.project.appDocTruyen.SignIn_SignUp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

import vuvanhai568.project.appDocTruyen.Modal.User;
import vuvanhai568.project.appDocTruyen.Modal.getLinkAPI;
import vuvanhai568.project.appDocTruyen.R;
import vuvanhai568.project.appDocTruyen.SeverDB;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUp extends AppCompatActivity {
    TextInputEditText editFullname, editEmail, editUser, editPass, editPassAgain;
    TextInputLayout checkFullname, checkuser, checkEmail, checkPass, checkPassAgain;
    CardView submit;
    TextView err, err2, signIn;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //ít nhất 1 số
                    "(?=.*[a-z])" +         //ít nhất 1 chữ thường
                    "(?=.*[A-Z])" +         //ít nhất 1 chữ hoa
                    "(?=\\S+$)" +           //không khoảng trắng
                    ".{4,}" +               //tối thiểu 4 kí tự
                    "$");
    private static final Pattern userName =
            Pattern.compile("^" +
                    ".{6,}" +               //tối thiểu 6 kí tự
                    "$");
    private String API = getLinkAPI.API;
    Retrofit retrofit;
    SeverDB serverDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editFullname = findViewById(R.id.SignUpFullname);
        editEmail = findViewById(R.id.SignUpEmail);
        editUser = findViewById(R.id.SignUpUser);
        editPass = findViewById(R.id.SignUpPass);
        editPassAgain = findViewById(R.id.SignUpAgainPass);

        err = findViewById(R.id.errSignUp);
        err2 = findViewById(R.id.errSignUp2);
        signIn = findViewById(R.id.txtSignIn);

        checkFullname = findViewById(R.id.checkFullname);
        checkuser = findViewById(R.id.checkUser);
        checkEmail = findViewById(R.id.CheckEmail);
        checkPass = findViewById(R.id.CheckPass);
        checkPassAgain = findViewById(R.id.checkPassAgain);

        submit = findViewById(R.id.btnSignUp);
        retrofit = new Retrofit.Builder()
                .baseUrl(API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        serverDB = retrofit.create(SeverDB.class);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!User() | !YourEmail() | !nameDN() | !pass() | !chekpassagain()) {
                    return;
                } else {
                    User user = new User(
                            editUser.getText().toString(),
                            editEmail.getText().toString(),
                            editFullname.getText().toString(),
                            editPass.getText().toString());
                    Call<User> call = serverDB.signUp(user);

                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            User userResponse = response.body();
                            String message = userResponse.getMessage();
                            if ("Done".equals(message)){
                                Intent i = new Intent(SignUp.this,SignIn.class);
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

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SignIn.class);
                startActivity(i);
                finish();
            }
        });

    }

    private boolean chekpassagain() {
        String PassAgain = checkPassAgain.getEditText().getText().toString().trim();
        String Pass = checkPass.getEditText().getText().toString().trim();
        if (PassAgain.isEmpty()) {
            checkPassAgain.setError(" ");
            return false;
        } else if (!Pass.equals(PassAgain)) {
            checkPassAgain.setError(" ");
            return false;
        } else {
            checkPassAgain.setError(null);
            return true;
        }
    }

    private boolean pass() {
        String b = checkPass.getEditText().getText().toString().trim();
        if (b.isEmpty()) {
            checkPass.setError(" ");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(b).matches()) {
            checkPass.setError(" ");
            err2.setText("Pass phải tối thiểu 4 ký tự 1 số 1 hoa 1 thường");
            return false;
        } else {
            checkPass.setError(null);
            err2.setText("");
            return true;
        }
    }


    private boolean nameDN() {
        String User = checkuser.getEditText().getText().toString().trim();
        if (User.isEmpty()) {
            checkuser.setError(" ");
            return false;
        } else if (!userName.matcher(User).matches()) {
            checkuser.setError(" ");
            err2.setText("Tên đăng nhập tối thiểu 6 ký tự ");
            return false;
        } else {
            checkuser.setError(null);
            err2.setText("");
            return true;
        }
    }

    private boolean YourEmail() {
        String d = checkEmail.getEditText().getText().toString().trim();
        if (d.isEmpty()) {
            checkEmail.setError(" ");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(d).matches()) {
            checkEmail.setError(" ");
            err2.setText("Email sai định dạng");
            return false;
        } else {
            checkEmail.setError(null);
            err2.setText("");
            return true;
        }
    }

    private boolean User() {
        String fullname = checkFullname.getEditText().getText().toString().trim();
        if (fullname.isEmpty()) {
            checkFullname.setError(" ");
            return false;
        } else {
            checkFullname.setError(null);
            return true;
        }
    }
}