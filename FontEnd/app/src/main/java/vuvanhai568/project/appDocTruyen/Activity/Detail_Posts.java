package vuvanhai568.project.appDocTruyen.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

import vuvanhai568.project.appDocTruyen.Adapter.AdapterItemCmt;
import vuvanhai568.project.appDocTruyen.Modal.Book;
import vuvanhai568.project.appDocTruyen.Modal.getLinkAPI;
import vuvanhai568.project.appDocTruyen.R;
import vuvanhai568.project.appDocTruyen.SeverDB;
import vuvanhai568.project.appDocTruyen.onClickComment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Detail_Posts extends AppCompatActivity implements onClickComment {

    Retrofit retrofit;
    ImageView img;
    TextView title, author, date, contact;
    EditText edtComment;
    Button btnSend;
    RecyclerView rcv;

    SeverDB severDB;
    AdapterItemCmt adapterItem;
    String id;

    ArrayList<Book.Comment> arrayList = new ArrayList<>();
    private String API = getLinkAPI.API;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_posts);
        back();

        img = findViewById(R.id.imgDetail);
        title = findViewById(R.id.txtTieuDe);
        author = findViewById(R.id.txtTacGia);
        date = findViewById(R.id.txtNgay);
        contact = findViewById(R.id.txtNoiDung);
        edtComment = findViewById(R.id.edtComment);
        btnSend = findViewById(R.id.btnSend);
        rcv = findViewById(R.id.rcvItem);

        retrofit = new Retrofit.Builder()
                .baseUrl(API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        severDB = retrofit.create(SeverDB.class);

        adapterItem = new AdapterItemCmt(getApplicationContext(), this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        rcv.setLayoutManager(layoutManager);
        rcv.setAdapter(adapterItem);

        Intent intent = getIntent();
        Bundle b = intent.getBundleExtra("dataBook");
        id = b.getString("id");
        loadData(id);

        SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        String username1 = sharedPreferences.getString("username", "");
        String name = sharedPreferences.getString("name", "");

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Book.Comment comment = new Book.Comment(username1, name, edtComment.getText().toString());
                Call<Book> call = severDB.addComment(id, comment);
                call.enqueue(new Callback<Book>() {
                    @Override
                    public void onResponse(Call<Book> call, Response<Book> response) {
                        Toast.makeText(Detail_Posts.this, "Thêm bình luận thành công", Toast.LENGTH_SHORT).show();
                        loadData(id);
                        edtComment.setText(" ");
                    }

                    @Override
                    public void onFailure(Call<Book> call, Throwable t) {
                        Toast.makeText(Detail_Posts.this, "Err", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void loadData(String id) {

        Call<Book> call = severDB.getOneBook(id);
        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                title.setText(response.body().getTitle());
                author.setText(response.body().getAuthor());
                date.setText(response.body().getDate());
                contact.setText(response.body().getContent());

                List<Book.Comment> comments = response.body().getBinhLuan();

                arrayList.clear();
                if (comments != null) {
                    for (Book.Comment comment : comments) {
                        Book.Comment comment1 = new Book.Comment(comment.get_id(), comment.getIdPerson(), comment.getFullName(), comment.getCmt());
                        arrayList.add(comment1);
                    }
                    adapterItem.setDataCmt(arrayList);
                }

                loadImageFromUrl(response.body().getImage(), img);
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

    private void back() {
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> Detail_Posts.this.onBackPressed());

    }

    @Override
    public void EditComment(String idPerson, String idComent, String cmt) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Detail_Posts.this);
        alertDialogBuilder.setTitle("Sửa Comment");

        EditText newCmt = new EditText(Detail_Posts.this);
        newCmt.setText(cmt);

        alertDialogBuilder.setView(newCmt);

        alertDialogBuilder.setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Book.Comment comment = new Book.Comment(newCmt.getText().toString());
                Call<Book.Comment> call = severDB.editComment(id,idPerson,idComent,comment);
                call.enqueue(new Callback<Book.Comment>() {
                    @Override
                    public void onResponse(Call<Book.Comment> call, Response<Book.Comment> response) {
                        Toast.makeText(Detail_Posts.this, "Sửa comment thành công", Toast.LENGTH_SHORT).show();
                        loadData(id);
                    }

                    @Override
                    public void onFailure(Call<Book.Comment> call, Throwable t) {

                    }
                });
            }
        });

        alertDialogBuilder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void DeleteComment(String idPerson, String idComent, String cmt) {
        AlertDialog.Builder aBuilder = new AlertDialog.Builder(this);
        aBuilder.setMessage("Bạn Có Muốn Xóa Comment " + cmt + " Không ?");
        aBuilder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Call<Book.Comment> call = severDB.deleteComment(id, idPerson, idComent);
                call.enqueue(new Callback<Book.Comment>() {
                    @Override
                    public void onResponse(Call<Book.Comment> call, Response<Book.Comment> response) {
                        Toast.makeText(Detail_Posts.this, "Xóa comment thành công", Toast.LENGTH_SHORT).show();
                        loadData(id);
                    }

                    @Override
                    public void onFailure(Call<Book.Comment> call, Throwable t) {

                    }
                });


            }
        });

        aBuilder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        aBuilder.show();


    }
}