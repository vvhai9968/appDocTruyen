package vuvanhai568.project.appDocTruyen.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

import vuvanhai568.project.appDocTruyen.Adapter.AdapterItem;
import vuvanhai568.project.appDocTruyen.Activity.AllPost;
import vuvanhai568.project.appDocTruyen.Activity.Detail_Posts;
import vuvanhai568.project.appDocTruyen.Modal.Book;
import vuvanhai568.project.appDocTruyen.Modal.getLinkAPI;
import vuvanhai568.project.appDocTruyen.R;
import vuvanhai568.project.appDocTruyen.SeverDB;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Fragment_Home extends Fragment {
    private String API = getLinkAPI.API;
    RecyclerView rcv;
    ArrayList<Book> arrayList = new ArrayList<>();
    AdapterItem adapterItem;
    TextView title, author, content,all;
    ImageView img;

    Retrofit retrofit;
    SeverDB severDB;
    LinearLayout itemHome;


    public Fragment_Home() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment__home, container, false);
        rcv = view.findViewById(R.id.rcv_Home);

        adapterItem = new AdapterItem(getContext());
        title = view.findViewById(R.id.txtfirstTitle);
        itemHome = view.findViewById(R.id.itemHome);
        author = view.findViewById(R.id.txtfirstAuthor);
        content = view.findViewById(R.id.txtfirstContect);
        img = view.findViewById(R.id.home_img);
        all = view.findViewById(R.id.xemAll);

        retrofit = new Retrofit.Builder()
                .baseUrl(API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        severDB = retrofit.create(SeverDB.class);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        rcv.setLayoutManager(layoutManager);
        rcv.setAdapter(adapterItem);
        loadData();

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), AllPost.class);
                startActivity(i);
            }
        });

        itemHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), Detail_Posts.class);

                Bundle b = new Bundle();
                String id = arrayList.get(0).getId();
                Log.d("zzz", "onClick: " + id);
                b.putString("id", id);
                i.putExtra("dataBook", b);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });


        return view;
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
                title.setText(arrayList.get(0).getTitle());
                author.setText(arrayList.get(0).getAuthor());
                content.setText(arrayList.get(0).getContent());
                adapterItem.setData(arrayList);
                loadImageFromUrl(arrayList.get(0).getImage());
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.e("zzz", "onFailure: "+t.toString() );

            }
        });
    }

    private void loadImageFromUrl(String imageUrl) {
        ImageRequest imageRequest = new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                if (bitmap != null) {
                    img.setImageBitmap(bitmap);
                } else {
                    Log.d("zzz", "Bitmap is null");
                }
            }
        }, 0, 0, null, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Volley.newRequestQueue(getContext()).add(imageRequest);
    }

}