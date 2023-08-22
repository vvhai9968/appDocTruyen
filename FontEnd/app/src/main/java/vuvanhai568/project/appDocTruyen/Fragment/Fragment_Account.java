package vuvanhai568.project.appDocTruyen.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import vuvanhai568.project.appDocTruyen.Activity.New_Posts;
import vuvanhai568.project.appDocTruyen.Adapter.AdapterItemAccount;
import vuvanhai568.project.appDocTruyen.Modal.Book;
import vuvanhai568.project.appDocTruyen.Modal.User;
import vuvanhai568.project.appDocTruyen.Modal.getLinkAPI;
import vuvanhai568.project.appDocTruyen.R;
import vuvanhai568.project.appDocTruyen.SeverDB;
import vuvanhai568.project.appDocTruyen.onClickAccount;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Fragment_Account extends Fragment implements onClickAccount {

    TextView name;
    RecyclerView rcv;
    Button edit, add;
    private String API = getLinkAPI.API;
    Retrofit retrofit;
    SeverDB serverDB;
    ArrayList<Book> arrayList = new ArrayList<>();
    AdapterItemAccount adapterItem;

    public Fragment_Account() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment__account, container, false);
        name = view.findViewById(R.id.txtName);
        edit = view.findViewById(R.id.edit_profile);
        add = view.findViewById(R.id.btn_new);
        retrofit = new Retrofit.Builder()
                .baseUrl(API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        serverDB = retrofit.create(SeverDB.class);

        rcv = view.findViewById(R.id.rcvAcc);
        adapterItem = new AdapterItemAccount(getContext(), this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        rcv.setLayoutManager(layoutManager);
        rcv.setAdapter(adapterItem);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        String username1 = sharedPreferences.getString("username", "");
        getData(username1);

        loadData(username1);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), New_Posts.class);
                startActivity(i);

            }
        });
        return view;
    }

    private void loadData(String id) {
        arrayList.clear();
        Call<List<Book>> call = serverDB.getAllBookAcount(id);
        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
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

    private void getData(String id) {
        Call<User> call = serverDB.getOnePerson(id);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("name", response.body().getFullname());
                editor.apply();

                name.setText(response.body().getFullname());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });


    }


    @Override
    public void delete(String id, String name) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        String username1 = sharedPreferences.getString("username", "");

        AlertDialog.Builder aBuilder = new AlertDialog.Builder(getContext());
        aBuilder.setMessage("Bạn Có Muốn Xóa " + name + " Không ?");

        aBuilder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Call<Book> call = serverDB.deleteById(id);
                        call.enqueue(new Callback<Book>() {
                            @Override
                            public void onResponse(Call<Book> call, Response<Book> response) {
                                Toast.makeText(getContext(), "Xóa thành công truyện " + name, Toast.LENGTH_SHORT).show();
                                loadData(username1);

                            }

                            @Override
                            public void onFailure(Call<Book> call, Throwable t) {

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






