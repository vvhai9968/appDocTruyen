package vuvanhai568.project.appDocTruyen.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import vuvanhai568.project.appDocTruyen.Adapter.AdapterItem;
import vuvanhai568.project.appDocTruyen.Modal.Book;
import vuvanhai568.project.appDocTruyen.Modal.getLinkAPI;
import vuvanhai568.project.appDocTruyen.R;
import vuvanhai568.project.appDocTruyen.SeverDB;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AllPost extends AppCompatActivity {
    private String API = getLinkAPI.API;
    RecyclerView rcv;
    ArrayList<Book> arrayList = new ArrayList<>();
    AdapterItem adapterItem;
    Retrofit retrofit;
    SeverDB severDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_post);
        retrofit = new Retrofit.Builder()
                .baseUrl(API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        severDB = retrofit.create(SeverDB.class);

        adapterItem = new AdapterItem(getApplicationContext());
        rcv = findViewById(R.id.rcvAll);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        rcv.setLayoutManager(layoutManager);
        rcv.setAdapter(adapterItem);
        loadData();
        back();
    }

    private void back() {
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> AllPost.this.onBackPressed());

    }

    private void loadData() {
        Call<List<Book>> call = severDB.getAllBook();
        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, retrofit2.Response<List<Book>> response) {
                for (Book b : response.body()) {
                    Book book = new Book(b.getId(),b.getTitle(),b.getContent(),b.getImage(),b.getAuthor(),b.getIdauthor(),b.getDate(),b.getBinhLuan());
                    arrayList.add(book);
                }
                adapterItem.setData(arrayList);
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.e("zzz", "onFailure: "+t.toString() );

            }
        });
    }

}