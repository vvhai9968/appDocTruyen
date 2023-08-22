package vuvanhai568.project.appDocTruyen.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

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

public class EditPost extends AppCompatActivity {
    Retrofit retrofit;
    ImageView img;
    EditText title, edtImg, contact;
    Button btnSave;
    SeverDB severDB;
    String id;
    private String API = getLinkAPI.API;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        retrofit = new Retrofit.Builder()
                .baseUrl(API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        severDB = retrofit.create(SeverDB.class);

        img = findViewById(R.id.imgEdit);
        title = findViewById(R.id.tieuDeed);
        edtImg = findViewById(R.id.linkAnhed);
        contact = findViewById(R.id.noiDunged);
        btnSave = findViewById(R.id.btnEditPost);

        Intent intent = getIntent();
        Bundle b = intent.getBundleExtra("dataBook");
        id = b.getString("id");
        loadData(id);
        back();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Book book = new Book(title.getText().toString(),contact.getText().toString(),edtImg.getText().toString());
                Call<Book>call = severDB.editPost(id,book);
                call.enqueue(new Callback<Book>() {
                    @Override
                    public void onResponse(Call<Book> call, Response<Book> response) {
                        Toast.makeText(EditPost.this, "Sửa thành công", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(EditPost.this, MainActivity.class);
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
        toolbar.setNavigationOnClickListener(view -> EditPost.this.onBackPressed());

    }

    private void loadData(String id) {

        Call<Book> call = severDB.getOneBook(id);
        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                title.setText(response.body().getTitle());
                contact.setText(response.body().getContent());
                edtImg.setText(response.body().getImage());
                loadImageFromUrl(response.body().getImage(),img);
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {

            }
        });
    }
    private void loadImageFromUrl(String imageUrl, ImageView imageView) {
        ImageRequest imageRequest = new ImageRequest(imageUrl, new com.android.volley.Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    Log.d("zzz", "Bitmap is null");
                }
            }
        }, 0, 0, null, null, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Xử lý phản hồi lỗi nếu cần thiết
            }
        });

        Volley.newRequestQueue(getApplicationContext()).add(imageRequest);
    }
}