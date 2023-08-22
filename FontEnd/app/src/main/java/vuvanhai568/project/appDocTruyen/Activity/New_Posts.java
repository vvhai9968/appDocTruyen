package vuvanhai568.project.appDocTruyen.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Locale;

import vuvanhai568.project.appDocTruyen.MainActivity;
import vuvanhai568.project.appDocTruyen.Modal.Book;
import vuvanhai568.project.appDocTruyen.Modal.getLinkAPI;
import vuvanhai568.project.appDocTruyen.R;
import vuvanhai568.project.appDocTruyen.SeverDB;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class New_Posts extends AppCompatActivity {


    EditText linkAnh, tieuDe, noiDung;
    Button btnAdd;
    private String API = getLinkAPI.API;
    Retrofit retrofit;
    SeverDB severDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_posts);
        back();

        linkAnh = findViewById(R.id.linkAnh);
        tieuDe = findViewById(R.id.tieuDe);
        noiDung = findViewById(R.id.noiDung);
        btnAdd = findViewById(R.id.btnAddPost);

        retrofit = new Retrofit.Builder()
                .baseUrl(API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        severDB = retrofit.create(SeverDB.class);
        SharedPreferences sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
        String username1 = sharedPreferences.getString("username", "");
        String name = sharedPreferences.getString("name", "");

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = tieuDe.getText().toString();
                String content = noiDung.getText().toString();
                String img = linkAnh.getText().toString();

                Calendar myCalendar = Calendar.getInstance();//lấy ngày hiện tại 
                // Format ngày hiện tại thành một chuỗi "dd/MM/yyyy"
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String date = sdf.format(myCalendar.getTime());


            Book book = new Book(title,content,img,name,username1,date);
            Call<Book> call = severDB.postNew(book);
            call.enqueue(new Callback<Book>() {
                @Override
                public void onResponse(Call<Book> call, Response<Book> response) {
                    Intent i = new Intent(New_Posts.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }

                @Override
                public void onFailure(Call<Book> call, Throwable t) {

                }
            });

            }
        });
    }

    private void back() {
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> New_Posts.this.onBackPressed());

    }
}