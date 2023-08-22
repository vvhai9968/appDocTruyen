package vuvanhai568.project.appDocTruyen.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import vuvanhai568.project.appDocTruyen.Adapter.AdapterItem;
import vuvanhai568.project.appDocTruyen.Modal.Book;
import vuvanhai568.project.appDocTruyen.Modal.getLinkAPI;
import vuvanhai568.project.appDocTruyen.R;
import vuvanhai568.project.appDocTruyen.SeverDB;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Fragment_Search extends Fragment {
    private String API = getLinkAPI.API;
    RecyclerView rcv;
    ArrayList<Book> arrayList = new ArrayList<>();
    AdapterItem adapterItem;
    Retrofit retrofit;
    SeverDB severDB;
    EditText search;
    ImageView btnSearch;

    public Fragment_Search() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__search, container, false);
        rcv = view.findViewById(R.id.rcv_sp_search);
        search = view.findViewById(R.id.ed_search);
        btnSearch = view.findViewById(R.id.img_search);

        retrofit = new Retrofit.Builder()
                .baseUrl(API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        severDB = retrofit.create(SeverDB.class);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList.clear();
                if (search.length() == 0) {
                    loadData();
                } else {
                    Call<List<Book>> call = severDB.search(search.getText().toString());

                    call.enqueue(new Callback<List<Book>>() {
                        @Override
                        public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                            Toast.makeText(getContext(), "ok", Toast.LENGTH_SHORT).show();

                            for (Book b : response.body()) {
                                Book book = new Book(b.getId(), b.getTitle(), b.getContent(), b.getImage(), b.getAuthor(), b.getIdauthor(), b.getDate(), b.getBinhLuan());
                                arrayList.add(book);
                            }
                            adapterItem.setData(arrayList);
                        }

                        @Override
                        public void onFailure(Call<List<Book>> call, Throwable t) {

                        }
                    });
                }
            }
        });

        adapterItem = new AdapterItem(getContext());
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        rcv.setLayoutManager(layoutManager);
        rcv.setAdapter(adapterItem);

        loadData();

        return view;
    }

    private void loadData() {
        Call<List<Book>> call = severDB.getAllBook();
        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, retrofit2.Response<List<Book>> response) {
                for (Book b : response.body()) {
                    Book book = new Book(b.getId(), b.getTitle(), b.getContent(), b.getImage(), b.getAuthor(), b.getIdauthor(), b.getDate(), b.getBinhLuan());
                    arrayList.add(book);
                }
                adapterItem.setData(arrayList);
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.e("zzz", "onFailure: " + t.toString());

            }
        });
    }
}