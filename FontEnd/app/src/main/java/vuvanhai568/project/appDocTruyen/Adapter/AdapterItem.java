package vuvanhai568.project.appDocTruyen.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

import vuvanhai568.project.appDocTruyen.Activity.Detail_Posts;
import vuvanhai568.project.appDocTruyen.Modal.Book;
import vuvanhai568.project.appDocTruyen.R;

public class AdapterItem extends RecyclerView.Adapter<AdapterItem.ViewHolder> {
    ArrayList<Book> arrayList;
    Context context;

    public AdapterItem(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<Book> arr) {
        arrayList = arr;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new AdapterItem.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (arrayList == null) {
            return;
        }
        Book book = arrayList.get(position);
        holder.author.setText(book.getAuthor());
        holder.title.setText(book.getTitle());
        holder.comment.setText(String.valueOf(book.getBinhLuanCount()));
        Log.d("zzz", "onBindViewHolder: "+book.getBinhLuanCount());

        loadImageFromUrl(book.getImage(), holder.img);

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, Detail_Posts.class);

                Bundle b = new Bundle();
                String id = book.getId();
                Log.d("zzz", "onClick: " + id);
                b.putString("id", id);
                i.putExtra("dataBook", b);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
    }

    private void loadImageFromUrl(String imageUrl, ImageView imageView) {
        ImageRequest imageRequest = new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    Log.d("zzz", "Bitmap is null");
                }
            }
        }, 0, 0, null, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Xử lý phản hồi lỗi nếu cần thiết
            }
        });

        Volley.newRequestQueue(context).add(imageRequest);
    }


    @Override
    public int getItemCount() {
        if (arrayList != null) {
            return arrayList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView author, title, comment;
        LinearLayout item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.imgBook);
            title = itemView.findViewById(R.id.txtTitle);
            author = itemView.findViewById(R.id.txtAuthor);
            comment = itemView.findViewById(R.id.comment);
            item = itemView.findViewById(R.id.item);
        }
    }
}
